package br.com.senaigo.helpdesk.api.enumeration;

import lombok.Getter;

@Getter
public enum StatusEnum {
	NOVO,
	DESIGNADO,
	RESOLVIDO,
	APROVADO,
	DESAPROVADO,
	FECHADO;
	
	public static StatusEnum getStatus(String status) {
		StatusEnum retorno = StatusEnum.valueOf(status != null ? status.trim().toUpperCase() : "NOVO");
		return retorno != null ? retorno : StatusEnum.NOVO;
	}
}
