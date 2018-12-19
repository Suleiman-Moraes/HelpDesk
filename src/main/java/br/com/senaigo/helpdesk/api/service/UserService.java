package br.com.senaigo.helpdesk.api.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.response.Response;

public interface UserService {
	static final String VALIDATEUPDATEUSER = "validateUpdateUser";
	static final String VALIDATECREATEUSER = "validateCreateUser";
	
	User findByEmail(String email);
	
	User createOrUpdate(User user);
	
	User findById(String id);
	
	void delete(String id);
	
	Page<User> findAll(int page, int count);
	
	/**
	 * 
	 * @param request
	 * @param user
	 * @param result
	 * @param validacao
	 * @param passwordEncoder
	 * @return Tudo que devolve para o usuario
	 * @author suleiman-am
	 */
	ResponseEntity<Response<User>> prepararCreateOrUpdate(HttpServletRequest request, User user, BindingResult result,
			String validacao, PasswordEncoder passwordEncoder);
}
