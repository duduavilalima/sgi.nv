package br.com.araujoabreu.sgi.service.impl.comercial;

import br.com.araujoabreu.sgi.administracao.domain.FormaContratacao;
import br.com.araujoabreu.sgi.administracao.domain.QFormaContratacao;
import br.com.araujoabreu.sgi.service.comercial.FormaContratacaoService;
import br.com.araujoabreu.sgi.service.comercial.transfer.FormaContratacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.FormaContratacaoTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.comercial.FormaContratacaoRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FormaContratacaoServiceImpl extends BaseSearchService<FormaContratacao, FormaContratacaoTO, FormaContratacaoPesquisaTO, FormaContratacaoRepository> implements FormaContratacaoService {

	private static final QFormaContratacao qFormaContratacao = QFormaContratacao.formaContratacao;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	public FormaContratacaoServiceImpl(FormaContratacaoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FormaContratacaoPesquisaTO exampleObj) {
		FormaContratacao fc = getMapper().map(exampleObj, FormaContratacao.class);

		if (Check.isAssigned(exampleObj.getNome())) {
			builder.and(qFormaContratacao.nome.containsIgnoreCase(exampleObj.getNome().trim()));
		}
		if (Check.isAssigned(exampleObj.getDescricao())) {
			builder.and(qFormaContratacao.descricao.containsIgnoreCase(exampleObj.getDescricao().trim()));
		}
		if (Check.isAssigned(exampleObj.getTipo())) {
			builder.and(qFormaContratacao.tipo.eq(fc.getTipo()));
		}
	}

	@Override
	protected void doAfterLoad(FormaContratacaoTO transferObj) {
		if (transferObj != null && transferObj.getCriador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getCriador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeCriador(list.get(0).getNome());
			}
		}

		if (transferObj != null && transferObj.getAtualizador() != null) {
			List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(transferObj.getAtualizador());
			if (list != null && !list.isEmpty()) {
				transferObj.setNomeAtualizador(list.get(0).getNome());
			}
		}
	}

	@Override
	protected void doBeforeSave(FormaContratacao entity, FormaContratacaoTO object) throws ServiceException {
		entity.setDataCriacao(new Date());
		entity.setCriador(usuarioSecurityService.getUsername());
		super.doBeforeSave(entity, object);
	}

	@Override
	protected void doBeforeUpdate(FormaContratacao entity, FormaContratacaoTO object) throws ServiceException {
		object.setDataAtualizacao(new Date());
		object.setAtualizador(usuarioSecurityService.getUsername());
		super.doBeforeUpdate(entity, object);
	}
}
