package br.com.senaigo.helpdesk.api.security.model;

import br.com.senaigo.helpdesk.api.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUser {
	
	private String token;
	private User user;
	
	public CurrentUser(String token, User user) {
		super();
		this.token = token;
		this.user = user;
	}
}
