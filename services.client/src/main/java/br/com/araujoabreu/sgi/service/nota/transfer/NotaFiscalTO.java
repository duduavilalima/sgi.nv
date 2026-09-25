package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.compras.transfer.DocumentoFiscalTO;

import java.math.BigDecimal;
import java.util.Date;

public class NotaFiscalTO extends NotaTO {

	private Date dataEmissao;
	private String serie;
	private String subserie;
	private Date limiteEmissao;
	private Date saida;
	private Date conclusao;
	private String chaveEletronica;
	private BigDecimal desconto;
	private BigDecimal valorFrete;
	private BigDecimal valorSeguro;
	private BigDecimal valorIpi;
	private BigDecimal valorOutrasDespesas;
	private String tipoFrete;
	private String tipoSeguro;
	private DocumentoFiscalTO documentoFiscal;
	private String natureza;
	private NotaFiscalTO notaFiscalTransporte;
	private String naturezaAquisicao;
	private String tipoFornecimento;
	private String tipoNotaFiscal;
	private String tipoCobranca;

	private Long codigo_cfop;
	private String horaSaida;

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getSubserie() {
		return subserie;
	}

	public void setSubserie(String subserie) {
		this.subserie = subserie;
	}

	public Date getLimiteEmissao() {
		return limiteEmissao;
	}

	public void setLimiteEmissao(Date limiteEmissao) {
		this.limiteEmissao = limiteEmissao;
	}

	public Date getSaida() {
		return saida;
	}

	public void setSaida(Date saida) {
		this.saida = saida;
	}

	public Date getConclusao() {
		return conclusao;
	}

	public void setConclusao(Date conclusao) {
		this.conclusao = conclusao;
	}

	public String getChaveEletronica() {
		return chaveEletronica;
	}

	public void setChaveEletronica(String chaveEletronica) {
		this.chaveEletronica = chaveEletronica;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public BigDecimal getValorIpi() {
		return valorIpi;
	}

	public void setValorIpi(BigDecimal valorIpi) {
		this.valorIpi = valorIpi;
	}

	public BigDecimal getValorOutrasDespesas() {
		return valorOutrasDespesas;
	}

	public void setValorOutrasDespesas(BigDecimal valorOutrasDespesas) {
		this.valorOutrasDespesas = valorOutrasDespesas;
	}

	public String getTipoFrete() {
		return tipoFrete;
	}

	public void setTipoFrete(String tipoFrete) {
		this.tipoFrete = tipoFrete;
	}

	public String getTipoSeguro() {
		return tipoSeguro;
	}

	public void setTipoSeguro(String tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}

	public DocumentoFiscalTO getDocumentoFiscal() {
		return documentoFiscal;
	}

	public void setDocumentoFiscal(DocumentoFiscalTO documentoFiscal) {
		this.documentoFiscal = documentoFiscal;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public NotaFiscalTO getNotaFiscalTransporte() {
		return notaFiscalTransporte;
	}

	public void setNotaFiscalTransporte(NotaFiscalTO notaFiscalTransporte) {
		this.notaFiscalTransporte = notaFiscalTransporte;
	}

	public String getNaturezaAquisicao() {
		return naturezaAquisicao;
	}

	public void setNaturezaAquisicao(String naturezaAquisicao) {
		this.naturezaAquisicao = naturezaAquisicao;
	}

	public String getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(String tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	public String getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	public void setTipoNotaFiscal(String tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	public String getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(String tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public Long getCodigo_cfop() {
		return codigo_cfop;
	}

	public void setCodigo_cfop(Long codigo_cfop) {
		this.codigo_cfop = codigo_cfop;
	}

	public String getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}
}
