package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoContratoService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoContratoPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoContratoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroSituacaoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoSituacaoRepository;
import br.com.fasolti.sgi.repository.contrato.MotivoRetornoDocumentoContratoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MotivoRetornoDocumentoContratoServiceImpl extends BaseSearchService<MotivoRetornoDocumentoContrato, MotivoRetornoDocumentoContratoTO, MotivoRetornoDocumentoContratoPesquisaTO, MotivoRetornoDocumentoContratoRepository> implements MotivoRetornoDocumentoContratoService {

	private static final QMotivoRetornoDocumentoContrato qMotivoRetornoDocumentoContrato = QMotivoRetornoDocumentoContrato.motivoRetornoDocumentoContrato;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private ContratoSituacaoRepository contratoSituacaoRepository;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private CentroFinanceiroSituacaoRepository centroFinanceiroSituacaoRepository;

	@Autowired
	public MotivoRetornoDocumentoContratoServiceImpl(MotivoRetornoDocumentoContratoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoDocumentoContratoPesquisaTO exampleObj) {
		if (exampleObj.getContrato() != null && exampleObj.getContrato().getCodigo() != null) {
			builder.and(qMotivoRetornoDocumentoContrato.contrato.codigo.eq(exampleObj.getContrato().getCodigo()));
		}
	}

	@Override
	protected void doAfterSearch(List<MotivoRetornoDocumentoContrato> entities, List<MotivoRetornoDocumentoContratoPesquisaTO> result) throws ServiceException {
		for (MotivoRetornoDocumentoContratoPesquisaTO to : result) {
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
	protected void doBeforeUpdate(MotivoRetornoDocumentoContrato entity, MotivoRetornoDocumentoContratoTO object) throws ServiceException {
		prepareValidate(object);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoDocumentoContrato entity, MotivoRetornoDocumentoContratoTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	public void prepareSave(MotivoRetornoDocumentoContrato entity, MotivoRetornoDocumentoContratoTO to) {
		Contrato contrato = contratoRepository.findById(to.getContrato().getCodigo()).orElse(null);
		contrato.setSituacao(Contrato.SituacaoContrato.CANCELADO);
		contrato.setDataAtualizacao(new Date());
		contrato.setAtualizador(securityService.getUser().getLogin());
		entity.setContrato(contrato);

		entity.setContrato(contratoRepository.save(entity.getContrato()));

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

		Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);

		ContratoSituacao contratoSituacao = new ContratoSituacao();
		contratoSituacao.setColaborador(colaborador);
		contratoSituacao.setContrato(contrato);
		contratoSituacao.setSituacao(Contrato.SituacaoContrato.CANCELADO);
		contratoSituacao.setDataSituacao(new Date());
		contratoSituacaoRepository.save(contratoSituacao);

		for (CentroFinanceiro centroFinanceiro : centroFinanceiroRepository.findByContrato(contrato)) {
			if (!CentroFinanceiro.SituacaoCentroFinanceiro.ENCERRADO.equals(centroFinanceiro.getSituacao())
				&& !CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO.equals(centroFinanceiro.getSituacao())) {

				centroFinanceiro.setSituacao(CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO);
				centroFinanceiro.setDataAtualizacao(new Date());
				centroFinanceiro.setAtualizador(securityService.getUsername());
				centroFinanceiroRepository.save(centroFinanceiro);

				adicionarCentroFinanceiroSituacao(colaborador, centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro.CANCELADO, false);
			}
		}

		MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(entity.getMotivoRetornoDocumento());

		entity.setMotivoRetornoDocumento(motivoRetornoDocumento);
	}

	public void adicionarCentroFinanceiroSituacao(Colaborador colaborador, CentroFinanceiro centroFinanceiro, CentroFinanceiro.SituacaoCentroFinanceiro situacaoCentroFinanceiro, boolean flush) {
		CentroFinanceiroSituacao centroFinanceiroSituacao = new CentroFinanceiroSituacao(centroFinanceiro, colaborador, new Date(), situacaoCentroFinanceiro);
		centroFinanceiroSituacaoRepository.save(centroFinanceiroSituacao);
	}


	public void prepareValidate(MotivoRetornoDocumentoContratoTO to) throws ValidationException {
		if (to.getContrato() == null || to.getContrato().getCodigo() == null) {
			throw new ValidationException("prepareValidate", "Contrato não informado.");
		}

		if (to.getMotivoRetornoDocumento() == null) {
			throw new ValidationException("prepareValidate", "Motivo de retorno documento não informado.");
		}

		if (to.getMotivoRetornoDocumento().getAplicacao() == null) {
			throw new ValidationException("prepareValidate", "Motivo aplicação não informado.");
		} else if (to.getMotivoRetornoDocumento().getAplicacao().getChave() == null || to.getMotivoRetornoDocumento().getAplicacao().getChave().isEmpty()) {
			throw new ValidationException("prepareValidate", "Chave do motivo aplicação não informada.");
		}

		if ((to.getMotivoRetornoDocumento().getListaMotivoRetorno() == null || to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty())
			&& (to.getMotivoRetornoDocumento().getObservacao() == null || to.getMotivoRetornoDocumento().getObservacao().isEmpty())) {
			throw new ValidationException("prepareValidate", "Lista de motivos de retorno e observação nulos, pelo menos um deve ser informado.");
		}

		if (to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivo : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				if (motivo.getMotivoRetorno() == null) {
					throw new ValidationException("prepareValidate", "Motivo de retorno não informado");
				} else {
					if (motivo.getMotivoRetorno().getCodigo() == null) {
						throw new ValidationException("prepareValidate", String.format("Código do motivo de retorno [%s], não informado.", motivo.getMotivoRetorno().getMotivo()));
					}
				}
			}
		}
	}

}
