package br.com.senaigo.helpdesk.api.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Summary implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer amountNovo;
	private Integer amountResolvido;
	private Integer amountAprovado;
	private Integer amountDesaprovado;
	private Integer amountDesignado;
	private Integer amountFechado;
	
	public void setSummary(Integer amountNovo, Integer amountResolvido, Integer amountAprovado, Integer amountDesaprovado,
			Integer amountDesignado, Integer amountFechado) {
		this.amountNovo = amountNovo;
		this.amountResolvido = amountResolvido;
		this.amountAprovado = amountAprovado;
		this.amountDesaprovado = amountDesaprovado;
		this.amountDesignado = amountDesignado;
		this.amountFechado = amountFechado;
	}
}
