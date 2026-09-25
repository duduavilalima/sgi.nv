package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "AVALIACAO")
@SequenceGenerator(name = "SEQ_AVALIACAO", sequenceName = "SEQ_AVALIACAO", allocationSize = 1)
public class Avaliacao extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -2794076921654343941L;

	private StatusAvaliacao status;
	private TipoAvaliacao tipoAvaliacao;
	private Date dataAvaliacao;
	private String avaliador;
	private List<ItemAvaliacao> itens = new ArrayList<>();
	private NotaFiscal notaFiscal;

	public Avaliacao() {
		super();
	}

	public Avaliacao(Long codigo) {
		setCodigo(codigo);
	}

	public Avaliacao(StatusAvaliacao status) {
		this.status = status;
	}

	public Avaliacao(Long codigo, StatusAvaliacao status, TipoAvaliacao tipoAvaliacao, Date dataAvaliacao, String avaliador, List<ItemAvaliacao> itens, NotaFiscal notaFiscal) {
		setCodigo(codigo);
		this.status = status;
		this.tipoAvaliacao = tipoAvaliacao;
		this.dataAvaliacao = dataAvaliacao;
		this.avaliador = avaliador;
		this.itens = itens;
		this.notaFiscal = notaFiscal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "avaliacao")
	public List<ItemAvaliacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemAvaliacao> itens) {
		this.itens = itens;
	}

	@Column(name = "STATUS", nullable = false)
	@NotNull
	public StatusAvaliacao getStatus() {
		return status;
	}

	public void setStatus(StatusAvaliacao situacao) {
		this.status = situacao;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoAvaliacao getTipoAvaliacao() {
		return tipoAvaliacao;
	}

	public void setTipoAvaliacao(TipoAvaliacao tipoAvaliacao) {
		this.tipoAvaliacao = tipoAvaliacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	@NotNull
	public Date getDataAvaliacao() {
		return dataAvaliacao;
	}

	public void setDataAvaliacao(Date dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}

	@Column(name = "AVALIADOR", nullable = false)
	@NotNull
	public String getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(String avaliador) {
		this.avaliador = avaliador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA")
	@NotNull
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof HibernateProxy)
			o = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
		if (!(o instanceof Avaliacao)) return false;
		if (!super.equals(o)) return false;
		Avaliacao avaliacao = (Avaliacao) o;
		return Objects.equals(getCodigo(), avaliacao.getCodigo()) &&
			getStatus() == avaliacao.getStatus() &&
			getTipoAvaliacao() == avaliacao.getTipoAvaliacao() &&
			Objects.equals(getDataAvaliacao(), avaliacao.getDataAvaliacao()) &&
			Objects.equals(getAvaliador(), avaliacao.getAvaliador());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigo(), getStatus(), getTipoAvaliacao(), getDataAvaliacao(), getAvaliador());
	}

	@Override
	public String toString() {
		return "Avaliacao [codigo=" + getCodigo() + ", status=" + status + "]";
	}

	@Override
	public Long getId() {
		return getCodigo();
	}

	@Override
	public void setId(Long id) {
		setCodigo(id);
	}

	public enum StatusAvaliacao {
		NAO_INICIADA("Não iniciada", "Avaliação do fornecedor não iniciada"),
		INICIADA("Iniciada", "Avaliação do fornecedor iniciada, mas não concluída (pode não ser possível o envio da NF para aprovação)"),
		CONCLUIDA("Concluída", "Avaliação do fornecedor concluída");

		private final String nome;
		private final String descricao;

		StatusAvaliacao(String nome, String descricao) {
			this.nome = nome;
			this.descricao = descricao;
		}

		public String getNome() {
			return nome;
		}

		public String getDescricao() {
			return this.descricao;
		}

	}

	public enum TipoAvaliacao {
		NOTA_FISCAL("Nota Fiscal");

		private final String nome;

		TipoAvaliacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

	}

}
