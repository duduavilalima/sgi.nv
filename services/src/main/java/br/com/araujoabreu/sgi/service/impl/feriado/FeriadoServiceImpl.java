package br.com.araujoabreu.sgi.service.impl.feriado;

import br.com.araujoabreu.sgi.administracao.domain.Feriado;
import br.com.araujoabreu.sgi.administracao.domain.QFeriado;
import br.com.araujoabreu.sgi.service.feriado.FeriadoService;
import br.com.araujoabreu.sgi.service.feriado.transfer.FeriadoPesquisaTO;
import br.com.araujoabreu.sgi.service.feriado.transfer.FeriadoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.DatasParcelaTO;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.feriado.FeriadoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FeriadoServiceImpl extends BaseSearchService<Feriado, FeriadoTO, FeriadoPesquisaTO, FeriadoRepository> implements FeriadoService {
	private static final QFeriado qFeriado = QFeriado.feriado;

	@Autowired
	public FeriadoServiceImpl(FeriadoRepository repository) {
		super(repository);
	}

	@Override
	public List<FeriadoTO> buscarTodos() {

		QBean qBean = Projections.bean(
			Feriado.class,
			qFeriado.codigo,
			qFeriado.dia,
			qFeriado.mes,
			qFeriado.ano,
			qFeriado.descricao
		);

		JPAQuery<Feriado> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qFeriado);

		List<Feriado> lista = jpaQuery.fetch();

		List<FeriadoTO> listaTO = new ArrayList<>();

		for (Feriado feriado : lista) {
			FeriadoTO to = getMapper().map(feriado, FeriadoTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}

	@Override
	public boolean isFeriado(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONDAY) + 1;
		int year = calendar.get(Calendar.YEAR);

		long feriadoEspecifico = select().where(QFeriado.feriado.ano.eq(year).and(QFeriado.feriado.mes.eq(month).and(QFeriado.feriado.dia.eq(day)))).fetchCount();
		long feriado = select().where(QFeriado.feriado.ano.isNull().and(QFeriado.feriado.mes.eq(month).and(QFeriado.feriado.dia.eq(day)))).fetchCount();
		return feriadoEspecifico > 0 ? Boolean.TRUE : feriado > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public Boolean verificarFeriado(Calendar feriado) {
		int dia = feriado.get(Calendar.DAY_OF_MONTH);
		int mes = feriado.get(Calendar.MONTH) + 1; // a classe Calendar começar a contar os meses com índice a partir do 0
		int ano = feriado.get(Calendar.YEAR);

		List<Feriado> listaFeriado = getRepository().findFeriadosByData(dia, mes, ano);

		if (listaFeriado == null || listaFeriado.isEmpty()) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	@Override
	public Date devolveDataUtil(Date data) {
		return calculaDataUtilIgualOuPosterior(data);
	}

	@Override
	public Date devolveDataUtil(Date data, int somaDia) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, somaDia);
		return calculaDataUtilIgualOuPosterior(cal.getTime());
	}

	/**
	 * Devolve a data util igual ou posterior a data do parametro.
	 */
	private Date calculaDataUtilIgualOuPosterior(Date data) {
		Date dataUtil = null;

		if (data != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(data);

			boolean definiuData = false;

			while (!definiuData) {
				boolean isFeriado = verificarFeriado(cal);

				if (isFeriado || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DAY_OF_MONTH, 1);
				} else {
					definiuData = true;
				}
			}

			dataUtil = cal.getTime();
		}

		return dataUtil;
	}

	@Override
	public DatasParcelaTO getDataPagamentoDataAgendamento(String dataVencimentoString) {

		Date dataVencimento = GlobalBean.converterStringParaData(dataVencimentoString);
		DatasParcelaTO datasParcelaTO = new DatasParcelaTO();
		datasParcelaTO.setDataVencimento(dataVencimento);
		datasParcelaTO.setDataPagamento(devolveDataUtil(dataVencimento));
		datasParcelaTO.setDataAgendamento(devolveDataUtil(datasParcelaTO.getDataPagamento(),1));
		return datasParcelaTO;
	}
}
