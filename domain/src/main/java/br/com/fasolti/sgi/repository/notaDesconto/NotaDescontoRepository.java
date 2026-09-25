package br.com.fasolti.sgi.repository.notaDesconto;

import br.com.araujoabreu.sgi.controleFiscal.domain.NotaDesconto;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaDescontoRepository extends BaseQueryDslRepository<NotaDesconto, Long> {
}
