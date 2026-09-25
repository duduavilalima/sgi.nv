package br.com.araujoabreu.sgi.service.util;

import java.util.ArrayList;
import java.util.List;

public class MenssagemPadraoTO {
	private List<String> warnings;

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public void addWarnings(String mensagemWarning) {
		if (this.warnings == null) {
			this.warnings = new ArrayList<String>();
		}
		this.warnings.add(mensagemWarning);
	}
}
