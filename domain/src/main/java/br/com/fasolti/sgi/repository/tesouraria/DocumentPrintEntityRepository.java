package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.DocumentPrintEntity;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentPrintEntityRepository extends BaseQueryDslRepository<DocumentPrintEntity, Long> {

}
