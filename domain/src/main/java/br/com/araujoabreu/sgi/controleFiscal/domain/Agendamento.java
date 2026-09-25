package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Representa um Agendamento, que pode ser utilizado em todo o sistema.
 * Por exemplo, Agendamento para pagamentos de Tributos.
 *
 * @author Rodrigo Barreiros
 */
@Entity
@Table(name = "AGENDAMENTO")
@SequenceGenerator(name="SEQ_AGENDAMENTO", sequenceName="SEQ_AGENDAMENTO", allocationSize=1)
public class Agendamento extends AuditEntity {

	/**
	 * Significado de cada item na expressão: Seconds, Minutes, Hours, Day-of-month, Month, Day-of-Week, Year
	 */
	private static final String CRON_EXPRESSION_PATTERN = "0 0 0 {0} {1} {2} {3}";
	private static final long serialVersionUID = -2260464849110242545L;
	/**
	 * Propriedade Persistêntes do Agendamento
	 */
	private Frequencia frequencia = Frequencia.DIARIO;
	private Date dataInicio;

	/**
	 * Retorna o Agendamento em linguagem natural. É a represetação do Agendamento em
	 * um texto de fácil compreensão.
	 *
	 * @return a descrição do agendamento
	 */
	@Transient
	public String getDescricao() {
		StringBuilder description = new StringBuilder();
		if (frequencia.equals(Frequencia.ANUAL)) {
			description.append("Anualmente");
			if (dataInicio != null) {
				description.append(" no dia ").append(getDay()).append(" de ").append(getMonth());
			}
		}
		if (frequencia.equals(Frequencia.TRIMESTRAL)) {
			description.append("Trimestralmente");
			if (dataInicio != null) {
				description.append(" no dia ").append(getDay());
			}
		}
		if (frequencia.equals(Frequencia.MENSAL)) {
			description.append("Mensalmenete");
			if (dataInicio != null) {
				description.append(" no dia ").append(getDay());
			}
		}
		if (frequencia.equals(Frequencia.SEMANAL)) {
			description.append("Semanalmente na segunda");
		}
		if (frequencia.equals(Frequencia.DIARIO)) {
			description.append("Diariamente (Segunda a Sexta)");
		}
		return description.toString();
	}

	/**
	 * Retorna o Agendamento representado por uma Expressão Cron. Útil caso se queria
	 * usar Mecanismos de Agendeameto baseados em Cron Expressions.
	 *
	 * @return Cron Expression do Agendamento
	 */
	@Transient
	public String getCronExpression() {
		Object[] args = null;
		if (frequencia.equals(Frequencia.ANUAL)) {
			args = new Object[]{getDay() + "W", getMonth(), "?", "*"};
		}
		if (frequencia.equals(Frequencia.TRIMESTRAL)) {
			args = new Object[]{getDay() + "W", "1/3", "?", "*"};
		}
		if (frequencia.equals(Frequencia.MENSAL)) {
			args = new Object[]{getDay() + "W", "*", "?", "*"};
		}
		if (frequencia.equals(Frequencia.SEMANAL)) {
			args = new Object[]{"?", "*", "MON", "*"};
		}
		if (frequencia.equals(Frequencia.DIARIO)) {
			//args = new Object[] {"*", "*", "MON-FRI", "*"};
			args = new Object[]{"?", "*", "MON-FRI", "*"};
		}

		return MessageFormat.format(CRON_EXPRESSION_PATTERN, args);
	}

	/**
	 * Retorna o Mês de Início do Agendamento em linguagem natural (Janeiro, Fevereiro e etc)
	 *
	 * @return mês de íncio do agendamento
	 */
	@Transient
	private String getMonth() {
		return new SimpleDateFormat("MMMMM").format(dataInicio);
	}


	//    @Transient
//    public Date getNextTime() {
//        try {
//            // Recuperando o dia anterior... Necessário para q o dia atual seja considerado uma data válida.
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_MONTH, -1);
//            // Recuperando a próxima data válida...
//            return new CronExpression(getCronExpression()).getNextValidTimeAfter(calendar.getTime());
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }

	/**
	 * Retorna o Dia de Início do Agendamento (Dia do Mês).
	 *
	 * @return o dia de início do agendamento
	 */
	@Transient
	private String getDay() {
		return new SimpleDateFormat("dd").format(dataInicio);
	}

	/**
	 * Retornas todas as possíveis frequências de agendamento.
	 *
	 * @return o array de frequências
	 */
	@Transient
	public Frequencia[] getFrequencias() {
		return Frequencia.values();
	}

	/**
	 * Retorna a Frequência de Agendamento.
	 *
	 * @return a frequência de agendamento.
	 */
	@Column(name = "FREQUENCIA")
	@NotNull
	public Frequencia getFrequencia() {
		return frequencia;
	}

	/**
	 * Seta a Frequência de Agendamento.
	 *
	 * @param frequencia a frequência de agendamento.
	 */
	public void setFrequencia(Frequencia frequencia) {
		this.frequencia = frequencia;
	}

	/**
	 * Retorna a Data de Início do Agendamento.
	 *
	 * @return a data de início do agendamento.
	 */
	@Column(name = "DATA_INICIO")
	@Temporal(TemporalType.DATE)
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * Seta a Data de Início do Agendamento.
	 *
	 * @param dataInicio a data de início do agendamento.
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(Agendamento.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Agendamento other = (Agendamento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	/**
	 * Todos as possíveis frequências disponíveis
	 */
	public enum Frequencia {DIARIO, SEMANAL, MENSAL, TRIMESTRAL, ANUAL}

}
