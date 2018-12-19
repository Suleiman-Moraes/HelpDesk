package br.com.senaigo.helpdesk.api.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import br.com.senaigo.helpdesk.api.entity.ChangeStatus;
import br.com.senaigo.helpdesk.api.entity.Ticket;

@Component
public interface TicketService {
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
}
