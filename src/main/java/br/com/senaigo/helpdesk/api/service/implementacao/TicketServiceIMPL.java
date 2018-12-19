package br.com.senaigo.helpdesk.api.service.implementacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.senaigo.helpdesk.api.entity.ChangeStatus;
import br.com.senaigo.helpdesk.api.entity.Ticket;
import br.com.senaigo.helpdesk.api.repository.ChangeStatusRepository;
import br.com.senaigo.helpdesk.api.repository.TicketRepository;
import br.com.senaigo.helpdesk.api.service.TicketService;

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
	
}
