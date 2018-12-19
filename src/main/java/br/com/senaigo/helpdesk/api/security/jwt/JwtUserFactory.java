package br.com.senaigo.helpdesk.api.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.enumeration.ProfileEnum;

public class JwtUserFactory {
	private JwtUserFactory() {}
	
	public static JwtUser create(User objeto) {
		return new JwtUser(objeto.getId(), objeto.getEmail(), objeto.getPassword(), mapToGrantedAthorities(objeto.getProfileEnum()));
	}
	
	private static List<GrantedAuthority> mapToGrantedAthorities(ProfileEnum objeto) {
		List<GrantedAuthority> listaGrantedAuthority = new ArrayList<>();
		listaGrantedAuthority.add(new SimpleGrantedAuthority(objeto.toString()));
		return listaGrantedAuthority;
	}
}
