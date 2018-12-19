package br.com.senaigo.helpdesk.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.senaigo.helpdesk.api.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	Page<Ticket> findByUserIdOrderByDataDesc(Pageable pages, String userID);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusEnumAndPrioridadeEnumOrderByDataDesc(String titulo,
			String statusEnum, String prioridadeEnum, Pageable pages);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusEnumAndPrioridadeEnumAndUserIdOrderByDataDesc(String titulo,
			String statusEnum, String prioridadeEnum, Pageable pages);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusEnumAndPrioridadeEnumAndAssignedUserIdOrderByDataDesc(
			String titulo, String statusEnum, String prioridadeEnum, Pageable pages);

	Page<Ticket> findByNumero(Integer number, Pageable pages);
}
