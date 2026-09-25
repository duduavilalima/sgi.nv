package br.com.araujoabreu.sgi.service.feriado;

import br.com.araujoabreu.sgi.service.feriado.transfer.FeriadoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.DatasParcelaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface FeriadoService extends CrudService<FeriadoTO> {

	List<FeriadoTO> buscarTodos();

	boolean isFeriado(Date date);

    Boolean verificarFeriado(Calendar feriado);

	Date devolveDataUtil(Date data);

	Date devolveDataUtil(Date data, int somaDia);

	DatasParcelaTO getDataPagamentoDataAgendamento(String data);
}
