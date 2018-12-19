package br.com.senaigo.helpdesk.api.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.senaigo.helpdesk.api.enumeration.ProfileEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class User {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	@NotBlank(message = "E-mail requerido")
	@Email(message = "E-mail inválido")
	private String email;
	
	@NotBlank(message = "Senha requerida")
	@Size(min = 6)
	private String password;
	
	private ProfileEnum profileEnum;
}