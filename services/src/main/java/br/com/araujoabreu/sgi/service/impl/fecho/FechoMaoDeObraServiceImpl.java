package br.com.araujoabreu.sgi.service.impl.fecho;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaMaoDeObra;
import br.com.araujoabreu.sgi.comercial.domain.FechoMaoDeObra;
import br.com.araujoabreu.sgi.comercial.domain.QCategoriaMaoDeObra;
import br.com.araujoabreu.sgi.comercial.domain.QFechoMaoDeObra;
import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraAdicionalService;
import br.com.araujoabreu.sgi.service.fecho.FechoMaoDeObraService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraAdicionalTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.fecho.FechoMaoDeObraRepository;
import br.com.fasolti.sgi.repository.fecho.FechoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class FechoMaoDeObraServiceImpl extends BaseSearchService<FechoMaoDeObra, FechoMaoDeObraTO, FechoMaoDeObraPesquisaTO, FechoMaoDeObraRepository> implements FechoMaoDeObraService {

	private static final QFechoMaoDeObra qFechoMaoDeObra = QFechoMaoDeObra.fechoMaoDeObra;

	@Autowired
	private FechoMaoDeObraAdicionalService fechoMaoDeObraAdicionalService;

	@Autowired
	private FechoRepository fechoRepository;

	@Autowired
	public FechoMaoDeObraServiceImpl(FechoMaoDeObraRepository repository) {
		super(repository);
	}

	@Override
	public List<FechoMaoDeObraTO> maoDeObraByCodFecho(String id) throws Exception {
		try {
			List<FechoMaoDeObraTO> lista = getListaMaoDeObraByCodFecho(Long.parseLong(id));
			List<FechoMaoDeObraTO> listaTO = new ArrayList<>();

			for (FechoMaoDeObraTO fecho : lista) {
				List<FechoMaoDeObraAdicionalTO> listaAdicionalTO = fechoMaoDeObraAdicionalService.buscarListaAdicionalByCodMaoDeObra(fecho.getCodigo());
				fecho.setListaMaoDeObraAdicional(listaAdicionalTO);

				listaTO.add(fecho);
			}

			return listaTO;

		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	public List<FechoMaoDeObraTO> getListaMaoDeObraByCodFecho(Long codFecho) {
		if (codFecho == null) {
			return null;
		}

		List<FechoMaoDeObra> lista = new ArrayList<>();

		QCategoriaMaoDeObra qCategoriaMaoDeObra = QCategoriaMaoDeObra.categoriaMaoDeObra;

		QBean qBean = Projections.bean(
			FechoMaoDeObra.class,
			qFechoMaoDeObra.codigo,
			qFechoMaoDeObra.quantidade,
			qFechoMaoDeObra.valorUnitario,
			qFechoMaoDeObra.tipoContratacao,
			Projections.bean(CategoriaMaoDeObra.class, qCategoriaMaoDeObra.codigo, qCategoriaMaoDeObra.nome).as("categoriaMaoDeObra")
		);

		JPAQuery<FechoMaoDeObra> jpaQueryFecho = select();

		jpaQueryFecho
			.select(qBean)
			.from(qFechoMaoDeObra).join(qFechoMaoDeObra.categoriaMaoDeObra, qCategoriaMaoDeObra)
			.where(qFechoMaoDeObra.fecho.codigo.eq(codFecho));

		lista = jpaQueryFecho.fetch();

		return map(lista);
	}

	public BigDecimal getTotal(List<FechoMaoDeObraTO> listaFechoMaoDeObra, BigDecimal encargoSocial) {
		BigDecimal total = BigDecimal.ZERO;
		total = total.add(this.getSubTotalMaoDeObra(listaFechoMaoDeObra)).add(getTotalEncargosSociais(listaFechoMaoDeObra, encargoSocial));
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	private BigDecimal getSubTotalMaoDeObra(List<FechoMaoDeObraTO> listaFechoMaoDeObra) {
		BigDecimal total = BigDecimal.ZERO;

		if (listaFechoMaoDeObra != null && !listaFechoMaoDeObra.isEmpty()) {
			for (FechoMaoDeObraTO maoDeObra : listaFechoMaoDeObra) {
				total = total.add(maoDeObra.getValorTotal());
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	private BigDecimal getTotalEncargosSociais(List<FechoMaoDeObraTO> listaFechoMaoDeObra, BigDecimal encargoSocial) {
		BigDecimal total = BigDecimal.ZERO;
		if (encargoSocial != null) {
			total = this.getSubTotalMaoDeObra(listaFechoMaoDeObra).multiply(encargoSocial);
			total = total.divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal totalAdicional(Long codigoFechoMaoDeObra) {
		List<FechoMaoDeObraAdicionalTO> listAdicional = fechoMaoDeObraAdicionalService.buscarListaAdicionalByCodMaoDeObra(codigoFechoMaoDeObra);

		BigDecimal resultado = BigDecimal.ZERO;

		if (listAdicional != null && !listAdicional.isEmpty()) {
			resultado = listAdicional.stream().map(FechoMaoDeObraAdicionalTO::getValorUnitario).reduce(BigDecimal::add).get();
		}

		return resultado;
	}
}
