package br.com.araujoabreu.sgi.service.impl.diretoria;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.ColaboradorCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Diretoria;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.QDiretoria;
import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.service.Diretoria.DiretoriaService;
import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaPesquisaTO;
import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.colaboradorCentroFinanceiro.ColaboradorCentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.diretoria.DiretoriaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DiretoriaServiceImpl extends BaseSearchService<Diretoria, DiretoriaSimpleTO, DiretoriaPesquisaTO, DiretoriaRepository> implements DiretoriaService {

	private static final QDiretoria qDiretoria = QDiretoria.diretoria;

	private static final QUnidade qUnidade = QUnidade.unidade;

	@Autowired
	private ColaboradorCentroFinanceiroRepository colaboradorCentroFinanceiroRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public DiretoriaServiceImpl(DiretoriaRepository repository) {
		super(repository);
	}

	@Override
	public List<DiretoriaSimpleTO> buscarDiretoriasDeContratos() {
		return findDiretoriasDeContratos();
	}

	public List<DiretoriaSimpleTO> findDiretoriasDeContratos() {
		List<Diretoria> diretorias = getRepository().findByPermiteContrato(Boolean.TRUE);

		if (ObjectUtil.isNotEmpty(diretorias)) {
			List<DiretoriaSimpleTO> retornoDiretoria = new ArrayList<>();

			for (Diretoria diretoria : diretorias) {
				retornoDiretoria.add(getMapper().map(diretoria, DiretoriaSimpleTO.class));
			}

			return retornoDiretoria;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<DiretoriaSimpleTO> buscarDiretoriaDeContratosByColaborador() {
		return findDiretoriasDeContratosByColaborador();
	}

	public List<DiretoriaSimpleTO> findDiretoriasDeContratosByColaborador() {
		Set<Diretoria> diretorias = new HashSet<>();

		if (securityService.getUser().temRole("admin-contrato")
			|| securityService.getUser().temRole("aprov-contrato-dir")
			|| securityService.getUser().temRole("aprov-contrato-ger")) {

			diretorias = new HashSet<>(getRepository().findByPermiteContrato(Boolean.TRUE));
			Set<Diretoria> stDiretorias = new HashSet<>();
			Colaborador colaborador = colaboradorRepository.findById(securityService.getUser().getPessoaFisica().getCodigo()).orElse(null);
			List<ColaboradorCentroFinanceiro> colaboradorCentroFinanceiro = colaboradorCentroFinanceiroRepository.findByColaborador(colaborador);

			for (ColaboradorCentroFinanceiro centroFinanceiro : colaboradorCentroFinanceiro) {
				if (centroFinanceiro.getCentroFinanceiro().getGerencia() instanceof Gerencia) {
					Gerencia g = (Gerencia) centroFinanceiro.getCentroFinanceiro().getGerencia();
					Diretoria d = getRepository().findByCodigo(g.getDiretoria().getCodigo());
					if (diretorias.contains(d))
						stDiretorias.add(d);
				}
			}
			diretorias = stDiretorias;
		} else if (securityService.getUser().temRole("aprov-contrato-rh")
			|| securityService.getUser().temRole("aprov-contrato-cont")
			|| securityService.getUser().temRole("aprov-contrato-fin")
			|| securityService.getUser().temRole("aprov-contrato-com")
			|| securityService.getUser().temRole("aprov-contrato-pres")) {

			diretorias = new HashSet<>(getRepository().findByPermiteContrato(Boolean.TRUE));
		}

		if (ObjectUtil.isNotEmpty(diretorias)) {
			ArrayList<DiretoriaSimpleTO> retornoDiretoria = new ArrayList<>();

			for (Diretoria diretoria : diretorias) {
				retornoDiretoria.add(getMapper().map(diretoria, DiretoriaSimpleTO.class));
			}

			return retornoDiretoria;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public List<DiretoriaSimpleTO> buscarDiretoriaPorEmpresa(@PathVariable String idEmpresa) throws Exception {
		try {
			JPAQuery<Diretoria> query = select();
			query.from(qDiretoria)
				.join(qDiretoria.unidade, qUnidade);
			query.where(new BooleanBuilder().and(qUnidade.codigo.eq(Long.parseLong(idEmpresa))).and(qDiretoria.permiteContrato.eq(Boolean.TRUE)));
			query.select(Projections.bean(
				Diretoria.class,
				qDiretoria.codigo,
				qDiretoria.descricao
			));
			query.orderBy(qDiretoria.descricao.asc());

			List<Diretoria> listaDiretoria = query.fetch();

			List<DiretoriaSimpleTO> listaTO = new ArrayList<>();

			for (Diretoria diretoria1 : listaDiretoria) {
				listaTO.add(getMapper().map(diretoria1, DiretoriaSimpleTO.class));
			}

			return listaTO;
		} catch (Exception e) {
			throw new Exception(e);

		}
	}

}
