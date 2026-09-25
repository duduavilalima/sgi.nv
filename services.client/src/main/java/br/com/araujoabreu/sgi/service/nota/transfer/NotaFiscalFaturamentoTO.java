package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class NotaFiscalFaturamentoTO extends NotaSaidaTO {

	private Date vencimento;
	//    private FaturamentoEtapaTO faturamentoEtapaTO;
	private List<LoteInterfaceNotaTO> lotes;
	private Boolean editTributos;
	private Boolean interfacePrefeitura;
	private Long codigoFaturamento;
	private Long codigoNotaAssociadaCancelamento;
	private BigDecimal valorLiquido;

	private NotaSaidaTO notaAssociadaCancelamento;
	private PessoaTO respNotaAssociadaCancelamento;
	private Date dataRespNotaAssociadaCancelamento;

	// Atributo usado ao criar uma nova Nota Fiscal atraves do Faturamento. Nenhum dos itens da lista pode ter "nota"
	private boolean validadoCL = false;

	// Atributo usado ao criar uma nova Nota Fiscal atraves do Faturamento. Nenhum dos itens da lista pode ter "nota"
	// para evitar erro de JSON de recursividade infinita. ex.: nota.listaNotaTributoAliquota[0].nota
	private List<NotaTributoAliquotaTO> listaNotaTributoAliquota;

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

    /*public FaturamentoEtapaTO getFaturamentoEtapaTO() {
        return faturamentoEtapaTO;
    }

    public void setFaturamentoEtapaTO(FaturamentoEtapaTO faturamentoEtapaTO) {
        this.faturamentoEtapaTO = faturamentoEtapaTO;
    }*/

	public List<LoteInterfaceNotaTO> getLotes() {
		return lotes;
	}

	public void setLotes(List<LoteInterfaceNotaTO> lotes) {
		this.lotes = lotes;
	}

	public List<NotaTributoAliquotaTO> getListaNotaTributoAliquota() {
		return listaNotaTributoAliquota;
	}

	public void setListaNotaTributoAliquota(List<NotaTributoAliquotaTO> listaNotaTributoAliquota) {
		this.listaNotaTributoAliquota = listaNotaTributoAliquota;
	}

	public Boolean getEditTributos() {
		return editTributos;
	}

	public void setEditTributos(Boolean editTributos) {
		this.editTributos = editTributos;
	}

	public Boolean getInterfacePrefeitura() {
		return interfacePrefeitura;
	}

	public void setInterfacePrefeitura(Boolean interfacePrefeitura) {
		this.interfacePrefeitura = interfacePrefeitura;
	}

	public Long getCodigoFaturamento() {
		return codigoFaturamento;
	}

	public void setCodigoFaturamento(Long codigoFaturamento) {
		this.codigoFaturamento = codigoFaturamento;
	}

	public Long getCodigoNotaAssociadaCancelamento() {
		return codigoNotaAssociadaCancelamento;
	}

	public void setCodigoNotaAssociadaCancelamento(Long codigoNotaAssociadaCancelamento) {
		this.codigoNotaAssociadaCancelamento = codigoNotaAssociadaCancelamento;
	}

	public NotaSaidaTO getNotaAssociadaCancelamento() {
		return notaAssociadaCancelamento;
	}

	public void setNotaAssociadaCancelamento(NotaSaidaTO notaAssociadaCancelamento) {
		this.notaAssociadaCancelamento = notaAssociadaCancelamento;
	}

	public PessoaTO getRespNotaAssociadaCancelamento() {
		return respNotaAssociadaCancelamento;
	}

	public void setRespNotaAssociadaCancelamento(PessoaTO respNotaAssociadaCancelamento) {
		this.respNotaAssociadaCancelamento = respNotaAssociadaCancelamento;
	}

	public Date getDataRespNotaAssociadaCancelamento() {
		return dataRespNotaAssociadaCancelamento;
	}

	public void setDataRespNotaAssociadaCancelamento(Date dataRespNotaAssociadaCancelamento) {
		this.dataRespNotaAssociadaCancelamento = dataRespNotaAssociadaCancelamento;
	}

	public boolean isValidadoCL() {
		return validadoCL;
	}

	public void setValidadoCL(boolean validadoCL) {
		this.validadoCL = validadoCL;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}
}


