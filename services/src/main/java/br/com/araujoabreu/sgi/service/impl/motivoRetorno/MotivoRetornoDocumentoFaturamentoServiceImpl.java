package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoFaturamento;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumentoFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoFaturamentoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoFaturamentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.faturamento.FaturamentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoFaturamentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MotivoRetornoDocumentoFaturamentoServiceImpl extends BaseSearchService<MotivoRetornoDocumentoFaturamento, MotivoRetornoDocumentoFaturamentoTO, MotivoRetornoDocumentoFaturamentoPesquisaTO, MotivoRetornoDocumentoFaturamentoRepository> implements MotivoRetornoDocumentoFaturamentoService {

	private static final QMotivoRetornoDocumentoFaturamento qMotivoRetornoDocumentoFaturamento = QMotivoRetornoDocumentoFaturamento.motivoRetornoDocumentoFaturamento;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private FaturamentoRepository faturamentoRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	public MotivoRetornoDocumentoFaturamentoServiceImpl(MotivoRetornoDocumentoFaturamentoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoDocumentoFaturamentoPesquisaTO exampleObj) {
		if (exampleObj.getFaturamento() != null && exampleObj.getFaturamento().getCodigo() != null) {
			builder.and(qMotivoRetornoDocumentoFaturamento.faturamento.codigo.eq(exampleObj.getFaturamento().getCodigo()));
		}
	}

	@Override
	protected void doAfterSearch(List<MotivoRetornoDocumentoFaturamento> entities, List<MotivoRetornoDocumentoFaturamentoPesquisaTO> result) throws ServiceException {
		for (MotivoRetornoDocumentoFaturamentoPesquisaTO to : result) {
			MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.findById(to.getMotivoRetornoDocumento().getCodigo()).orElse(null);
			Pessoa pessoa = pessoaRepository.findById(motivoRetornoDocumento.getPessoa().getCodigo()).orElse(null);
			to.setMotivoRetornoDocumento(new MotivoRetornoDocumentoTO(motivoRetornoDocumento.getCodigo(), motivoRetornoDocumento.getObservacao(), new PessoaTO(pessoa.getCodigo(), pessoa.getDocumento(), pessoa.getNome(), pessoa.getTipoPessoa().toString(), pessoa.getSituacao().toString())));
			to.getMotivoRetornoDocumento().setCriador(motivoRetornoDocumento.getCriador());
			to.getMotivoRetornoDocumento().setDataCriacao(motivoRetornoDocumento.getDataCriacao());
			to.getMotivoRetornoDocumento().setAtualizador(motivoRetornoDocumento.getAtualizador());
			to.getMotivoRetornoDocumento().setDataAtualizacao(motivoRetornoDocumento.getDataAtualizacao());

			List<MotivoDocumento> list = motivoDocumentoRepository.findByMotivoRetornoDocumentoCodigo(motivoRetornoDocumento.getCodigo());
			to.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

			for (MotivoDocumento motivoDocumento : list) {
				MotivoDocumentoTO auxTO = new MotivoDocumentoTO();

				MotivoRetornoTO mrTO = new MotivoRetornoTO();
				MotivoRetorno mr = motivoRetornoRepository.findById(motivoDocumento.getMotivoRetorno().getCodigo()).orElse(null);
				mrTO.setCodigo(mr.getCodigo());
				mrTO.setMotivo(mr.getMotivo());
				mrTO.setSolucao(mr.getSolucao());
				mrTO.setCriador(mr.getCriador());
				mrTO.setDataCriacao(mr.getDataCriacao());
				mrTO.setAtualizador(mr.getAtualizador());
				mrTO.setDataAtualizacao(mr.getDataAtualizacao());

				auxTO.setCodigo(motivoDocumento.getCodigo());
				auxTO.setMotivoRetorno(mrTO);

				to.getMotivoRetornoDocumento().getListaMotivoRetorno().add(auxTO);
			}
		}
	}

	@Override
	protected void doBeforeUpdate(MotivoRetornoDocumentoFaturamento entity, MotivoRetornoDocumentoFaturamentoTO object) throws ServiceException {
		prepareValidate(object);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoDocumentoFaturamento entity, MotivoRetornoDocumentoFaturamentoTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	public void prepareSave(MotivoRetornoDocumentoFaturamento entity, MotivoRetornoDocumentoFaturamentoTO to) {
		Faturamento faturamento = faturamentoRepository.findById(to.getFaturamento().getCodigo()).orElse(null);
		faturamento.setCancelador(securityService.getUser().getPessoaFisica());
		faturamento.setDataCancelamento(new Timestamp(new Date().getTime()));
		faturamento.setSituacao(Faturamento.Status.CANCELADO);
		faturamento.setDataAtualizacao(new Date());
		faturamento.setAtualizador(securityService.getUser().getLogin());
		entity.setFaturamento(faturamento);

		entity.setFaturamento(faturamentoRepository.save(entity.getFaturamento()));

		entity.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(to.getMotivoRetornoDocumento().getAplicacao().getChave()));

		entity.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
		entity.getMotivoRetornoDocumento().setCriador(securityService.getUser().getLogin());
		entity.getMotivoRetornoDocumento().setDataCriacao(new Date());

		entity.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());

		entity.getMotivoRetornoDocumento().setObservacao(to.getMotivoRetornoDocumento().getObservacao());

		if (to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivoDocumentoTO : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				entity.getMotivoRetornoDocumento().getListaMotivoRetorno().add(new MotivoDocumento(entity.getMotivoRetornoDocumento(), motivoRetornoRepository.findById(motivoDocumentoTO.getMotivoRetorno().getCodigo()).orElse(null)));
			}
		}

		MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(entity.getMotivoRetornoDocumento());

		entity.setMotivoRetornoDocumento(motivoRetornoDocumento);
	}

	public void prepareValidate(MotivoRetornoDocumentoFaturamentoTO to) throws ValidationException {
		if (to.getFaturamento() == null || to.getFaturamento().getCodigo() == null) {
			throw new ValidationException("ValidationException","Faturamento não informado.");
		}

		if (to.getMotivoRetornoDocumento() == null) {
			throw new ValidationException("ValidationException","Motivo de retorno documento não informado.");
		}

		if (to.getMotivoRetornoDocumento().getAplicacao() == null) {
			throw new ValidationException("ValidationException","Motivo aplicação não informado.");
		} else if (to.getMotivoRetornoDocumento().getAplicacao().getChave() == null || to.getMotivoRetornoDocumento().getAplicacao().getChave().isEmpty()) {
			throw new ValidationException("ValidationException","Chave do motivo aplicação não informada.");
		}

		if ((to.getMotivoRetornoDocumento().getListaMotivoRetorno() == null || to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty())
			&& (to.getMotivoRetornoDocumento().getObservacao() == null || to.getMotivoRetornoDocumento().getObservacao().isEmpty())) {
			throw new ValidationException("ValidationException","Lista de motivos de retorno e observação nulos, pelo menos um deve ser informado.");
		}

		if (to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivo : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				if (motivo.getMotivoRetorno() == null) {
					throw new ValidationException("ValidationException","Motivo de retorno não informado");
				} else {
					if (motivo.getMotivoRetorno().getCodigo() == null) {
						throw new ValidationException("ValidationException",String.format("Código do motivo de retorno [%s], não informado.", motivo.getMotivoRetorno().getMotivo()));
					}
				}
			}
		}
	}

}
