package br.com.senaigo.helpdesk.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.senaigo.helpdesk.api.entity.Ticket;
import br.com.senaigo.helpdesk.api.entity.User;
import br.com.senaigo.helpdesk.api.response.Response;
import br.com.senaigo.helpdesk.api.security.jwt.JwtTokenUtil;
import br.com.senaigo.helpdesk.api.service.TicketService;
import br.com.senaigo.helpdesk.api.service.UserService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	protected JwtTokenUtil jwtTokenUtil;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		return ticketService.prepararCreateOrUpdate(request, user, result, UserService.VALIDATECREATEUSER, passwordEncoder);
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> update(HttpServletRequest request, @RequestBody User user,
			BindingResult result) {
		return userService.prepararCreateOrUpdate(request, user, result, UserService.VALIDATEUPDATEUSER, passwordEncoder);
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<User>> findById(@PathVariable("id") String id) {
		Response<User> response = new Response<>();
		User user = userService.findById(id);
		if(user == null) {
			response.getErros().add("ID não registrado == " + id);
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(user);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> deleteById(@PathVariable("id") String id) {
		Response<String> response = new Response<>();
		User user = userService.findById(id);
		if(user == null) {
			response.getErros().add("ID não registrado == " + id);
			return ResponseEntity.badRequest().body(response);
		}
		userService.delete(id);
		return ResponseEntity.ok(new Response<>());
	}
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Page<User>>> findAll(@PathVariable("page") int page, @PathVariable("count") int count) {
		Response<Page<User>> response = new Response<>();
		Page<User> listaUser = userService.findAll(page, count);
		response.setData(listaUser);
		return ResponseEntity.ok(response);
	}
}
