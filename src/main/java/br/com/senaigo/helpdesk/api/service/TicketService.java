package br.com.senaigo.helpdesk.api.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import br.com.senaigo.helpdesk.api.entity.ChangeStatus;
import br.com.senaigo.helpdesk.api.entity.Ticket;
import br.com.senaigo.helpdesk.api.response.Response;
import br.com.senaigo.helpdesk.api.security.jwt.JwtTokenUtil;

@Component
public interface TicketService {
	static final String VALIDATEUPDATEUSER = "validateUpdateUser";
	static final String VALIDATECREATETICKET = "validateCreateTicket";
	
	Ticket createOrUpdate(Ticket objeto);
	
	Ticket findById(String id);
	
	void delete(String id);
	
	Page<Ticket> listTicket(int page, int count);
	
	ChangeStatus createChangeStatus(ChangeStatus changeStatus);
	
	Iterable<ChangeStatus> listChangeStatus(String ticketId);
	
	Page<Ticket> finByCrurrentUser(int page, int count, String userId);
	
	Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status, String prioridade, String userId);
	
	Page<Ticket> findByParameters(int page, int count, String title, String status, String prioridade);
	
	Page<Ticket> findByNumber(int page, int count, Integer number);
	
	Iterable<Ticket> findAll();
	
	Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status, String prioridade, String assignedUser);
	
	/**
	 * @author suleiman-am
	 * @param request
	 * @param ticket
	 * @param result
	 * @param validacao
	 * @param jwtTokenUtil
	 * @param userService
	 * @return
	 */
	ResponseEntity<Response<Ticket>> prepararCreateOrUpdate(HttpServletRequest request, Ticket ticket, BindingResult result,
			String validacao, JwtTokenUtil jwtTokenUtil, UserService userService);
}
