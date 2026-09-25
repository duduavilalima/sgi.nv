package br.com.araujoabreu.sgi.service.reinf.r2010;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR2010 {
	/*
		Os arquivos de documentação para a gerar o Reinf foram baixados originalmente do endereço : http://sped.rfb.gov.br/pasta/show/1495
		-- 16/04/2018 - versão 1.3.02 da documentação
	 */

	/*
	Evento Serviços Tomados - Cessão de Mão de Obra ou Empreitada
	Regras de validação:
	REGRA_EVE_IND_RETIFICACAO
	REGRA_EVE_VALIDO_PREST
	REGRA_EXISTE_INFO_CONTRIBUINTE
	REGRA_VALIDA_ID_EVENTO

	As regras de validação estão no arquivo "Leiautes da EFD-Reinf <Número da Versão> - Anexo II - Tabela de Regras"
	*/
	private EvtServTom evtServTom;

	public EvtServTom getEvtServTom() {
		return evtServTom;
	}

	public void setEvtServTom(EvtServTom evtServTom) {
		this.evtServTom = evtServTom;
	}
}
