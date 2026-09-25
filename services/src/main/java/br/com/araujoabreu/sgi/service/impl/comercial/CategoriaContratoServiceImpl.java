package br.com.araujoabreu.sgi.service.impl.comercial;

import br.com.araujoabreu.sgi.administracao.domain.CategoriaContrato;
import br.com.araujoabreu.sgi.administracao.domain.QCategoriaContrato;
import br.com.araujoabreu.sgi.comercial.domain.ContratoCategoriaContrato;
import br.com.araujoabreu.sgi.service.comercial.CategoriaContratoService;
import br.com.araujoabreu.sgi.service.comercial.transfer.CategoriaContratoPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.CategoriaContratoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.comercial.CategoriaContratoRepository;
import br.com.fasolti.sgi.repository.comercial.ContratoCategoriaContratoRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoriaContratoServiceImpl extends BaseSearchService<CategoriaContrato, CategoriaContratoTO, CategoriaContratoPesquisaTO, CategoriaContratoRepository> implements CategoriaContratoService {

	private static final QCategoriaContrato qCategoriaContrato = QCategoriaContrato.categoriaContrato;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private ContratoCategoriaContratoRepository contratoCategoriaContratoRepository;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	public CategoriaContratoServiceImpl(CategoriaContratoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CategoriaContratoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getTipo())) {
			builder.and(qCategoriaContrato.tipo.eq((CategoriaContrato.TipoCategoriaContrato) Util.getEnumByString(CategoriaContrato.TipoCategoriaContrato.class, exampleObj.getTipo())));
		}

		if (exampleObj.getDescricao() != null) {
			builder.and(qCategoriaContrato.descricao.upper().contains(exampleObj.getDescricao().toUpperCase().trim()));
		}
	}

	@Override
	protected void doAfterLoad(CategoriaContratoTO categoriaContratoTO) {
		if (categoriaContratoTO != null && categoriaContratoTO.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(categoriaContratoTO.getCriador());
			if (list != null && !list.isEmpty()) {
				categoriaContratoTO.setCriador(list.get(0).getNome());
			}
		}

		if (categoriaContratoTO != null && categoriaContratoTO.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(categoriaContratoTO.getAtualizador());
			if (list != null && !list.isEmpty()) {
				categoriaContratoTO.setAtualizador(list.get(0).getNome());
			}
		}
	}

	@Override
	protected void doBeforeUpdate(CategoriaContrato entity, CategoriaContratoTO to) throws ServiceException {
		prepareValidateToSave(to);
		to.setDataAtualizacao(new Date());
		to.setAtualizador(usuarioSecurityService.getUsername());
	}

	@Override
	protected void doBeforeSave(CategoriaContrato entity, CategoriaContratoTO to) throws ServiceException {
		prepareValidateToSave(to);
		entity.setDataCriacao(new Date());
		entity.setCriador(usuarioSecurityService.getUsername());
	}

	public void prepareValidateToSave(CategoriaContratoTO categoriaContratoTO) throws ServiceException {
		CategoriaContrato.TipoCategoriaContrato tipo = null;

		for (CategoriaContrato.TipoCategoriaContrato tipoCategoriaContrato : CategoriaContrato.TipoCategoriaContrato.values()) {
			if (tipoCategoriaContrato.toString().trim().equalsIgnoreCase(categoriaContratoTO.getTipo())) {
				tipo = tipoCategoriaContrato;
			}
		}

		List<CategoriaContrato> categoriaList = getRepository().findCategoriaContratoByDescricaoAndTipo(categoriaContratoTO.getDescricao(), tipo);

		if (categoriaList != null && !categoriaList.isEmpty()) {
			for (CategoriaContrato categoriaContrato : categoriaList) {
				if (!categoriaContrato.getCodigo().equals(categoriaContratoTO.getCodigo())) {
					throw new ValidationException("ValidationException","Categoria de Contrato já cadastrada.");
				}
			}
		}
	}

	@Override
	protected void doBeforeDelete(CategoriaContratoTO object) throws ServiceException {
		List<ContratoCategoriaContrato> contratoCategoriaContratos =
			contratoCategoriaContratoRepository.findByCodigoCategoriaContrato(object.getId());

		if (contratoCategoriaContratos != null && !contratoCategoriaContratos.isEmpty()) {
			throw new ValidationException("ValidationException","Esta categoria de contrato está associada a 1 ou mais contratos. Não foi possível removê-la.");
		}
	}

}
