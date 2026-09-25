package br.com.araujoabreu.sgi.service.impl.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoNotaSaidaParcela;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumentoNotaSaidaParcela;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoNotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoNotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoTO;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaBaixaRetencaoService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaBaixaRetencaoTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoNotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MotivoRetornoDocumentoNotaSaidaParcelaServiceImpl extends BaseSearchService<MotivoRetornoDocumentoNotaSaidaParcela, MotivoRetornoDocumentoNotaSaidaParcelaTO, MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO, MotivoRetornoDocumentoNotaSaidaParcelaRepository> implements MotivoRetornoDocumentoNotaSaidaParcelaService {

	private static final QMotivoRetornoDocumentoNotaSaidaParcela qMotivoRetornoDocumentoNotaSaidaParcela = QMotivoRetornoDocumentoNotaSaidaParcela.motivoRetornoDocumentoNotaSaidaParcela;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	private NotaSaidaParcelaBaixaRetencaoService notaSaidaParcelaBaixaRetencaoService;

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
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	public MotivoRetornoDocumentoNotaSaidaParcelaServiceImpl(MotivoRetornoDocumentoNotaSaidaParcelaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO exampleObj) {
		if (exampleObj.getNotaSaidaParcela() != null && exampleObj.getNotaSaidaParcela().getCodigo() != null) {
			builder.and(qMotivoRetornoDocumentoNotaSaidaParcela.notaSaidaParcela.codigo.eq(exampleObj.getNotaSaidaParcela().getCodigo()));
		}
	}

	@Override
	protected void doAfterSearch(List<MotivoRetornoDocumentoNotaSaidaParcela> entities, List<MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO> result) throws ServiceException {
		for (MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO to : result) {
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
	protected void doBeforeUpdate(MotivoRetornoDocumentoNotaSaidaParcela entity, MotivoRetornoDocumentoNotaSaidaParcelaTO object) throws ServiceException {
		prepareValidate(object);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoDocumentoNotaSaidaParcela entity, MotivoRetornoDocumentoNotaSaidaParcelaTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	@Transactional
	public void prepareSave(MotivoRetornoDocumentoNotaSaidaParcela entity, MotivoRetornoDocumentoNotaSaidaParcelaTO to) {
		NotaSaidaParcela notaSaidaParcela = notaSaidaParcelaRepository.findById(to.getNotaSaidaParcela().getCodigo()).orElse(null);
		notaSaidaParcela.setResponsavelCancelamento(securityService.getUser().getPessoaFisica());
		notaSaidaParcela.setDataResponsavelCancelamento(new Timestamp(new Date().getTime()));
		notaSaidaParcela.setSituacao(NotaSaidaParcela.SituacaoParcela.CANCELADA);
		entity.setNotaSaidaParcela(notaSaidaParcela);

		entity.setNotaSaidaParcela(notaSaidaParcelaRepository.save(entity.getNotaSaidaParcela()));

		entity.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(to.getMotivoRetornoDocumento().getAplicacao().getChave()));

		entity.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());

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

	public void prepareValidate(MotivoRetornoDocumentoNotaSaidaParcelaTO to) throws ValidationException {
		if (to.getNotaSaidaParcela() == null || to.getNotaSaidaParcela().getCodigo() == null) {
			throw new ValidationException("ValidationException","Nota de saída não informada.");
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

	@Override
	@Transactional
	public void salvarMotivoRetornoParcelaRecebimento(MotivoRetornoDocumentoNotaSaidaParcelaTO to) {
		MotivoRetornoDocumentoNotaSaidaParcela entity = new MotivoRetornoDocumentoNotaSaidaParcela();

		NotaSaidaParcela notaSaidaParcela = notaSaidaParcelaRepository.findById(to.getNotaSaidaParcela().getCodigo()).orElse(null);

		List<NotaSaidaParcelaBaixaRetencaoTO> listaBaixas = notaSaidaParcelaBaixaRetencaoService.obterBaixasRetencaoDaNotaSaidaParcela(notaSaidaParcela.getCodigo());

		if (listaBaixas.size() > 0) {
			listaBaixas.forEach(notaSaidaParcelaBaixaRetencaoTO -> notaSaidaParcelaBaixaRetencaoService.removerBaixaRetencao(notaSaidaParcelaBaixaRetencaoTO.getCodigo()));
		}

		MotivoRetornoDocumento mrd = getMapper().map(to.getMotivoRetornoDocumento(), MotivoRetornoDocumento.class);
		mrd.setCriador(usuarioSecurityService.getUsername());
		mrd.setDataCriacao(new Date());

		entity.setNotaSaidaParcela(notaSaidaParcela);
		entity.setMotivoRetornoDocumento(mrd);
		entity.getMotivoRetornoDocumento().setAplicacao(motivoAplicacaoRepository.findByChave(to.getMotivoRetornoDocumento().getAplicacao().getChave()));
		entity.getMotivoRetornoDocumento().setPessoa(securityService.getUser().getPessoaFisica());
		entity.getMotivoRetornoDocumento().setListaMotivoRetorno(new ArrayList<>());
		entity.getMotivoRetornoDocumento().setObservacao(to.getMotivoRetornoDocumento().getObservacao());

		if (to.getMotivoRetornoDocumento() != null && to.getMotivoRetornoDocumento().getListaMotivoRetorno() != null && !to.getMotivoRetornoDocumento().getListaMotivoRetorno().isEmpty()) {
			for (MotivoDocumentoTO motivoDocumentoTO : to.getMotivoRetornoDocumento().getListaMotivoRetorno()) {
				entity.getMotivoRetornoDocumento().getListaMotivoRetorno().add(new MotivoDocumento(entity.getMotivoRetornoDocumento(), motivoRetornoRepository.findById(motivoDocumentoTO.getMotivoRetorno().getCodigo()).orElse(null)));
			}
		}

		MotivoRetornoDocumento motivoRetornoDocumento = motivoRetornoDocumentoRepository.save(entity.getMotivoRetornoDocumento());
		entity.setMotivoRetornoDocumento(motivoRetornoDocumento);

		getRepository().save(entity);
	}

	@Override
	@Transactional
	public void deletarPorParcela(Long codigoNotaSaidaParcela) {
		selectDelete().where(qMotivoRetornoDocumentoNotaSaidaParcela.notaSaidaParcela.codigo.eq(codigoNotaSaidaParcela)).execute();
	}
}
