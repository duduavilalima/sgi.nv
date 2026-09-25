package br.com.araujoabreu.sgi.service.impl.apoio;

import br.com.araujoabreu.sgi.service.apoio.EnumService;
import br.com.fasolti.commons.Check;
import br.com.fasolti.commons.Util;
import org.springframework.stereotype.Service;

@Service
public class EnumServiceImpl implements EnumService {

	public String[] getEnumClassValues(String pacote, String classe, String nomeEnum) {
		if (Check.isNotEmpty(classe) && !"null".equalsIgnoreCase(classe))
			return Util.getEnumValues(pacote + "." + classe + "$" + nomeEnum);
		else
			return Util.getEnumValues(pacote + "." + nomeEnum);
	}

}
