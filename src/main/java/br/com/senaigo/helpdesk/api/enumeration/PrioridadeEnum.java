package br.com.senaigo.helpdesk.api.enumeration;

public enum PrioridadeEnum {
	ALTA,
	NORMAL,
	BAIXA;
	
	public static PrioridadeEnum getStatus(String prioridade) {
		PrioridadeEnum retorno = PrioridadeEnum.valueOf(prioridade != null ? prioridade.trim().toUpperCase() : "BAIXA");
		return retorno != null ? retorno : PrioridadeEnum.NORMAL;
	}
}
