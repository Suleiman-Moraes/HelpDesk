package br.com.senaigo.helpdesk.api.service.implementacao;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.mongodb.DuplicateKeyException;

import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.repository.UserRepository;
import br.com.senaigo.helpdesk.api.response.Response;
import br.com.senaigo.helpdesk.api.service.UserService;
import lombok.Getter;

@Getter
@Service
public class UserServiceIMPL implements UserService {

	@Autowired
	private UserRepository persistencia;
	
	@Override
	public User findByEmail(String email) {
		return this.getPersistencia().findByEmail(email);
	}

	@Override
	public User createOrUpdate(User user) {
		return this.getPersistencia().save(user);
	}

	@Override
	public User findById(String id) {
		return this.getPersistencia().findById(id).get();
	}

	@Override
	public void delete(String id) {
		this.getPersistencia().deleteById(id);
	}

	@Override
	public Page<User> findAll(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.getPersistencia().findAll(pages);
	}
	
	@Override
	public ResponseEntity<Response<User>> prepararCreateOrUpdate(HttpServletRequest request, User user, BindingResult result,
			String validacao, PasswordEncoder passwordEncoder) {
		Response<User> response = new Response<>();
		try {
			this.getClass().getDeclaredMethod(validacao, User.class, BindingResult.class).invoke(user, request);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User userPersisted = (User) createOrUpdate(user);
			response.setData(userPersisted);
		} catch (DuplicateKeyException dE) {
			response.getErros().add("E-mail já está registrado");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	public void validateCreateUser(User user, BindingResult result) {
		this.validateEmaiOfUser(user, result);
	}

	private void validateEmaiOfUser(User user, BindingResult result) {
		if (user.getEmail() == null) {
			result.addError(new ObjectError("User", "E-mail não informado"));
		}
	}

	public void validateUpdateUser(User user, BindingResult result) {
		if (user.getId() == null) {
			result.addError(new ObjectError("User", "Id não informado"));
		}
		this.validateEmaiOfUser(user, result);
	}
}
