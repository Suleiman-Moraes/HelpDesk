package br.com.senaigo.helpdesk.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.senaigo.helpdesk.api.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	Page<Ticket> findByUserIdOrderByDataDesc(Pageable pages, String userID);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusEnumIgnoreCaseContainingAndPrioridadeEnumIgnoreCaseContainingOrderByDataDesc(String titulo,
			String statusEnum, String prioridadeEnum, Pageable pages);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusEnumIgnoreCaseContainingAndPrioridadeEnumIgnoreCaseContainingAndUserIdOrderByDataDesc(
			String titulo, String statusEnum, String prioridadeEnum, String userId, Pageable pages);

	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusEnumIgnoreCaseContainingAndPrioridadeEnumIgnoreCaseContainingAndAssignedUserIdOrderByDataDesc(
			String titulo, String statusEnum, String prioridadeEnum, String userId, Pageable pages);

	Page<Ticket> findByNumero(Integer number, Pageable pages);
}
