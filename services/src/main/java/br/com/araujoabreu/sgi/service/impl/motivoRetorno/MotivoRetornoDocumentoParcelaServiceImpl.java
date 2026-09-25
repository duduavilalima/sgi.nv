package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoFaturamento;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumentoFaturamento;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoParcelaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoFaturamentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotivoRetornoDocumentoParcelaServiceImpl extends BaseSearchService<MotivoRetornoDocumentoFaturamento, MotivoRetornoDocumentoFaturamentoTO, MotivoRetornoDocumentoFaturamentoPesquisaTO, MotivoRetornoDocumentoFaturamentoRepository> implements MotivoRetornoDocumentoParcelaService {

	private static final QMotivoRetornoDocumentoFaturamento query = QMotivoRetornoDocumentoFaturamento.motivoRetornoDocumentoFaturamento;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	public MotivoRetornoDocumentoParcelaServiceImpl(MotivoRetornoDocumentoFaturamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoDocumentoFaturamentoPesquisaTO exampleObj) {
		if (exampleObj.getFaturamento() != null && exampleObj.getFaturamento().getCodigo() != null) {
			builder.and(query.faturamento.codigo.eq(exampleObj.getFaturamento().getCodigo()));
		}
	}

}
