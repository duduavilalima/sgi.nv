package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import br.com.fasolti.corporativo.security.domain.Perfil;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "APLICACAO_MOTIVO_RETORNO")
@SequenceGenerator(name="SEQ_APLICACAO_MOTIVO_RETORNO", sequenceName="SEQ_APLICACAO_MOTIVO_RETORNO", allocationSize=1)
public class MotivoAplicacao extends AuditEntity implements DomainObject, Dominio {

	private static final long serialVersionUID = 4165441071039134333L;
	private String nome;
	private String chave;
	private TipoMotivo tipoMotivo;
	private Perfil perfil;
	private List<MotivoRetorno> listaMotivosRetorno = new ArrayList<>();

	public MotivoAplicacao() {
		super();
	}

	public MotivoAplicacao(Long codigo) {
		super();
		setCodigo(codigo);
	}

	public MotivoAplicacao(String nome, String chave, TipoMotivo tipoMotivo) {
		super();
		this.nome = nome;
		this.chave = chave;
		this.tipoMotivo = tipoMotivo;
	}

	public MotivoAplicacao(String nome, String chave, TipoMotivo tipoMotivo, List<MotivoRetorno> listaMotivosRetorno) {
		super();
		this.nome = nome;
		this.chave = chave;
		this.tipoMotivo = tipoMotivo;
		this.listaMotivosRetorno = listaMotivosRetorno;
	}

	public MotivoAplicacao(String nome, String chave, TipoMotivo tipoMotivo, Perfil perfil, List<MotivoRetorno> listaMotivosRetorno) {
		super();
		this.nome = nome;
		this.chave = chave;
		this.tipoMotivo = tipoMotivo;
		this.perfil = perfil;
		this.listaMotivosRetorno = listaMotivosRetorno;
	}

	@Override
	@Column(name = "NOME", length = 50)
	@Length(max = 50)
	@NotNull
	public String getNome() {
		return nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "CHAVE")
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	@Column(name = "TIPO")
	@NotNull
	public TipoMotivo getTipoMotivo() {
		return tipoMotivo;
	}

	public void setTipoMotivo(TipoMotivo tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PERFIL")
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "motivoAplicacao")
	@OrderBy(value = "motivo")
	public List<MotivoRetorno> getListaMotivosRetorno() {
		return listaMotivosRetorno;
	}

	public void setListaMotivosRetorno(List<MotivoRetorno> listaMotivosRetorno) {
		this.listaMotivosRetorno = listaMotivosRetorno;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MotivoAplicacao)) return false;
		if (!super.equals(o)) return false;
		MotivoAplicacao that = (MotivoAplicacao) o;
		return Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getChave(), that.getChave()) &&
			getTipoMotivo() == that.getTipoMotivo();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getChave(), getTipoMotivo());
	}

	@Override
	public String toString() {
		return "MotivoAplicacao{" +
			"nome='" + nome + '\'' +
			", chave='" + chave + '\'' +
			", tipoMotivo=" + tipoMotivo +
			'}';
	}

	public enum TipoAplicacao {
		NOTA_FISCAL("nota-fiscal"),
		FUNDO_FIXO("fundo-fixo"),
		CONTAS_PAGAR("selecao-cap"),
		AUTORIZAR_REQUISICAO("auto-requi"),
		ORDEM_COMPRA("ordem-compra"),
		AUTORIZAR_COMPRA("auto-compra"),
		APROVAR_NOTA("nota-fiscal-aprovacao"),
		FUNDO_FIXO_HOMOLOGACAO("fundo-fixo-homologacao"),
		MOVIMENTO_NOTA_ANALISE("cf-movimento"),
		RECEBER_COMPRA("receber-compra"),
		AUTORIZAR_PAGAMENTO("pagamento-autorizar"),
		CANCELAR_PAGAMENTO("pagamento-cancelar"),
		CENTRO_FINANCEIRO_ASSINATURA("cf-retornar");

		private final String constante;

		private TipoAplicacao(String constante) {
			this.constante = constante;
		}

		public String getConstante() {
			return constante;
		}

		@Override
		public String toString() {
			return constante;
		}
	}

	public enum TipoMotivo {
		RETORNO("Retorno"),
		CANCELAMENTO("Cancelamento");

		private final String constante;

		private TipoMotivo(String constante) {
			this.constante = constante;
		}

		public String getConstante() {
			return constante;
		}

		@Override
		public String toString() {
			return constante;
		}
	}
}



