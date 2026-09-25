package br.com.araujoabreu.sgi.service.util;

import java.util.Date;

public interface AuditoriaBasicaTO {

	Long getCodigo();

	String getCriador();

	Date getDataCriacao();

	String getAtualizador();

	Date getDataAtualizacao();
}
