package br.com.senaigo.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.senaigo.helpdesk.api.entity.ChangeStatus;

public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String> {
	Iterable<ChangeStatus> findByTicketIdOrderByDataAlteracaoDesc(String ticket);
}
