package br.com.araujoabreu.sgi.service.impl.motivoRetorno;


import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumento;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoChequeContaCorrente;
import br.com.araujoabreu.sgi.administracao.domain.QMotivoRetornoDocumentoChequeContaCorrente;
import br.com.araujoabreu.sgi.service.motivoRetorno.MotivoRetornoDocumentoChequeContaCorrenteService;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoDocumentoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoChequeContaCorrentePesquisaTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoRetornoDocumentoChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaRepository;
import br.com.fasolti.sgi.repository.contaCorrente.ContaCorrenteRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoAplicacaoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoChequeContaCorrenteRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoDocumentoRepository;
import br.com.fasolti.sgi.repository.motivoRetorno.MotivoRetornoRepository;
import br.com.fasolti.sgi.repository.tesouraria.ChequeContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class MotivoRetornoDocumentoChequeContaCorrenteServiceImpl extends BaseSearchService<MotivoRetornoDocumentoChequeContaCorrente, MotivoRetornoDocumentoChequeContaCorrenteTO, MotivoRetornoDocumentoChequeContaCorrentePesquisaTO, MotivoRetornoDocumentoChequeContaCorrenteRepository> implements MotivoRetornoDocumentoChequeContaCorrenteService {

	private static final QMotivoRetornoDocumentoChequeContaCorrente query = QMotivoRetornoDocumentoChequeContaCorrente.motivoRetornoDocumentoChequeContaCorrente;

	@Autowired
	private MotivoAplicacaoRepository motivoAplicacaoRepository;

	@Autowired
	private ChequeContaCorrenteRepository chequeContaCorrenteRepository;

	@Autowired
	private MotivoRetornoDocumentoRepository motivoRetornoDocumentoRepository;

	@Autowired
	private MotivoDocumentoRepository motivoDocumentoRepository;

	@Autowired
	private MotivoRetornoRepository motivoRetornoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ContaCorrenteRepository contaCorrenteRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public MotivoRetornoDocumentoChequeContaCorrenteServiceImpl(MotivoRetornoDocumentoChequeContaCorrenteRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	protected void doBeforeSave(MotivoRetornoDocumentoChequeContaCorrente entity, MotivoRetornoDocumentoChequeContaCorrenteTO to) {
		prepareValidate(to);
		prepareSave(entity, to);
	}

	@Transactional
	public void prepareSave(MotivoRetornoDocumentoChequeContaCorrente entity, MotivoRetornoDocumentoChequeContaCorrenteTO to) {
		Long codigoCheque = null;
		ChequeContaCorrente chequeEmBranco = null;

		if (to.getChequeContaCorrenteTO().getCodigo() != null) {
			codigoCheque = to.getChequeContaCorrenteTO().getCodigo();
		} else {
			chequeEmBranco = new ChequeContaCorrente();
			chequeEmBranco.setCodigo(to.getChequeContaCorrenteTO().getCodigo());
			chequeEmBranco.setCheque(to.getChequeContaCorrenteTO().getCheque());
			chequeEmBranco.setContaCorrente(new ContaCorrente(to.getChequeContaCorrenteTO().getCodigoContaCorrente()));
			chequeEmBranco.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
			chequeEmBranco.setValor(BigDecimal.ZERO);
			chequeEmBranco.setCriador(securityService.getUser().getUsername());
			chequeEmBranco.setDataCriacao(new Date());
		}

		ChequeContaCorrente chequeContaCorrente = null;

		if (codigoCheque != null) {
			chequeContaCorrente = chequeContaCorrenteRepository.findById(codigoCheque).orElse(null);
		} else {
			chequeContaCorrente = chequeEmBranco;
		}

		chequeContaCorrente.setResponsavelCancelamento(securityService.getUser().getPessoaFisica());
		chequeContaCorrente.setDataCancelamento(new Timestamp(new Date().getTime()));
		chequeContaCorrente.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.CANCELADO_USUARIO);
		chequeContaCorrente.setCheque(to.getChequeContaCorrenteTO().getCheque());
		chequeContaCorrente.setDataAtualizacao(new Date());
		chequeContaCorrente.setAtualizador(securityService.getUser().getLogin());

		chequeContaCorrente = chequeContaCorrenteRepository.save(chequeContaCorrente);

		entity.setChequeContaCorrente(chequeContaCorrente);

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


	public void prepareValidate(MotivoRetornoDocumentoChequeContaCorrenteTO to) throws ValidationException {
		/*if (to.getChequeContaCorrenteTO() == null || to.getChequeContaCorrenteTO().getCodigo() == null) {
			throw new ValidationException("ValidationException","Cheque não informado.");
		}*/

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

	@Transactional(propagation = Propagation.REQUIRED)
	public ChequeContaCorrente criarChequeEmBranco(ChequeContaCorrenteTO chequeContaCorrenteTO) {
		ContaCorrente contaCorrente = contaCorrenteRepository.findById(chequeContaCorrenteTO.getCodigoContaCorrente()).orElse(null);

		ChequeContaCorrente chequeContaCorrente = new ChequeContaCorrente();
		chequeContaCorrente.setCheque(chequeContaCorrenteTO.getCheque());
		chequeContaCorrente.setContaCorrente(contaCorrente);
		chequeContaCorrente.setSituacao(ChequeContaCorrente.StatusChequeContaCorrente.EMITIDO);
		chequeContaCorrente.setValor(BigDecimal.ZERO);
		chequeContaCorrente.setCriador(securityService.getUser().getUsername());
		chequeContaCorrente.setDataCriacao(new Date());

		chequeContaCorrente = chequeContaCorrenteRepository.save(chequeContaCorrente);

		return chequeContaCorrente;
	}

}
