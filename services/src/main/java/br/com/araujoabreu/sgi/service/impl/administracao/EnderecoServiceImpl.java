package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.service.administracao.EnderecoService;
import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.TipoLogradouroRepository;
import br.com.fasolti.sgi.repository.cidade.CidadeRepository;
import br.com.fasolti.sgi.repository.unidade.EnderecoRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class EnderecoServiceImpl extends BaseSearchService
	<Endereco,
		EnderecoTO,
		EnderecoPesquisaTO,
		EnderecoRepository> implements EnderecoService {

	private static final QEndereco query = QEndereco.endereco1;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private TipoLogradouroRepository tipoLogradouroRepository;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	public EnderecoServiceImpl(EnderecoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, EnderecoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCodigo())) {
			builder.and(query.codigo.eq(exampleObj.getCodigo()));
		}
	}

	@Override
	protected void doAfterSearch(List<Endereco> entities, List<EnderecoPesquisaTO> result) throws ServiceException {
		for (EnderecoPesquisaTO enderecoPesquisaTO : result) {
			if (Check.isAssigned(enderecoPesquisaTO.getCodigoCidade())) {
				Cidade cidade = cidadeRepository.findById(enderecoPesquisaTO.getCodigoCidade()).orElse(null);
				enderecoPesquisaTO.setNomeCidade(cidade.getCidadeUf());
			}

			if (Check.isAssigned(enderecoPesquisaTO.getCodigoTipoLogradouro())) {
				TipoLogradouro tipoLogradouro = tipoLogradouroRepository.findById(enderecoPesquisaTO.getCodigoTipoLogradouro()).orElse(null);
				enderecoPesquisaTO.setLogradouroTipo(tipoLogradouro.getNome());
			}
		}
	}

	@Override
	public EnderecoPesquisaTO findLastNotaFiscalByUnidadeCodigo(EnderecoPesquisaTO exampleObj) {
		Endereco endereco = null;

		if (exampleObj.getCodigoPessoa() == null) {
			return null;
		}

		if (Check.isAssigned(exampleObj.getCodigoPessoa())) {
			endereco = getRepository().findByPessoa(exampleObj.getCodigoPessoa());
		}

		EnderecoPesquisaTO enderecoPesquisaTO = mapSearch(endereco);

		if (Check.isAssigned(enderecoPesquisaTO.getCodigoCidade())) {
			Cidade cidade = cidadeRepository.findById(enderecoPesquisaTO.getCodigoCidade()).orElse(null);
			enderecoPesquisaTO.setNomeCidade(cidade.getCidadeUf());
		}

		if (Check.isAssigned(enderecoPesquisaTO.getCodigoTipoLogradouro())) {
			TipoLogradouro tipoLogradouro = tipoLogradouroRepository.findById(enderecoPesquisaTO.getCodigoTipoLogradouro()).orElse(null);
			enderecoPesquisaTO.setLogradouroTipo(tipoLogradouro.getNome());
		}

		return enderecoPesquisaTO;
	}

	@Override
	public List<TipoLogradouroTO> buscarTipoLogradouro() {
		try {
			Iterable<TipoLogradouro> iterable = tipoLogradouroRepository.findAll();
			List<TipoLogradouro> listaTipoLogradouro = Lists.newArrayList(iterable);
			listaTipoLogradouro.sort((tipoLogradouro1, tipoLogradouro2) -> tipoLogradouro1.getNome().compareTo(tipoLogradouro2.getNome()));

			List<TipoLogradouroTO> listaTipoLogradouroTO = new ArrayList<>();

			for (TipoLogradouro tipoLogradouro : listaTipoLogradouro) {

				TipoLogradouroTO tipoLogradouroTO = getMapper().map(tipoLogradouro, TipoLogradouroTO.class);
				listaTipoLogradouroTO.add(tipoLogradouroTO);

			}
			return listaTipoLogradouroTO;

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	@Override
	protected void doBeforeSave(Endereco entity, EnderecoTO object) {
		object.setDataCriacao(new Date());
		object.setCriador(usuarioSecurityService.getUsername());
		entity.setUf(object.getUf());
	}
}
