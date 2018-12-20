package br.com.senaigo.helpdesk.api.controller.interfaces;

import org.springframework.http.ResponseEntity;

import br.com.senaigo.helpdesk.api.response.Response;

public interface ICRUDSimples<T> {
	/**
	 * 
	 * @param id
	 * @param service
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default ResponseEntity<Response<T>> interfaceFindById(String id, Object service) {
		Response<T> response = new Response<>();
		try {
			T objeto = (T) service.getClass().getMethod("findById", String.class).invoke(service.getClass(), id);
			if (objeto == null) {
				response.getErros().add("ID não registrado == " + id);
				return ResponseEntity.badRequest().body(response);
			}
			response.setData(objeto);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@SuppressWarnings("unchecked")
	default ResponseEntity<Response<String>> interfaceDeleteById(String id, Object service) {
		Response<String> response = new Response<>();
		try {
			T objeto = (T) service.getClass().getMethod("findById", String.class).invoke(service.getClass(), id);
			if (objeto == null) {
				response.getErros().add("ID não registrado == " + id);
				return ResponseEntity.badRequest().body(response);
			}
			service.getClass().getMethod("delete", String.class).invoke(service.getClass(), id);
			return ResponseEntity.ok(new Response<>());
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}
