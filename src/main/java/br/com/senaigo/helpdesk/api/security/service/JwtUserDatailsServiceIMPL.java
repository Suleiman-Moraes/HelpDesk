package br.com.senaigo.helpdesk.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.security.jwt.JwtUserFactory;
import br.com.senaigo.helpdesk.api.service.UserService;

@Service
public class JwtUserDatailsServiceIMPL implements UserDetailsService{

	@Autowired
	private UserService service;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = service.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("Usuário não encontrado com o e-mail \"%s\".", email));
		}
		else {
			return JwtUserFactory.create(user);
		}
	}
	
}
