package br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class LoteInterfaceNotaTO extends BaseTO {
	private LoteInterfaceTO loteInterface;
	private NotaTO nota;
	private String tipo;
	private Integer ordem;

	public LoteInterfaceTO getLoteInterface() {
		return loteInterface;
	}

	public void setLoteInterface(LoteInterfaceTO loteInterface) {
		this.loteInterface = loteInterface;
	}

	public NotaTO getNota() {
		return nota;
	}

	public void setNota(NotaTO nota) {
		this.nota = nota;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
