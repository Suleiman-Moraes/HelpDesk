package br.com.senaigo.helpdesk.api.util;

import org.springframework.http.ResponseEntity;

import br.com.senaigo.helpdesk.api.response.Response;

public class ICRUDSimples {
	/**
	 * 
	 * @param <T>
	 * @param id
	 * @param service
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<Response<T>> interfaceFindById(String id, Object service) {
		Response<T> response = new Response<>();
		try {
			T objeto = (T) service.getClass().getMethod("findById", String.class).invoke(service, id);
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
	public static <T> ResponseEntity<Response<String>> interfaceDeleteById(String id, Object service) {
		Response<String> response = new Response<>();
		try {
			T objeto = (T) service.getClass().getMethod("findById", String.class).invoke(service, id);
			if (objeto == null) {
				response.getErros().add("ID não registrado == " + id);
				return ResponseEntity.badRequest().body(response);
			}
			service.getClass().getMethod("delete", String.class).invoke(service, id);
			return ResponseEntity.ok(new Response<>());
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}
