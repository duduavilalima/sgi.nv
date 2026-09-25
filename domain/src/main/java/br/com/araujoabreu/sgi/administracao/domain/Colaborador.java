package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.fundoFixo.domain.FundoFixo;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COLABORADOR")
public class Colaborador extends PessoaFisica {

	private static final long serialVersionUID = 89420488456932759L;
	private CentroFinanceiro centroFinanceiro;
	private List<FundoFixo> fundosFixos;
	private List<ColaboradorCentroFinanceiro> centrosFinanceiros = new ArrayList<>();

	public Colaborador() {

	}

	public Colaborador(Long codigo) {
		setCodigo(codigo);
	}

	@Transient
	public Gerencia getGerencia() {
		if (centroFinanceiro instanceof Gerencia) {
			return ((Gerencia) centroFinanceiro);
		} else if (centroFinanceiro instanceof CentroFinanceiro && centroFinanceiro.getPai() != null) {
			return ((Gerencia) centroFinanceiro.getPai());
		} else {
			return (Gerencia) centroFinanceiro;
		}
	}

	@Transient
	public Unidade getUnidade() {
		Gerencia gerencia = getGerencia();
		if (gerencia != null) {
			return gerencia.getUnidade();
		}
		return null;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "colaborador")
	public List<FundoFixo> getFundosFixos() {
		return fundosFixos;
	}

	public void setFundosFixos(List<FundoFixo> fundosFixos) {
		this.fundosFixos = fundosFixos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "colaborador")
	public List<ColaboradorCentroFinanceiro> getCentrosFinanceiros() {
		return centrosFinanceiros;
	}

	public void setCentrosFinanceiros(
		List<ColaboradorCentroFinanceiro> centrosFinanceiros) {
		this.centrosFinanceiros = centrosFinanceiros;
	}

	@Transient
	public List<MensagemUsuario> getListaMensagens() {
		if (this.getUsuario() == null)
			return null;

		return this.getUsuario().getListaMensagens();
	}

}
