package br.com.senaigo.helpdesk.api.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.senaigo.helpdesk.api.enumeration.PrioridadeEnum;
import br.com.senaigo.helpdesk.api.enumeration.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Ticket {
	
	@Id
	private String id;
	
	private Date data;
	private Integer numero;
	private String titulo;
	private String descricao;
	private String imagem;
	private StatusEnum statusEnum;
	private PrioridadeEnum prioridadeEnum;
	
	@DBRef(lazy = true)
	private User assignedUser;
	
	@DBRef(lazy = true)
	private User user;
	
	@Transient
	private List<ChangeStatus> listaChangeStatus;
}
