package br.com.senaigo.helpdesk.api.controller;

import java.util.ArrayList;
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

import br.com.senaigo.helpdesk.api.controller.interfaces.ICRUDSimples;
import br.com.senaigo.helpdesk.api.entity.ChangeStatus;
import br.com.senaigo.helpdesk.api.entity.Ticket;
import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.enumeration.ProfileEnum;
import br.com.senaigo.helpdesk.api.response.Response;
import br.com.senaigo.helpdesk.api.security.jwt.JwtTokenUtil;
import br.com.senaigo.helpdesk.api.service.TicketService;
import br.com.senaigo.helpdesk.api.service.UserService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController implements ICRUDSimples<Ticket>{

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
		ResponseEntity<Response<Ticket>> responseEntity = interfaceFindById(id, ticketService);
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
		return interfaceDeleteById(id, ticketService);
	}

	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("count") int count) {
		Response<Page<Ticket>> response = new Response<>();
		Page<Ticket> listaUser = null;
		User userRequest = ticketService.userFromRequest(request, jwtTokenUtil, userService);
		if(userRequest.getProfileEnum().equals(ProfileEnum.ROLE_TECHNICIAN)) {
			
		}
		
//		Page<User> listaUser = userService.findAll(page, count);
		response.setData(listaUser);
		return ResponseEntity.ok(response);
	}
}
