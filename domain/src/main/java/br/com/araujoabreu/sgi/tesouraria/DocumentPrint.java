package br.com.araujoabreu.sgi.tesouraria;

import br.com.fasolti.sgi.repository.tesouraria.enumerations.PrintStatus;
import br.com.fasolti.sgi.repository.tesouraria.enumerations.TiposImpressao;

import java.util.Date;

public interface DocumentPrint {

	Long getCodigo();

	String getTextoImpressao();

	PrintStatus getStatus();

	TiposImpressao getTipoImpressao();

	Date getDataCriacao();

	Date getDataImpressao();

	String getCriador();

}
