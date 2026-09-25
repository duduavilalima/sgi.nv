package br.com.fasolti.sgi.repository.telefone;

import br.com.fasolti.corporativo.pessoa.domain.Operadora;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadoraRepository extends BaseQueryDslRepository<Operadora, Long> {
}
