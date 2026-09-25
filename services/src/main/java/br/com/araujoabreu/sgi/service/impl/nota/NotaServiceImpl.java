package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.QNota;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterfaceNota;
import br.com.araujoabreu.sgi.service.nota.NotaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.sgi.repository.nota.NotaRepository;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class NotaServiceImpl extends BaseSearchService<Nota, NotaTO, NotaPesquisaTO, NotaRepository> implements NotaService {

	private static final QNota qNota = QNota.nota;

	@Autowired
	public NotaServiceImpl(NotaRepository repository) {
		super(repository);
	}

	/*
	 * @Query("UPDATE Nota SET status = ?1 WHERE codigo = (SELECT lote.nota.codigo FROM LoteInterfaceNota lote INNER JOIN lote.nota WHERE lote.codigo = ?2 ) ")
	 */
	@Override
	public void updateStatusByLoteInterfaceNotaEmRecebimentoFaturamento(Long codigoLoteInterfaceNota) {
		selectUpdate().where(qNota.codigo.eq(
			JPAExpressions
				.selectFrom(QLoteInterfaceNota.loteInterfaceNota)
				.select(QLoteInterfaceNota.loteInterfaceNota.nota.codigo)
				.innerJoin(QLoteInterfaceNota.loteInterfaceNota.nota)
				.where(QLoteInterfaceNota.loteInterfaceNota.codigo.eq(codigoLoteInterfaceNota))
		).and(qNota.status.ne(Nota.StatusNota.CANCELADA_FATURAMENTO)))
			.set(qNota.status, Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO)
			.execute();
	}

	@Override
	@Transactional
	public void updateDataReversaoCancelamento(Long codigo, Date dataReversaoCancelamento) {
		selectUpdate().where(qNota.codigo.eq(codigo)).set(qNota.dataReversaoCancelamento, dataReversaoCancelamento).execute();
	}

	@Override
	@Transactional
	public void updateResponsavelReversaoCancelamento(Long codigo, Long codigoPessoaFisica) {
		PessoaFisica pessoaFisica = null;
		if (codigoPessoaFisica != null) {
			pessoaFisica = new PessoaFisica();
			pessoaFisica.setCodigo(codigoPessoaFisica);
		}
		selectUpdate().where(qNota.codigo.eq(codigo)).set(qNota.responsavelReversaoCancelamento, pessoaFisica).execute();
	}

}
