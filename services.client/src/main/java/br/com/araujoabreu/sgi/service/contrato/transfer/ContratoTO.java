package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.FormaContratacaoTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ContratoTO extends BaseTO {
	private String numero;
	private String tipo;
	private String situacao;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private Integer prazoAtual;
	private Double valorMensal;
	private String periodoReajuste;
	private String resumo;
	private String resumoFaturamento;
	private String contratoOriginal;
	private BigDecimal valorTotal = new BigDecimal("0");
	private PessoaTO clienteTO;
	private BigDecimal percentualRetencao;
	private List<ContratoContatoTO> contratoContatos;
	private String estagioComercial;
	private DiretoriaSimpleTO diretoria;
	private PessoaTO cliente;
	private Date dataAssinatura;
	private String proposta;
	private UnidadeTO unidade;
	private FormaContratacaoTO formaContratacao;
	private String indiceReajuste;
	private String prorrogacao;
	private Date dataPrimeiraParcela;
	private List<CentroFinanceiroTO> listaCentroFinanceiro;
	private List<ContratoEnderecoTO> listaContratoEndereco;
	private List<AditivoTO> listaAditivo;
	private List<ContratoAnexoTO> listaAnexos;
	private List<MotivoRetornoDocumentoContratoTO> listaMotivoRetornoDocContrato;
	private Boolean favorecido;


	/* Aba Controles*/
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String envioAnalise;
	private Date dataEnvioAnalise;
	private String ativacao;
	private Date dataAtivacao;
	private String encerramento;
	private Date dataEncerramento;
	private String paralizacao;
	private Date dataParalizacao;
	private String reativacao;
	private Date dataReativacao;
	private String cancelamento;
	private Date dataCancelamento;

	public ContratoTO() {
	}

	public ContratoTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	public Integer getPrazoAtual() {
		return prazoAtual;
	}

	public void setPrazoAtual(Integer prazoAtual) {
		this.prazoAtual = prazoAtual;
	}

	public Double getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(Double valorMensal) {
		this.valorMensal = valorMensal;
	}

	public String getPeriodoReajuste() {
		return periodoReajuste;
	}

	public void setPeriodoReajuste(String periodoReajuste) {
		this.periodoReajuste = periodoReajuste;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getResumoFaturamento() {
		return resumoFaturamento;
	}

	public void setResumoFaturamento(String resumoFaturamento) {
		this.resumoFaturamento = resumoFaturamento;
	}

	public String getContratoOriginal() {
		return contratoOriginal;
	}

	public void setContratoOriginal(String contratoOriginal) {
		this.contratoOriginal = contratoOriginal;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public PessoaTO getClienteTO() {
		return clienteTO;
	}

	public void setClienteTO(PessoaTO clienteTO) {
		this.clienteTO = clienteTO;
	}

	public BigDecimal getPercentualRetencao() {
		return percentualRetencao;
	}

	public void setPercentualRetencao(BigDecimal percentualRetencao) {
		this.percentualRetencao = percentualRetencao;
	}

	public List<ContratoContatoTO> getContratoContatos() {
		return contratoContatos;
	}

	public void setContratoContatos(List<ContratoContatoTO> contratoContatos) {
		this.contratoContatos = contratoContatos;
	}

	public String getEstagioComercial() {
		return estagioComercial;
	}

	public void setEstagioComercial(String estagioComercial) {
		this.estagioComercial = estagioComercial;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public PessoaTO getCliente() {
		return cliente;
	}

	public void setCliente(PessoaTO cliente) {
		this.cliente = cliente;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public String getProposta() {
		return proposta;
	}

	public void setProposta(String proposta) {
		this.proposta = proposta;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public FormaContratacaoTO getFormaContratacao() {
		return formaContratacao;
	}

	public void setFormaContratacao(FormaContratacaoTO formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public String getIndiceReajuste() {
		return indiceReajuste;
	}

	public void setIndiceReajuste(String indiceReajuste) {
		this.indiceReajuste = indiceReajuste;
	}

	public String getProrrogacao() {
		return prorrogacao;
	}

	public void setProrrogacao(String prorrogacao) {
		this.prorrogacao = prorrogacao;
	}

	public Date getDataPrimeiraParcela() {
		return dataPrimeiraParcela;
	}

	public void setDataPrimeiraParcela(Date dataPrimeiraParcela) {
		this.dataPrimeiraParcela = dataPrimeiraParcela;
	}

	public List<CentroFinanceiroTO> getListaCentroFinanceiro() {
		return listaCentroFinanceiro;
	}

	public void setListaCentroFinanceiro(List<CentroFinanceiroTO> listaCentroFinanceiro) {
		this.listaCentroFinanceiro = listaCentroFinanceiro;
	}

	public List<ContratoEnderecoTO> getListaContratoEndereco() {
		return listaContratoEndereco;
	}

	public void setListaContratoEndereco(List<ContratoEnderecoTO> listaContratoEndereco) {
		this.listaContratoEndereco = listaContratoEndereco;
	}

	public List<AditivoTO> getListaAditivo() {
		return listaAditivo;
	}

	public void setListaAditivo(List<AditivoTO> listaAditivo) {
		this.listaAditivo = listaAditivo;
	}

	public List<ContratoAnexoTO> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<ContratoAnexoTO> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

	public List<MotivoRetornoDocumentoContratoTO> getListaMotivoRetornoDocContrato() {
		return listaMotivoRetornoDocContrato;
	}

	public void setListaMotivoRetornoDocContrato(List<MotivoRetornoDocumentoContratoTO> listaMotivoRetornoDocContrato) {
		this.listaMotivoRetornoDocContrato = listaMotivoRetornoDocContrato;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getEnvioAnalise() {
		return envioAnalise;
	}

	public void setEnvioAnalise(String envioAnalise) {
		this.envioAnalise = envioAnalise;
	}

	public Date getDataEnvioAnalise() {
		return dataEnvioAnalise;
	}

	public void setDataEnvioAnalise(Date dataEnvioAnalise) {
		this.dataEnvioAnalise = dataEnvioAnalise;
	}

	public String getAtivacao() {
		return ativacao;
	}

	public void setAtivacao(String ativacao) {
		this.ativacao = ativacao;
	}

	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public String getEncerramento() {
		return encerramento;
	}

	public void setEncerramento(String encerramento) {
		this.encerramento = encerramento;
	}

	public Date getDataEncerramento() {
		return dataEncerramento;
	}

	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}

	public String getParalizacao() {
		return paralizacao;
	}

	public void setParalizacao(String paralizacao) {
		this.paralizacao = paralizacao;
	}

	public Date getDataParalizacao() {
		return dataParalizacao;
	}

	public void setDataParalizacao(Date dataParalizacao) {
		this.dataParalizacao = dataParalizacao;
	}

	public String getReativacao() {
		return reativacao;
	}

	public void setReativacao(String reativacao) {
		this.reativacao = reativacao;
	}

	public Date getDataReativacao() {
		return dataReativacao;
	}

	public void setDataReativacao(Date dataReativacao) {
		this.dataReativacao = dataReativacao;
	}

	public String getCancelamento() {
		return cancelamento;
	}

	public void setCancelamento(String cancelamento) {
		this.cancelamento = cancelamento;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public Boolean getFavorecido() {
		return favorecido;
	}

	public void setFavorecido(Boolean favorecido) {
		this.favorecido = favorecido;
	}
}


