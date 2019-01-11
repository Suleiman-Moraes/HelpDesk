package br.com.senaigo.helpdesk.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senaigo.helpdesk.api.dto.Summary;
import br.com.senaigo.helpdesk.api.entity.ChangeStatus;
import br.com.senaigo.helpdesk.api.entity.Ticket;
import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.enumeration.ProfileEnum;
import br.com.senaigo.helpdesk.api.enumeration.StatusEnum;
import br.com.senaigo.helpdesk.api.response.Response;
import br.com.senaigo.helpdesk.api.security.jwt.JwtTokenUtil;
import br.com.senaigo.helpdesk.api.service.TicketService;
import br.com.senaigo.helpdesk.api.service.UserService;
import br.com.senaigo.helpdesk.api.util.ICRUDSimples;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController{

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	protected JwtTokenUtil jwtTokenUtil;

	@PostMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		return ticketService.prepararCreateOrUpdate(request, ticket, result, TicketService.VALIDATECREATETICKET,
				jwtTokenUtil, userService);
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		return ticketService.prepararCreateOrUpdate(request, ticket, result, TicketService.VALIDATEUPDATETICKET,
				jwtTokenUtil, userService);
	}

	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id) {
		ResponseEntity<Response<Ticket>> responseEntity = ICRUDSimples.interfaceFindById(id, ticketService);
		Ticket ticket = responseEntity.getBody().getData();
		List<ChangeStatus> listaChangeStatus = new ArrayList<>();
		Iterable<ChangeStatus> listaChangeStatusCurrent = ticketService.listChangeStatus(ticket.getId());
		for (Iterator<ChangeStatus> iterator = listaChangeStatusCurrent.iterator(); iterator.hasNext();) {
			ChangeStatus changeStatus = (ChangeStatus) iterator.next();
			changeStatus.setTicket(null);
			listaChangeStatus.add(changeStatus);
		}
		ticket.setListaChangeStatus(listaChangeStatus);
		responseEntity.getBody().setData(ticket);
		return responseEntity;
	}

	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<String>> deleteById(@PathVariable("id") String id) {
		return ICRUDSimples.interfaceDeleteById(id, ticketService);
	}
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("count") int count) {
		Response<Page<Ticket>> response = new Response<>();
		Page<Ticket> listaTicket = null;
		User userRequest = ticketService.userFromRequest(request, jwtTokenUtil, userService);
		if(userRequest.getProfileEnum().equals(ProfileEnum.ROLE_TECHNICIAN)) {
			listaTicket = ticketService.listTicket(page, count);
		}
		else if(userRequest.getProfileEnum().equals(ProfileEnum.ROLE_CUSTOMER)){
			listaTicket = ticketService.finByCrurrentUser(page, count, userRequest.getId());
		}
		
		response.setData(listaTicket);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "{page}/{count}/{number}/{title}/{status}/{priority}/{assigned}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findByParams(HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("count") int count, @PathVariable("number") Integer number, @PathVariable("title") String title, 
			@PathVariable("status") String status, @PathVariable("priority") String priority, 
			@PathVariable("assigned") boolean assigned) {
		
		title = title.equals("uninformed") ? "" : title;
		status = status.equals("uninformed") ? "" : status;
		priority = priority.equals("uninformed") ? "" : priority;
		
		Response<Page<Ticket>> response = new Response<>();
		Page<Ticket> listaTicket = null;
		if(number > 0) {
			listaTicket = ticketService.findByNumber(page, count, number);
		}
		else {
			User userRequest = ticketService.userFromRequest(request, jwtTokenUtil, userService);
			if(userRequest.getProfileEnum().equals(ProfileEnum.ROLE_TECHNICIAN)) {
				if(assigned) {
					listaTicket = ticketService.findByParameterAndAssignedUser(page, count, title, status, 
							priority, userRequest.getId());
				}
				else {
					listaTicket = ticketService.findByParameters(page, count, title, status, priority);
				}
			}
			else if(userRequest.getProfileEnum().equals(ProfileEnum.ROLE_CUSTOMER)){
				listaTicket = ticketService.findByParametersAndCurrentUser(page, count, title, status, priority, userRequest.getId());
			}
		}
		
		response.setData(listaTicket);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping(value = "{id}/{status}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> changeStatus(@PathVariable("id") String id, @PathVariable("status") String status
			, HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result){
		Response<Ticket> response = new Response<>();
		try {
			ticketService.validateChangeStatus(ticket, status, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(id);
			ticketCurrent.setStatusEnum(StatusEnum.getStatus(status));
			if(status.equalsIgnoreCase("DESIGNADO")) {
				ticketCurrent.setAssignedUser(ticketService.userFromRequest(request, jwtTokenUtil, userService));
			}
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticketCurrent);
			ChangeStatus changeStatus = new ChangeStatus();
			changeStatus.setUserChange(ticketService.userFromRequest(request, jwtTokenUtil, userService));
			changeStatus.setDataAlteracao(new Date());
			changeStatus.setStatusEnum(StatusEnum.getStatus(status));
			changeStatus.setTicket(ticketPersisted);
			ticketService.createChangeStatus(changeStatus);
			response.setData(ticketPersisted);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/summary")
	public ResponseEntity<Response<Summary>> findSummary(){
		Response<Summary> response = new Response<>();
		Summary summary = new Summary();
		Integer amountNovo = 0;
		Integer amountResolvido = 0;
		Integer amountAprovado = 0;
		Integer amountDesaprovado = 0;
		Integer amountDesignado = 0;
		Integer amountFechado = 0;
		
		Iterable<Ticket> listaTicket = ticketService.findAll();
		if(listaTicket != null) {
			for (Iterator<Ticket> iterator = listaTicket.iterator(); iterator.hasNext();) {
				Ticket ticket = iterator.next();
				switch (ticket.getStatusEnum()) {
					case NOVO:
						amountNovo ++;
						break;
					case RESOLVIDO:
						amountResolvido ++;
						break;
					case APROVADO:
						amountAprovado ++;
						break;
					case DESAPROVADO:
						amountDesaprovado ++;
						break;
					case DESIGNADO:
						amountDesignado ++;
						break;
					case FECHADO:
						amountFechado ++;
						break;
				}
			}
		}
		summary.setSummary(amountNovo, amountResolvido, amountAprovado, amountDesaprovado, amountDesignado, amountFechado);
		response.setData(summary);
		return ResponseEntity.ok(response);
	}
}
