package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MENSAGEM")
@SequenceGenerator(name="SEQ_MENSAGEM", sequenceName="SEQ_MENSAGEM", allocationSize=1)
public class Mensagem extends AuditEntity {

	private static final long serialVersionUID = -8964587086702416841L;
	private Diretoria diretoria;
	private Gerencia gerencia;
	private Date vigenciaInicio;
	private Date vigenciaFim;
	private TipoMensagem tipoMensagem;
	private Boolean assinada;
	private String textoMensagem;
	private String nome;
	private List<MensagemUsuario> listaMensagensUsuario;
	private List<MensagemDiretoria> listaMensagensDiretoria;
	private List<MensagemGerencia> listaMensagemGerencia;
	private boolean ciente;
	private List<MensagemPerfil> listaMensagemPerfil;

	public Mensagem() {

	}

	public Mensagem(Long codigo) {
		setCodigo(codigo);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DIRETORIA_ASSINATURA")
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_GERENCIA_ASSINATURA")
	public Gerencia getGerencia() {
		return gerencia;
	}

	public void setGerencia(Gerencia gerencia) {
		this.gerencia = gerencia;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VIGENCIA_INICIO")
	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VIGENCIA_FIM")
	public Date getVigenciaFim() {
		return vigenciaFim;
	}

	public void setVigenciaFim(Date vigenciaFim) {
		this.vigenciaFim = vigenciaFim;
	}

	@Column(name = "TIPO")
	@NotNull
	public TipoMensagem getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(TipoMensagem tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	@Column(name = "ASSINADA")
	public Boolean getAssinada() {
		return assinada;
	}

	public void setAssinada(Boolean assinada) {
		this.assinada = assinada;
	}

	@Column(name = "mensagem", nullable = false)
	public String getTextoMensagem() {
		return textoMensagem;
	}

	public void setTextoMensagem(String textoMensagem) {
		this.textoMensagem = textoMensagem;
	}

	@Column(name = "nome", nullable = false, length = 15)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mensagem")
	public List<MensagemUsuario> getListaMensagensUsuario() {
		return listaMensagensUsuario;
	}

	public void setListaMensagensUsuario(List<MensagemUsuario> listaMensagensUsuario) {
		this.listaMensagensUsuario = listaMensagensUsuario;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mensagem")
	public List<MensagemGerencia> getListaMensagemGerencia() {
		return listaMensagemGerencia;
	}

	public void setListaMensagemGerencia(List<MensagemGerencia> listaMensagemGerencia) {
		this.listaMensagemGerencia = listaMensagemGerencia;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mensagem")
	public List<MensagemDiretoria> getListaMensagensDiretoria() {
		return listaMensagensDiretoria;
	}

	public void setListaMensagensDiretoria(List<MensagemDiretoria> listaMensagensDiretoria) {
		this.listaMensagensDiretoria = listaMensagensDiretoria;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mensagem")
	public List<MensagemPerfil> getListaMensagemPerfil() {
		return listaMensagemPerfil;
	}

	public void setListaMensagemPerfil(List<MensagemPerfil> listaMensagemPerfil) {
		this.listaMensagemPerfil = listaMensagemPerfil;
	}

	@Transient
	public String getNumero() {
		if (getCodigo() != null) {
			return "ME/" + getCodigo();
		}
		return "";
	}

	@Transient
	public String getGerenciaDiretoriaResponsavel() {
		if (tipoMensagem != null) {
			if (tipoMensagem.equals(TipoMensagem.DIRETORIA) && this.diretoria != null && this.diretoria.getCodigo() != null) {
				return this.diretoria.getNome().toUpperCase();
			}
			if (tipoMensagem.equals(TipoMensagem.GERENCIA) && this.gerencia != null && this.gerencia.getCodigo() != null) {
				return this.gerencia.getNome().toUpperCase();
			}
		}
		return "";
	}

	@Transient
	public String getPublico() {
		if (tipoMensagem != null) {
			if (tipoMensagem.equals(TipoMensagem.DIRETORIA)) {
				if (listaMensagensDiretoria != null && !listaMensagensDiretoria.isEmpty()) {
					if (listaMensagensDiretoria.size() == 1) {
						return "Diretoria";
					}
					if (listaMensagensDiretoria.size() > 1) {
						return "Várias";
					}
				}
			}

			if (tipoMensagem.equals(TipoMensagem.GERENCIA)) {
				if (listaMensagemGerencia != null && !listaMensagemGerencia.isEmpty()) {
					if (listaMensagemGerencia.size() == 1) {
						return "Gerência";
					}
					if (listaMensagemGerencia.size() > 1) {
						return "Vários";
					}
				}
			}
			if (tipoMensagem.equals(TipoMensagem.PERFIL)) {
				if (listaMensagemPerfil != null && !listaMensagemPerfil.isEmpty()) {
					if (listaMensagemPerfil.size() == 1) {
						return "Perfil";
					}
					if (listaMensagemPerfil.size() > 1) {
						return "Várias";
					}
				}
			}

		}
		return "";
	}

	@Transient
	public boolean hasAceite() {
		if (this.listaMensagensUsuario != null && !listaMensagensUsuario.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transient
	public Integer getTotalAceite() {
		if (listaMensagensUsuario != null && !listaMensagensUsuario.isEmpty()) {
			return listaMensagensUsuario.size();
		}
		return 0;
	}

	@Transient
	public Integer getTotalRealizado() {
		int totalRealizado = 0;
		List<MensagemUsuario> aceitesRealizados = this.getListaAceitesRealizados();
		if (aceitesRealizados != null && !aceitesRealizados.isEmpty()) {
			totalRealizado = aceitesRealizados.size();
		}
		return totalRealizado;
	}

	@Transient
	public BigDecimal getPercentualPendente() {
		BigDecimal totalAceite = new BigDecimal(this.getTotalAceite());
		BigDecimal totalPendente = new BigDecimal(getPendentes());
		BigDecimal percentualPendente = new BigDecimal("0");
		if (!totalAceite.equals(BigDecimal.ZERO) && !totalPendente.equals(BigDecimal.ZERO)) {
			if (totalAceite.equals(totalPendente)) {
				percentualPendente = new BigDecimal("100");
			} else {
				percentualPendente = totalPendente.divide(totalAceite, 2, RoundingMode.HALF_EVEN);
				percentualPendente = percentualPendente.multiply(new BigDecimal("100"));
			}
		}
		return percentualPendente.setScale(2, RoundingMode.UP);
	}

	@Transient
	public BigDecimal getPercentualRealizado() {
		BigDecimal totalAceite = new BigDecimal(this.getTotalAceite()).setScale(2, RoundingMode.UP);
		BigDecimal totalRealizado = new BigDecimal(getTotalRealizado()).setScale(2, RoundingMode.UP);
		BigDecimal percentualRealizado = new BigDecimal("0").setScale(2, RoundingMode.UP);
		if (!totalAceite.equals(BigDecimal.ZERO) && !totalRealizado.equals(BigDecimal.ZERO)) {
			if (totalAceite.equals(totalRealizado)) {
				percentualRealizado = new BigDecimal("100");
			} else {
				percentualRealizado = totalRealizado.divide(totalAceite, 2, RoundingMode.UP);
				percentualRealizado = percentualRealizado.multiply(new BigDecimal("100")).setScale(2, RoundingMode.UP);
			}
		}
		return percentualRealizado.setScale(2, RoundingMode.UP);
	}

	@Transient
	public Integer getPendentes() {
		return getTotalAceite() - getTotalRealizado();
	}

	//	temp
	@Transient
	public List<MensagemUsuario> getListaAceitesRealizados() {
		List<MensagemUsuario> aceitesRealizados = null;
		if (listaMensagensUsuario != null && !listaMensagensUsuario.isEmpty()) {
			aceitesRealizados = new ArrayList<>();
			aceitesRealizados.addAll(listaMensagensUsuario);
		}
		return aceitesRealizados;
	}

	@Transient
	public List<MensagemUsuario> getListaAceitesNaoRealizados() {
		List<MensagemUsuario> aceitesRealizados = new ArrayList<>();
		if (listaMensagensUsuario != null && !listaMensagensUsuario.isEmpty()) {
			for (MensagemUsuario mensagemUsuario : listaMensagensUsuario) {
				if (mensagemUsuario.getDataAceite() == null) {
					aceitesRealizados.add(mensagemUsuario);
				}
			}
		}
		return aceitesRealizados;
	}

	@Transient
	public boolean getCiente() {
		return ciente;
	}

	public void setCiente(boolean ciente) {
		this.ciente = ciente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(Mensagem.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Mensagem other = (Mensagem) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mensagem [tipoMensagem=" + tipoMensagem + ", textoMensagem=" + textoMensagem + ", ciente=" + ciente + "]";
	}

	public enum TipoMensagem {
		DIRETORIA("Diretoria"), GERENCIA("Gerência"), PERFIL("Perfil");

		private final String nome;

		TipoMensagem(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

}
