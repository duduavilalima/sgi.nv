package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "REINF_SERVICO")
@SequenceGenerator(name="SEQ_REINF_SERVICO", sequenceName="SEQ_REINF_SERVICO", allocationSize=1)
public class ReinfServico extends BaseEntity {

	private static final long serialVersionUID = -2269279994085628885L;
	private String codigoClassificacao;
	private String tipoServico;

	@Column(name = "codigo_classificacao", nullable = false, length = 9)
	public String getCodigoClassificacao() {
		return codigoClassificacao;
	}

	public void setCodigoClassificacao(String codigoClassificacao) {
		this.codigoClassificacao = codigoClassificacao;
	}

	@Column(name = "tipo_servico", nullable = false, length = 100)
	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ReinfServico)) return false;
		if (!super.equals(o)) return false;
		ReinfServico that = (ReinfServico) o;
		return Objects.equals(getCodigoClassificacao(), that.getCodigoClassificacao()) &&
			Objects.equals(getTipoServico(), that.getTipoServico());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigoClassificacao(), getTipoServico());
	}

	@Override
	public String toString() {
		return "ReinfServico{" +
			"codigoClassificacao='" + codigoClassificacao + '\'' +
			", tipoServico='" + tipoServico + '\'' +
			'}';
	}
}
