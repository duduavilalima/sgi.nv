package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumento;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MotivoRetornoDocumentoServiceImpl extends BaseSearchService<MotivoRetornoDocumento, MotivoRetornoDocumentoTO, MotivoRetornoDocumentoPesquisaTO, MotivoRetornoDocumentoRepository> implements MotivoRetornoDocumentoService {

	private static final QMotivoRetornoDocumento qMotivoRetornoDocumento = QMotivoRetornoDocumento.motivoRetornoDocumento;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	public MotivoRetornoDocumentoServiceImpl(MotivoRetornoDocumentoRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(MotivoRetornoDocumento entity, MotivoRetornoDocumentoTO object) throws ServiceException {
		entity.setDataCriacao(new Date());
		entity.setCriador(usuarioSecurityService.getUsername());

		if (object.getPessoa() == null) {
			Pessoa pessoa = usuarioSecurityService.getUser().getPessoaFisica();
			entity.setPessoa(pessoa);
		}
	}

	@Override
	public MotivoRetornoDocumentoTO findByCodigo(Long codigo) {
		return map(select().where(qMotivoRetornoDocumento.codigo.eq(codigo)).fetchOne());
	}

	public MotivoRetornoDocumentoTO buscarMotivoRetornoDocumentoByCod(Long codigoMotivoRetornoDocumento) {
		QMotivoAplicacao qMotivoAplicacao = QMotivoAplicacao.motivoAplicacao;
		QPessoa qPessoa = QPessoa.pessoa;

		JPAQuery<MotivoRetornoDocumento> jpaQuery = select();

		QBean qBean = Projections.fields(MotivoRetornoDocumento.class,
			qMotivoRetornoDocumento.codigo,
			qMotivoRetornoDocumento.criador,
			qMotivoRetornoDocumento.dataCriacao,
			qMotivoRetornoDocumento.atualizador,
			qMotivoRetornoDocumento.dataAtualizacao,
			qMotivoRetornoDocumento.observacao,
			Projections.bean(MotivoAplicacao.class,
				qMotivoAplicacao.codigo,
				qMotivoAplicacao.chave
			).as("aplicacao"),
			Projections.bean(Pessoa.class,
				qPessoa.codigo
			).as("pessoa")
		);

		jpaQuery
			.select(qBean)
			.from(qMotivoRetornoDocumento)
			.join(qMotivoRetornoDocumento.pessoa, qPessoa)
			.leftJoin(qMotivoRetornoDocumento.aplicacao, qMotivoAplicacao)
			.where(qMotivoRetornoDocumento.codigo.eq(codigoMotivoRetornoDocumento));

		MotivoRetornoDocumento motivoRetornoDocumento = jpaQuery.fetchOne();

		return getMapper().map(motivoRetornoDocumento, MotivoRetornoDocumentoTO.class);
	}

	@Override
	public JPAQuery getJpaQuery() {
		return select();
	}
}
