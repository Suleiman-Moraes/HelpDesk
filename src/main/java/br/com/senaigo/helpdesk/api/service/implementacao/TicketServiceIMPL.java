package br.com.senaigo.helpdesk.api.service.implementacao;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import br.com.senaigo.helpdesk.api.entity.ChangeStatus;
import br.com.senaigo.helpdesk.api.entity.Ticket;
import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.enumeration.StatusEnum;
import br.com.senaigo.helpdesk.api.repository.ChangeStatusRepository;
import br.com.senaigo.helpdesk.api.repository.TicketRepository;
import br.com.senaigo.helpdesk.api.response.Response;
import br.com.senaigo.helpdesk.api.security.jwt.JwtTokenUtil;
import br.com.senaigo.helpdesk.api.service.TicketService;
import br.com.senaigo.helpdesk.api.service.UserService;

@Service
public class TicketServiceIMPL implements TicketService{
	
	@Autowired
	private TicketRepository persistencia;

	@Autowired
	private ChangeStatusRepository changeStatusRepository;
	
	@Override
	public Ticket createOrUpdate(Ticket objeto) {
		return persistencia.save(objeto);
	}

	@Override
	public Ticket findById(String id) {
		return persistencia.findById(id).get();
	}

	@Override
	public void delete(String id) {
		persistencia.deleteById(id);
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return persistencia.findAll(pages);
	}

	@Override
	public ChangeStatus createChangeStatus(ChangeStatus changeStatus) {
		return changeStatusRepository.save(changeStatus);
	}

	@Override
	public Iterable<ChangeStatus> listChangeStatus(String ticketId) {
		return changeStatusRepository.findByTicketIdOrderByDataAlteracaoDesc(ticketId);
	}

	@Override
	public Page<Ticket> finByCrurrentUser(int page, int count, String userId) {
		Pageable pages = PageRequest.of(page, count);
		return persistencia.findByUserIdOrderByDataDesc(pages, userId);
	}

	@Override
	public Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status,
			String prioridade, String userId) {
		Pageable pages = PageRequest.of(page, count);
		return persistencia.findByTituloIgnoreCaseContainingAndStatusEnumAndPrioridadeEnumAndUserIdOrderByDataDesc(title, status, prioridade, pages);
	}

	@Override
	public Page<Ticket> findByParameters(int page, int count, String title, String status, String prioridade) {
		Pageable pages = PageRequest.of(page, count);
		return persistencia.findByTituloIgnoreCaseContainingAndStatusEnumAndPrioridadeEnumOrderByDataDesc(title, status, prioridade, pages);
	}

	@Override
	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		Pageable pages = PageRequest.of(page, count);
		return persistencia.findByNumero(number, pages);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return persistencia.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status,
			String prioridade, String assignedUser) {
		Pageable pages = PageRequest.of(page, count);
		return persistencia.findByTituloIgnoreCaseContainingAndStatusEnumAndPrioridadeEnumAndAssignedUserIdOrderByDataDesc(title, status, prioridade, pages);
	}
	
	@Override
	public ResponseEntity<Response<Ticket>> prepararCreateOrUpdate(HttpServletRequest request, Ticket ticket, BindingResult result,
			String validacao, JwtTokenUtil jwtTokenUtil, UserService userService) {
		Response<Ticket> response = new Response<>();
		try {
			this.getClass().getDeclaredMethod(validacao, User.class, BindingResult.class).invoke(this, ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			if(validacao.equals(TicketService.VALIDATECREATETICKET)) {
				ticket.setStatusEnum(StatusEnum.NOVO);
				ticket.setUser(userFromRequest(request, jwtTokenUtil, userService));
				ticket.setData(new Date());
				ticket.setNumero(generateNumber());
			}
			else if(validacao.equals(TicketService.VALIDATEUPDATETICKET)){
				Ticket ticketCurrent = findById(ticket.getId());
				ticket.setStatusEnum(ticketCurrent.getStatusEnum());
				ticket.setUser(ticketCurrent.getUser());
				ticket.setData(ticketCurrent.getData());
				ticket.setNumero(ticketCurrent.getNumero());
				if(ticketCurrent.getAssignedUser() != null) {
					ticket.setAssignedUser(ticketCurrent.getAssignedUser());
				}
			}
			Ticket ticketPersisted = (Ticket) createOrUpdate(ticket);
			response.setData(ticketPersisted);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}

	@Override
	public User userFromRequest(HttpServletRequest request, JwtTokenUtil jwtTokenUtil, UserService userService) {
		final String token = request.getHeader("Authorization");
		final String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}

	public void validateCreateTicket(Ticket ticket, BindingResult result) {
		validateTicketNotNullAndTituloOfTicket(ticket, result);
	}

	private void validateTicketNotNullAndTituloOfTicket(Ticket ticket, BindingResult result) {
		if(ticket.getTitulo() == null) {
			result.addError(new ObjectError("Ticket", "Título não informado"));
		}
	}

	public void validateUpdateTicket(Ticket ticket, BindingResult result) {
		validateTicketNotNullAndTituloOfTicket(ticket, result);
		if(ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "ID não informado"));
		}
	}
}
