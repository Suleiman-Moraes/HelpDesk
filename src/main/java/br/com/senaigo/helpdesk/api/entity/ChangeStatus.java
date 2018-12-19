package br.com.senaigo.helpdesk.api.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.senaigo.helpdesk.api.enumeration.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class ChangeStatus {
	@Id
	private String id;
	
	@DBRef
	private Ticket ticket;
	
	@DBRef
	private User userChange;
	
	private Date dataAlteracao;
	private StatusEnum statusEnum;
}
