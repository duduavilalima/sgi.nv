package br.com.araujoabreu.sgi.service.impl.fecho;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.comercial.domain.Fecho;
import br.com.araujoabreu.sgi.comercial.domain.FechoDespesaGeral;
import br.com.araujoabreu.sgi.comercial.domain.FechoMaoDeObra;
import br.com.araujoabreu.sgi.comercial.domain.FechoMaoDeObraAdicional;
import br.com.araujoabreu.sgi.comercial.domain.QFecho;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroAliquotaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroFluxoEtapaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroFluxoEtapaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSituacaoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.fecho.FechoDespesaGeralService;
import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraAdicionalService;
import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraService;
import br.com.araujoabreu.sgi.service.fecho.FechoService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoTO;
import br.com.araujoabreu.sgi.tesouraria.util.boletoUtil.ObjectUtil;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroFluxoEtapaRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroSituacaoRepository;
import br.com.fasolti.sgi.repository.fecho.FechoDespesaGeralRepository;
import br.com.fasolti.sgi.repository.fecho.FechoMaoDeObraAdicionalRepository;
import br.com.fasolti.sgi.repository.fecho.FechoMaoDeObraRepository;
import br.com.fasolti.sgi.repository.fecho.FechoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FechoServiceImpl extends BaseSearchService<Fecho, FechoTO, FechoPesquisaTO, FechoRepository> implements FechoService {

	private static final QFecho qFecho = QFecho.fecho;

	@Autowired
	private FechoMaoDeObraRepository fechoMaoDeObraRepository;

	@Autowired
	private FechoMaoDeObraAdicionalRepository fechoMaoDeObraAdicionalRepository;

	@Autowired
	private FechoDespesaGeralRepository fechoDespesaGeralRepository;

	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	@Autowired
	private CentroFinanceiroSituacaoRepository centroFinanceiroSituacaoRepository;

	@Autowired
	private CentroFinanceiroFluxoEtapaRepository centroFinanceiroFluxoEtapaRepository;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	private FechoMaoDeObraService fechoMaoDeObraService;

	@Autowired
	private FechoMaoDeObraAdicionalService fechoMaoDeObraAdicionalService;

	@Autowired
	private FechoDespesaGeralService fechoDespesaGeralService;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private CentroFinanceiroFluxoEtapaService centroFinanceiroFluxoEtapaService;

	@Autowired
	private CentroFinanceiroAliquotaService centroFinanceiroAliquotaService;

	@Autowired
	public FechoServiceImpl(FechoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Fecho> jpaQuery, FechoPesquisaTO exampleObj) {
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		jpaQuery.from(qFecho)
			.join(qFecho.centroFinanceiro, qCentroFinanceiro);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FechoPesquisaTO exampleObj) {
		if (Check.isAssigned(exampleObj.getCentroFinanceiro()) && Check.isAssigned(exampleObj.getCentroFinanceiro().getCodigo())) {
			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			builder.and(qCentroFinanceiro.codigo.eq(exampleObj.getCentroFinanceiro().getCodigo()));
		}
	}

	/**
	 * FIXME: Ao carregar o Fecho os itens nas listas "ListaFechosMaoDeObra" e "ListaFechosDespesasGerais" estão sendo duplicados. Por isso está sendo usado o Set neste método para remover as duplicações.
	 *
	 * @param transferObj
	 */
	@Override
	protected void doAfterLoad(FechoTO transferObj) {

		if (!transferObj.getListaFechosMaoDeObra().isEmpty() && transferObj.getListaFechosMaoDeObra().size() > 0) {
			Set<FechoMaoDeObraTO> set = new HashSet<>(transferObj.getListaFechosMaoDeObra());
			transferObj.getListaFechosMaoDeObra().clear();
			transferObj.getListaFechosMaoDeObra().addAll(set);
		}

		if (!transferObj.getListaFechosDespesasGerais().isEmpty() && transferObj.getListaFechosDespesasGerais().size() > 0) {
			Set<FechoDespesaGeralTO> set = new HashSet<>(transferObj.getListaFechosDespesasGerais());
			transferObj.getListaFechosDespesasGerais().clear();
			transferObj.getListaFechosDespesasGerais().addAll(set);
		}

		super.doAfterLoad(transferObj);
	}

	@Override
	public FechoTO fechoByCodCentroFinanceiro(String id) throws Exception {
		try {
			List<Fecho> listaFecho = getListaFechoByCodCentroFinanceiro(Long.parseLong(id));
			Fecho fecho = getUltimoFecho(listaFecho);
			FechoTO fechoTO = getMapper().map(fecho, FechoTO.class);

			fechoTO.setValorMaoObra(totalMaoDeObra(fechoTO.getCodigo()));
			fechoTO.setValorGastosGerais(totalGastosGerais(fechoTO.getCodigo()));
			fechoTO.setValorMaterial(totalMateriais(fechoTO.getCodigo()));
			fechoTO.setValorServicoTerceiro(totalServicosTerceiros(fechoTO.getCodigo()));

			return fechoTO;

		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public List<FechoTO> listaFechoByCodCentroFinanceiro(String id) throws Exception {
		try {
			return buscarListaFechoByCodCentroFinanceiro(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Override
	public List<FechoTO> buscarListaFechoByCodCentroFinanceiro(Long id) {
		List<FechoTO> listaFechoTO = new ArrayList<>();
		List<Fecho> listaFecho = getListaFechoByCodCentroFinanceiro(id);

		for (Fecho fecho : listaFecho) {
			FechoTO to = getMapper().map(fecho, FechoTO.class);
			listaFechoTO.add(to);
		}

		return listaFechoTO;
	}

	@Override
	@Transactional
	public void removerReajusteByCodCentroFinanceiro(String id) throws Exception {
		try {
			removerReajuste(Long.parseLong(id));
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	@Transactional
	private void removerReajuste(Long codCentroFinanceiro) {
		FechoTO ultimoFecho = getUltimoFechoByCodCentroFinanceiro(codCentroFinanceiro);

		if (ultimoFecho.getListaFechosMaoDeObra() != null && !ultimoFecho.getListaFechosMaoDeObra().isEmpty()) {
			for (FechoMaoDeObraTO fechoMaoDeObra : ultimoFecho.getListaFechosMaoDeObra()) {
				fechoMaoDeObraRepository.deleteById(fechoMaoDeObra.getCodigo());
			}
		}

		if (ultimoFecho.getListaFechosDespesasGerais() != null && !ultimoFecho.getListaFechosDespesasGerais().isEmpty()) {
			for (FechoDespesaGeralTO fechoDespesaGeral : ultimoFecho.getListaFechosDespesasGerais()) {
				fechoDespesaGeralRepository.deleteById(fechoDespesaGeral.getCodigo());
			}
		}

		ultimoFecho.setListaFechosMaoDeObra(null);
		ultimoFecho.setListaFechosDespesasGerais(null);

		getRepository().deleteById(ultimoFecho.getCodigo());
	}

	@Transactional
	@Override
	public void addReajuste(FechoTO fechoTO) throws Exception {
		try {
			salvarReajuste(fechoTO);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Transactional
	private void salvarReajuste(FechoTO fechoTO) throws Exception {
		Fecho reajuste = getMapper().map(fechoTO, Fecho.class);

		if (!validarReajuste(reajuste)) {
			throw new Exception("Pelo menos, um dos percentuais de reajuste deve ser preenchido.");
		} else {

			//cria fecho reajuste
			Fecho fecho = criarReajuste(reajuste);

			List<FechoDespesaGeralTO> fechoDespesaGeralTOS = new ArrayList<>();

			for (FechoDespesaGeral fechoDespesaGeral : fecho.getListaFechosDespesasGerais()) {
				fechoDespesaGeralTOS.add(getMapper().map(fechoDespesaGeral, FechoDespesaGeralTO.class));
			}

			fecho.setValorGastosGerais(fechoDespesaGeralService.getSubTotal(fechoDespesaGeralTOS, FechoDespesaGeral.TipoDespesaGeral.GASTO_GERAL.toString()));
			fecho.setValorMaterial(fechoDespesaGeralService.getSubTotal(fechoDespesaGeralTOS, FechoDespesaGeral.TipoDespesaGeral.MATERIAL.toString()));
			fecho.setValorServicoTerceiro(fechoDespesaGeralService.getSubTotal(fechoDespesaGeralTOS, FechoDespesaGeral.TipoDespesaGeral.SERVICO.toString()));

			List<FechoMaoDeObraTO> fechoMaoDeObraTOES = new ArrayList<>();

			for (FechoMaoDeObra fechoMaoDeObra : fecho.getListaFechosMaoDeObra()) {
				fechoMaoDeObraTOES.add(getMapper().map(fechoMaoDeObra, FechoMaoDeObraTO.class));
			}

			fecho.setValorMaoObra(fechoMaoDeObraService.getTotal(fechoMaoDeObraTOES, fecho.getPercentualEncargosSociais()));

			//Prepara para atualiza centro financeiro
			CentroFinanceiro centroFinanceiro = centroFinanceiroRepository.findById(fechoTO.getCentroFinanceiro().getCodigo()).orElse(null);
			PessoaFisica pessoaFisica = usuarioSecurityService.getUser().getPessoaFisica();

			centroFinanceiro.setDataAtualizacao(new Date());
			centroFinanceiro.setAtualizador(pessoaFisica.getUsuario().getLogin());

			//Criar situações
			List<CentroFinanceiroSituacaoTO> centroFinanceiroSituacoes = centroFinanceiroService.criarSituacoesParaCentroFinanceiro(getMapper().map(centroFinanceiro, CentroFinanceiroTO.class));

			//salvar entidades

			List<FechoDespesaGeral> listaDespesaGeralDoNovoFecho = new ArrayList<>(fecho.getListaFechosDespesasGerais());
			List<FechoMaoDeObra> listaMaoDeObraDoNovoFecho = new ArrayList<>(fecho.getListaFechosMaoDeObra());

			Fecho fechoSalvo = getRepository().save(fecho);

			if (listaDespesaGeralDoNovoFecho != null && !listaDespesaGeralDoNovoFecho.isEmpty()) {
				for (FechoDespesaGeral despesaGeral : listaDespesaGeralDoNovoFecho) {
					despesaGeral.setFecho(fechoSalvo);
					fechoDespesaGeralRepository.save(despesaGeral);
				}
			}

			if (listaMaoDeObraDoNovoFecho != null && !listaMaoDeObraDoNovoFecho.isEmpty()) {
				for (FechoMaoDeObra maoDeObra : listaMaoDeObraDoNovoFecho) {
					maoDeObra.setFecho(fechoSalvo);
					fechoMaoDeObraRepository.save(maoDeObra);
				}
			}

			centroFinanceiroRepository.save(centroFinanceiro);

			if (centroFinanceiroSituacoes != null && centroFinanceiroSituacoes.size() > 0) {
				for (CentroFinanceiroSituacaoTO situacao : centroFinanceiroSituacoes) {
					centroFinanceiroSituacaoRepository.save(getMapper().map(situacao, CentroFinanceiroSituacao.class));
				}
			}

			if (centroFinanceiro.getContrato() != null && centroFinanceiro.getContrato().getCodigo() != null) {
				//criar assinatura
				List<CentroFinanceiroFluxoEtapaTO> listaAssinaturas = centroFinanceiroFluxoEtapaService.criarAssinaturas();

				for (CentroFinanceiroFluxoEtapaTO assinatura : listaAssinaturas) {
					centroFinanceiroFluxoEtapaRepository.save(getMapper().map(assinatura, CentroFinanceiroFluxoEtapa.class));
				}
			}
		}
	}

	private Boolean validarReajuste(Fecho fecho) {
		if ((ObjectUtil.isNull(fecho.getPercentualReajusteGastosGerais()) || fecho.getPercentualReajusteGastosGerais().doubleValue() == 0.0d)
			&& (ObjectUtil.isNull(fecho.getPercentualReajusteMaoDeObra()) || fecho.getPercentualReajusteMaoDeObra().doubleValue() == 0.0d)
			&& (ObjectUtil.isNull(fecho.getPercentualReajusteMateriais()) || fecho.getPercentualReajusteMateriais().doubleValue() == 0.0d)
			&& (ObjectUtil.isNull(fecho.getPercentualReajusteServicos()) || fecho.getPercentualReajusteServicos().doubleValue() == 0.0d)) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private List<Fecho> getListaFechoByCodCentroFinanceiro(Long codCentroFinanceiro) {
		QColaborador qColaborador = QColaborador.colaborador;

		QBean<Fecho> qBean = Projections.bean(
			Fecho.class,
			qFecho.codigo,
			qFecho.numero,
			qFecho.dataReajuste,
			qFecho.percentualImposto,
			qFecho.percentualReajusteMaoDeObra,
			qFecho.percentualReajusteGastosGerais,
			qFecho.percentualReajusteMateriais,
			qFecho.percentualReajusteServicos,
			qFecho.percentualBdiBeneficios,
			qFecho.percentualBdiDespesas,
			qFecho.percentualEncargosSociais,
			qFecho.indiceRajuste,
			qFecho.valorMaoObra,
			qFecho.valorGastosGerais,
			qFecho.valorMaterial,
			qFecho.valorServicoTerceiro,
			Projections.bean(Colaborador.class, qColaborador.codigo, qColaborador.nome).as(qFecho.responsavelReajuste)
		);

		JPAQuery<Fecho> jpaQueryFecho = select();

		jpaQueryFecho
			.select(qBean)
			.from(qFecho).leftJoin(qFecho.responsavelReajuste, qColaborador)
			.where(qFecho.centroFinanceiro.codigo.eq(codCentroFinanceiro))
			.orderBy(qFecho.codigo.asc());

		return jpaQueryFecho.fetch();
	}

	//FIXME: Verificar se pode refatorar
	private Fecho getUltimoFecho(List<Fecho> listaFecho) {
		Fecho fecho = null;

		if (listaFecho != null && !listaFecho.isEmpty()) {
			int aux = -1;
			int index = -1;
			for (int x = 0; x < listaFecho.size(); x++) {
				if (listaFecho.get(x).getNumero() > aux) {
					aux = listaFecho.get(x).getNumero();
					index = x;
				}
			}
			fecho = listaFecho.get(index);
		} else {
			fecho = new Fecho();
		}

		return fecho;
	}

	@Override
	public FechoTO getUltimoFechoByCodCentroFinanceiro(Long codCentroFinanceiro) {
		List<Fecho> listaFecho = getListaFechoByCodCentroFinanceiro(codCentroFinanceiro);

		Fecho ultimoFecho = getUltimoFecho(listaFecho);

		ultimoFecho.setCentroFinanceiro(new CentroFinanceiro());
		ultimoFecho.getCentroFinanceiro().setCodigo(codCentroFinanceiro);

		List<FechoMaoDeObraTO> listaMaoDeObraTO = fechoMaoDeObraService.getListaMaoDeObraByCodFecho(ultimoFecho.getCodigo());

		List<FechoMaoDeObra> listaMaoDeObra = new ArrayList<>();

		if (listaMaoDeObraTO != null) {
			for (FechoMaoDeObraTO fechoMaoDeObraTO : listaMaoDeObraTO) {
				listaMaoDeObra.add(getMapper().map(fechoMaoDeObraTO, FechoMaoDeObra.class));
			}
		}

		ultimoFecho.setListaFechosMaoDeObra(listaMaoDeObra);

		List<FechoDespesaGeralTO> listaDespesaTO = fechoDespesaGeralService.getListaDespesaByCodFecho(ultimoFecho.getCodigo());

		List<FechoDespesaGeral> listaDespesa = new ArrayList<>();

		if (listaDespesaTO != null) {
			for (FechoDespesaGeralTO fechoDespesaGeralTO : listaDespesaTO) {
				listaDespesa.add(getMapper().map(fechoDespesaGeralTO, FechoDespesaGeral.class));
			}
		}

		ultimoFecho.setListaFechosDespesasGerais(listaDespesa);

		return map(ultimoFecho);
	}

	private Fecho criarReajuste(Fecho reajuste) throws Exception {

		FechoTO ultimoFecho = getUltimoFechoByCodCentroFinanceiro(reajuste.getCentroFinanceiro().getCodigo());
		Fecho novoFecho = new Fecho();

		try {
			novoFecho.setCentroFinanceiro(getMapper().map(ultimoFecho.getCentroFinanceiro(), CentroFinanceiro.class));

			PessoaFisica pessoaFisica = usuarioSecurityService.getUser().getPessoaFisica();
			Colaborador colaborador = new Colaborador();
			colaborador.setCodigo(pessoaFisica.getCodigo());

			novoFecho.setResponsavelReajuste(colaborador);
			novoFecho.setCriador(pessoaFisica.getUsuario().getLogin());
			novoFecho.setDataCriacao(new Date(System.currentTimeMillis()));

			novoFecho.setIndiceRajuste(reajuste.getIndiceRajuste());
			novoFecho.setNumero(ultimoFecho.getNumero().intValue() + 1);

			novoFecho.setDataReajuste(new Date(System.currentTimeMillis()));

			novoFecho.setPercentualBdiBeneficios(reajuste.getPercentualBdiBeneficios());
			novoFecho.setPercentualBdiDespesas(reajuste.getPercentualBdiDespesas());
			novoFecho.setPercentualImposto(reajuste.getPercentualImposto());
			novoFecho.setPercentualEncargosSociais(reajuste.getPercentualEncargosSociais());

			novoFecho.setPercentualReajusteGastosGerais(reajuste.getPercentualReajusteGastosGerais());
			novoFecho.setPercentualReajusteMaoDeObra(reajuste.getPercentualReajusteMaoDeObra());
			novoFecho.setPercentualReajusteMateriais(reajuste.getPercentualReajusteMateriais());
			novoFecho.setPercentualReajusteServicos(reajuste.getPercentualReajusteServicos());

			novoFecho.setValorMaoObra(ultimoFecho.getValorMaoObra());
			novoFecho.setValorGastosGerais(ultimoFecho.getValorGastosGerais());
			novoFecho.setValorMaterial(ultimoFecho.getValorMaterial());
			novoFecho.setValorServicoTerceiro(ultimoFecho.getValorServicoTerceiro());

			List<FechoMaoDeObra> listFechoMaoDeObras = new ArrayList<>();
			for (FechoMaoDeObra fechoMaoDeObra : reajuste.getListaFechosMaoDeObra()) {
				FechoMaoDeObra deObra = fechoMaoDeObra.clone();

				// SE % MÃO DE OBRA > 0, ENTÃO APLICAR O % DE REAJUSTE NOS
				// VALORES UNITÁRIOS DOS GRIDS "MÃO DE OBRA" E "ADICIONAIS"
				if (ObjectUtil.isNotNull(reajuste.getPercentualReajusteMaoDeObra()) && reajuste.getPercentualReajusteMaoDeObra().doubleValue() > 0.0d) {
					double valorUnitario = deObra.getValorUnitario().doubleValue();
					double perc = reajuste.getPercentualReajusteMaoDeObra().doubleValue() / 100;
					double valorPerc = valorUnitario * perc;
					deObra.setValorUnitario(new BigDecimal(deObra.getValorUnitario().doubleValue() + valorPerc));
				}
				deObra.setFecho(novoFecho);

				// Cria clones da lista de FechoMaoDeObraAdicional. E adiciona a lista ao clone do FechoMaoDeObra
				if (fechoMaoDeObra.getListaMaoDeObraAdicional() != null && fechoMaoDeObra.getListaMaoDeObraAdicional().size() > 0) {
					List<FechoMaoDeObraAdicional> listaFechoMaoDeObraAdicional = new ArrayList<>();

					for (FechoMaoDeObraAdicional fechoMaoDeObraAdicional : fechoMaoDeObra.getListaMaoDeObraAdicional()) {
						FechoMaoDeObraAdicional deObraAdicional = fechoMaoDeObraAdicional.clone();
						deObraAdicional.setFechoMaoDeObra(deObra);

						if (deObraAdicional.getValorUnitario() == null) {
							deObraAdicional.setValorUnitario(BigDecimal.ZERO);
						}

						listaFechoMaoDeObraAdicional.add(deObraAdicional);
					}

					deObra.setListaMaoDeObraAdicional(listaFechoMaoDeObraAdicional);
				}

				listFechoMaoDeObras.add(deObra);
			}

			novoFecho.setListaFechosMaoDeObra(listFechoMaoDeObras);

			List<FechoDespesaGeral> listaFechosDespesasGerais = new ArrayList<>();
			for (FechoDespesaGeral fechoDespesaGeral : reajuste.getListaFechosDespesasGerais()) {
				FechoDespesaGeral fechoDespAux = fechoDespesaGeral.clone();

				// SE % GASTOS GERAIS > 0, ENTÃO APLICAR O % DE REAJUSTE NOS
				// VALORES UNITÁRIOS DO GRID "GASTOS GERAIS" (TIPO = 0)
				if (FechoDespesaGeral.TipoDespesaGeral.GASTO_GERAL.equals(fechoDespAux.getTipoDespesaGeral())) {
					if (ObjectUtil.isNotNull(reajuste.getPercentualReajusteGastosGerais()) && reajuste.getPercentualReajusteGastosGerais().doubleValue() > 0.0d) {
						double valorUnitario = fechoDespAux.getValorUnitario().doubleValue();
						double perc = reajuste.getPercentualReajusteGastosGerais().doubleValue() / 100;
						double valorPerc = valorUnitario * perc;
						fechoDespAux.setValorUnitario(new BigDecimal(fechoDespAux.getValorUnitario().doubleValue() + valorPerc));
					}
					// SE % MATERIAIS > 0, ENTÃO APLICAR O % DE REAJUSTE NOS
					// VALORES UNITÁRIOS DO GRID "MATERIAIS" (TIPO = 1)
				} else if (FechoDespesaGeral.TipoDespesaGeral.MATERIAL.equals(fechoDespAux.getTipoDespesaGeral())) {
					if (ObjectUtil.isNotNull(reajuste.getPercentualReajusteMateriais()) && reajuste.getPercentualReajusteMateriais().doubleValue() > 0.0d) {
						double valorUnitario = fechoDespAux.getValorUnitario().doubleValue();
						double perc = reajuste.getPercentualReajusteMateriais().doubleValue() / 100;
						double valorPerc = valorUnitario * perc;
						fechoDespAux.setValorUnitario(new BigDecimal(fechoDespAux.getValorUnitario().doubleValue() + valorPerc));
					}
					// SE % SERVIÇOS TERCEIROS > 0, ENTÃO APLICAR O % DE
					// REAJUSTE NOS VALORES UNITÁRIOS DO GRID
					// "SERVIÇOS TERCEIROS" (TIPO = 2)
				} else if (FechoDespesaGeral.TipoDespesaGeral.SERVICO.equals(fechoDespAux.getTipoDespesaGeral())) {
					if (ObjectUtil.isNotNull(reajuste.getPercentualReajusteServicos()) && reajuste.getPercentualReajusteServicos().doubleValue() > 0.0d) {
						double valorUnitario = fechoDespAux.getValorUnitario().doubleValue();
						double perc = reajuste.getPercentualReajusteServicos().doubleValue() / 100;
						double valorPerc = valorUnitario * perc;
						fechoDespAux.setValorUnitario(new BigDecimal(fechoDespAux.getValorUnitario().doubleValue() + valorPerc));
					}
				}
				fechoDespAux.setFecho(novoFecho);
				listaFechosDespesasGerais.add(fechoDespAux);
			}
			novoFecho.setListaFechosDespesasGerais(listaFechosDespesasGerais);

		} catch (Exception e) {
			throw new Exception("Erro ao criar reajuste. " + e.getMessage());
		}

		return novoFecho;
	}

	private Fecho criarFecho(Fecho fecho, Long codigoCentroFinanceiro) {
		Fecho novoFecho = fecho;

		PessoaFisica pessoaFisica = usuarioSecurityService.getUser().getPessoaFisica();
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(pessoaFisica.getCodigo());

		novoFecho.setResponsavelReajuste(colaborador);
		novoFecho.setCriador(pessoaFisica.getUsuario().getLogin());
		novoFecho.setDataCriacao(new Date(System.currentTimeMillis()));

		novoFecho.setIndiceRajuste("Fecho inicial");
		novoFecho.setNumero(1);

		novoFecho.setDataReajuste(new Date(System.currentTimeMillis()));

		CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
		centroFinanceiro.setCodigo(codigoCentroFinanceiro);
		novoFecho.setCentroFinanceiro(centroFinanceiro);

		return novoFecho;
	}

	@Override
	@Transactional
	public void criarReajuste(FechoTO fechoTO) {
		Fecho fecho = getMapper().map(fechoTO, Fecho.class);
		try {
			Fecho fechoReajuste = criarReajuste(fecho);
			getRepository().save(fechoReajuste);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Neste método é criado um novo fecho. Mas para criar o fecho é preciso remover as listas de FechoMaoDeObra e FechoDespesaGeral.
	 * E depois salvar cada um dos itens dessas listas separado, depois que já tiver um fecho salvo (com código).
	 * @param fechoTO
	 * @param codigoCentroFinanceiro
	 */
	@Override
	@Transactional
	public void criarFecho(FechoTO fechoTO, Long codigoCentroFinanceiro) throws ServiceException {
		Fecho fecho = getMapper().map(fechoTO, Fecho.class);
		try {
			FechoTO ultimoFecho = getUltimoFechoByCodCentroFinanceiro(codigoCentroFinanceiro);

			Fecho fechoAjustado = criarFecho(fecho, codigoCentroFinanceiro);

			if (ultimoFecho != null && ultimoFecho.getNumero() != null) {
				fechoAjustado.setNumero(ultimoFecho.getNumero() + 1);
			}

			List<FechoMaoDeObra> listaFechoMaoDeObra = copiarListaFechoMaoDeObra(fechoAjustado.getListaFechosMaoDeObra());
			fechoAjustado.setListaFechosMaoDeObra(null);

			List<FechoDespesaGeral> listaFechoDespesaGeral = copiarListaFechoDespesaGeral(fechoAjustado.getListaFechosDespesasGerais());
			fechoAjustado.setListaFechosDespesasGerais(null);

			Fecho fechoSalvo = getRepository().save(fechoAjustado);

			salvarFechoMaoDeObra(listaFechoMaoDeObra, fechoSalvo);
			salvarFechoDespesaGeral(listaFechoDespesaGeral, fechoSalvo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	private List<FechoMaoDeObra> copiarListaFechoMaoDeObra(List<FechoMaoDeObra> lista) {
		List<FechoMaoDeObra> novaLista = new ArrayList<>();

		if (lista != null && lista.size() > 0) {
			for (FechoMaoDeObra fechoMaoDeObra : lista) {
				FechoMaoDeObra clone = fechoMaoDeObra.clone();

				clone.setListaMaoDeObraAdicional(copiarFechoMaoDeObraAdicional(fechoMaoDeObra.getListaMaoDeObraAdicional()));

				novaLista.add(clone);
			}
		}

		return novaLista;
	}

	@Transactional
	private void salvarFechoMaoDeObra (List<FechoMaoDeObra> lista, Fecho fecho) {
		if (lista != null && lista.size() > 0) {
			for (FechoMaoDeObra fechoMaoDeObra : lista) {
				List<FechoMaoDeObraAdicional> listaClone = copiarFechoMaoDeObraAdicional(fechoMaoDeObra.getListaMaoDeObraAdicional());
				fechoMaoDeObra.setListaMaoDeObraAdicional(null);

				fechoMaoDeObra.setFecho(fecho);
				FechoMaoDeObra fechoMaoDeObraSalvo = fechoMaoDeObraRepository.save(fechoMaoDeObra);

				salvarFechoMaoDeObraAdicional(listaClone, fechoMaoDeObraSalvo);
			}
		}
	}


	private List<FechoMaoDeObraAdicional> copiarFechoMaoDeObraAdicional(List<FechoMaoDeObraAdicional> lista) {
		List<FechoMaoDeObraAdicional> novaLista = new ArrayList<>();

		if (lista != null && lista.size() > 0) {
			for (FechoMaoDeObraAdicional fechoMaoDeObra : lista) {
				FechoMaoDeObraAdicional clone = fechoMaoDeObra.clone();
				novaLista.add(clone);
			}
		}

		return novaLista;
	}

	@Transactional
	private void salvarFechoMaoDeObraAdicional (List<FechoMaoDeObraAdicional> lista, FechoMaoDeObra fechoMaoDeObra) {
		if (lista != null && lista.size() > 0) {
			for (FechoMaoDeObraAdicional fechoAdicional : lista) {
				fechoAdicional.setFechoMaoDeObra(fechoMaoDeObra);
				fechoMaoDeObraAdicionalRepository.save(fechoAdicional);
			}
		}
	}

	private List<FechoDespesaGeral> copiarListaFechoDespesaGeral(List<FechoDespesaGeral> lista) {
		List<FechoDespesaGeral> novaLista = new ArrayList<>();

		if (lista != null && lista.size() > 0) {
			for (FechoDespesaGeral fechoDespesaGeral : lista) {
				FechoDespesaGeral clone = fechoDespesaGeral.clone();
				novaLista.add(clone);
			}
		}

		return novaLista;
	}

	@Transactional
	private void salvarFechoDespesaGeral (List<FechoDespesaGeral> lista, Fecho fecho) {
		if (lista != null && lista.size() > 0) {
			for (FechoDespesaGeral fechoDespesaGeral : lista) {
				fechoDespesaGeral.setFecho(fecho);
				fechoDespesaGeralRepository.save(fechoDespesaGeral);
			}
		}
	}


	@Override
	@Transactional
	public void atualizarFecho(FechoTO fechoTO) {
		Fecho fecho = getMapper().map(fechoTO, Fecho.class);

		getRepository().updatePercentualBdiBeneficios(fecho.getPercentualBdiBeneficios(), fecho.getCodigo());
		getRepository().updatePercentualBdiDespesas(fecho.getPercentualBdiDespesas(), fecho.getCodigo());
		getRepository().updatePercentualImposto(fecho.getPercentualImposto(), fecho.getCodigo());
		getRepository().updatePercentualEncargosSociais(fecho.getPercentualEncargosSociais(), fecho.getCodigo());

		getRepository().updateValorMaoObra(fecho.getValorMaoObra(), fecho.getCodigo());
		getRepository().updateValorGastosGerais(fecho.getValorGastosGerais(), fecho.getCodigo());
		getRepository().updateValorMaterial(fecho.getValorMaterial(), fecho.getCodigo());
		getRepository().updateValorServicoTerceiro(fecho.getValorServicoTerceiro(), fecho.getCodigo());

		/*
		* FECHO DESPESA GERAL - É deletado os itens antigos para gravar novos
		*/
		List<FechoDespesaGeral> listaDespesaGeralNovo = fecho.getListaFechosDespesasGerais();
		List<FechoDespesaGeralTO> listaDespesaGeralAntigo = fechoDespesaGeralService.getListaDespesaByCodFecho(fecho.getCodigo());

		listaDespesaGeralAntigo.stream().forEach(item -> fechoDespesaGeralRepository.deleteById(item.getCodigo()));

		listaDespesaGeralNovo.stream().forEach(item -> {
			item.setFecho(fecho);
			item.setCodigo(null);
			fechoDespesaGeralRepository.save(item);
		});

		/*
		 * FECHO MAO DE OBRA
		 */
		List<FechoMaoDeObra> listaMaoDeObraNovo = fecho.getListaFechosMaoDeObra();
		List<FechoMaoDeObraTO> listaMaoDeObraAntigo = fechoMaoDeObraService.getListaMaoDeObraByCodFecho(fecho.getCodigo());

		List<FechoMaoDeObraTO> listaDel = new ArrayList();
		Set<Long> conjCodMaoObraNovo = new HashSet<>();
		listaMaoDeObraNovo.stream().filter(item -> item.getCodigo() != null).forEach(item -> conjCodMaoObraNovo.add(item.getCodigo()));
		listaMaoDeObraAntigo.stream().filter(item -> !conjCodMaoObraNovo.contains(item.getCodigo())).forEach(item -> listaDel.add(item));

		//NOVO e EDIÇÃO
		listaMaoDeObraNovo.forEach(fechoMaoDeObra -> {
			fechoMaoDeObra.setFecho(fecho);
			fechoMaoDeObra.getListaMaoDeObraAdicional().stream().forEach(fechoMaoDeObraAdicional -> {
				fechoMaoDeObraAdicional.setFechoMaoDeObra(fechoMaoDeObra);

				if (fechoMaoDeObraAdicional.getValorUnitario() == null) {
					fechoMaoDeObraAdicional.setValorUnitario(BigDecimal.ZERO);
				}

			});
			fechoMaoDeObraRepository.save(fechoMaoDeObra);
		});

		//DELETAR
		listaDel.forEach(fechoMaoDeObraTO -> {
			List<FechoMaoDeObraAdicionalTO> listaAdicional = fechoMaoDeObraAdicionalService.buscarListaAdicionalByCodMaoDeObra(fechoMaoDeObraTO.getCodigo());
			listaAdicional.forEach(adicional -> fechoMaoDeObraAdicionalRepository.deleteById(adicional.getCodigo()));
			fechoMaoDeObraRepository.deleteById(fechoMaoDeObraTO.getCodigo());
		});

	}

	@Override
	public BigDecimal custoMensal(Long codFecho) {
		BigDecimal totalMaoDeObra = totalMaoDeObra(codFecho);
		BigDecimal totalGastosGerais = totalGastosGerais(codFecho);
		BigDecimal totalMateriais = totalMateriais(codFecho);
		BigDecimal totalServicosTerceiros = totalServicosTerceiros(codFecho);

		return totalMaoDeObra.add(totalGastosGerais).add(totalMateriais).add(totalServicosTerceiros);
	}

	@Override
	public BigDecimal totalMaoDeObra(Long codFecho) {
		BigDecimal resultado = BigDecimal.ZERO;
		BigDecimal subtotal = BigDecimal.ZERO;

		List<FechoMaoDeObraTO> listFechoMaoDeObraTO = fechoMaoDeObraService.getListaMaoDeObraByCodFecho(codFecho);

		if (listFechoMaoDeObraTO != null && !listFechoMaoDeObraTO.isEmpty()) {
			for (FechoMaoDeObraTO fechoMaoDeObraTO : listFechoMaoDeObraTO) {
				BigDecimal totalAdicionais = fechoMaoDeObraService.totalAdicional(fechoMaoDeObraTO.getCodigo());
				subtotal = subtotal.add(
					(fechoMaoDeObraTO.getQuantidade().multiply(fechoMaoDeObraTO.getValorUnitario().add(totalAdicionais))).setScale(2, BigDecimal.ROUND_HALF_EVEN)
				);
			}

			if (!subtotal.equals(BigDecimal.ZERO)) {
				BigDecimal percentualEncargosSociais = getRepository().getPercentualEncargosSociaisByCodigo(codFecho);

				if (percentualEncargosSociais != null) {
					BigDecimal totalEncargosSociais = subtotal.multiply(percentualEncargosSociais).divide(new BigDecimal("100"), 8, RoundingMode.HALF_EVEN);
					resultado = subtotal.add(totalEncargosSociais.setScale(2, BigDecimal.ROUND_HALF_EVEN));

				} else {
					resultado = subtotal;
				}
			}
		}

		return resultado;
	}

	@Override
	public BigDecimal totalGastosGerais(Long codFecho) {
		return totalDespesaGeralByTipo(codFecho, FechoDespesaGeral.TipoDespesaGeral.GASTO_GERAL);
	}

	@Override
	public BigDecimal totalMateriais(Long codFecho) {
		return totalDespesaGeralByTipo(codFecho, FechoDespesaGeral.TipoDespesaGeral.MATERIAL);
	}

	@Override
	public BigDecimal totalServicosTerceiros(Long codFecho) {
		return totalDespesaGeralByTipo(codFecho, FechoDespesaGeral.TipoDespesaGeral.SERVICO);
	}

	private BigDecimal totalDespesaGeralByTipo(Long codFecho, FechoDespesaGeral.TipoDespesaGeral tipoDespesaGeral ) {
		List<FechoDespesaGeralTO> listDespesaGeralTO = fechoDespesaGeralService.getListaDespesaByCodFecho(codFecho);

		BigDecimal resultado = BigDecimal.ZERO;
		if (listDespesaGeralTO != null && !listDespesaGeralTO.isEmpty()) {
			resultado = listDespesaGeralTO.stream()
				.filter(item -> item.getTipoDespesaGeral().equals(tipoDespesaGeral.getNome()) )
				.map(FechoDespesaGeralTO::getValorTotalItem)
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		}

		return resultado;
	}

	@Override
	public BigDecimal percentualBDI(Long codFecho) {
		BigDecimal percentualBdiBeneficios = getRepository().getPercentualBdiBeneficiosByCodigo(codFecho);
		BigDecimal percentualBdiDespesas = getRepository().getPercentualBdiDespesasByCodigo(codFecho);

		if (percentualBdiBeneficios == null) {
			percentualBdiBeneficios = BigDecimal.ZERO;
		}

		if (percentualBdiDespesas == null) {
			percentualBdiDespesas = BigDecimal.ZERO;
		}

		if (BigDecimal.ZERO.equals(percentualBdiBeneficios) && BigDecimal.ZERO.equals(percentualBdiDespesas)) {
			return BigDecimal.ZERO;
		} else {
			return percentualBdiBeneficios.add(percentualBdiDespesas).divide(new BigDecimal("100"), 8, RoundingMode.HALF_EVEN);
		}
	}

	@Override
	public BigDecimal valorBDI(Long codFecho) {
		BigDecimal custoMensal = custoMensal(codFecho);
		BigDecimal percentualBdiBeneficios = getRepository().getPercentualBdiBeneficiosByCodigo(codFecho);
		percentualBdiBeneficios = percentualBdiBeneficios == null ? BigDecimal.ZERO : percentualBdiBeneficios;

		BigDecimal percentualBdiDespesas = getRepository().getPercentualBdiDespesasByCodigo(codFecho);
		percentualBdiDespesas = percentualBdiDespesas == null ? BigDecimal.ZERO : percentualBdiDespesas;


		BigDecimal dividendoBdiBeneficios = custoMensal.multiply(percentualBdiBeneficios);
		BigDecimal dividendoBdiDespesas = custoMensal.multiply(percentualBdiDespesas);
		BigDecimal divisor = new BigDecimal(100).subtract(percentualBdiBeneficios.add(percentualBdiDespesas));

		return dividendoBdiBeneficios.divide(divisor, 2, RoundingMode.HALF_EVEN).add(dividendoBdiDespesas.divide(divisor, 2, RoundingMode.HALF_EVEN));
	}

	@Override
	public BigDecimal baseDeTributos(Long codFecho) {
		BigDecimal custoMensal = custoMensal(codFecho);
		BigDecimal valorBDI = valorBDI(codFecho);

		return custoMensal.add(valorBDI);
	}

	@Override
	public BigDecimal percentualTributo(Long codFecho) {
		BigDecimal percentualImposto = getRepository().getPercentualImpostoByCodigo(codFecho);

		if (percentualImposto != null) {
			return percentualImposto.divide(new BigDecimal("100"), 8, RoundingMode.HALF_EVEN);
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Override
	public BigDecimal valorDosTributos(Long codFecho) {
		Fecho fecho = getRepository().findById(codFecho).orElse(null);
		if (fecho == null)
			return BigDecimal.ZERO;

		try {
			List<CentroFinanceiroAliquotaTO> listaCfAliquota = centroFinanceiroAliquotaService.getListByCodCentroFinanceiro(String.valueOf(fecho.getCentroFinanceiro().getCodigo()));

			BigDecimal cem = new BigDecimal(100);
			BigDecimal baseTributos = baseDeTributos(codFecho);
			BigDecimal divisor = BigDecimal.ONE.subtract(percentualTributo(codFecho));
			BigDecimal totalTributos = BigDecimal.ZERO;
			for (CentroFinanceiroAliquotaTO to : listaCfAliquota) {
				BigDecimal aliquota = to.getAliquota().getPercentual().divide(cem, 6, BigDecimal.ROUND_HALF_EVEN);
				BigDecimal dividendo = baseTributos.multiply(aliquota);
				totalTributos = totalTributos.add(dividendo.divide(divisor, 2, BigDecimal.ROUND_HALF_EVEN));
			}

			return totalTributos;
		}
		catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
	}

	@Override
	public BigDecimal valorMensal(Long codFecho) {
		BigDecimal custoMensal = custoMensal(codFecho);
		BigDecimal bdi = valorBDI(codFecho);
		BigDecimal valorTributos = valorDosTributos(codFecho);

		BigDecimal total = custoMensal.add(bdi).add(valorTributos);
		return total;
	}

}
