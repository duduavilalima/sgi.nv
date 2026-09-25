package br.com.fasolti.sgi.repository.tesouraria.enumerations;

/**
 * Status de uma impressão.
 */
public enum PrintStatus {

	LIBERADO("Liberado para impressão"),
	EM_PROCESSO("Em processo de impressão"),
	IMPRESSO("Impresso");

	private final String status;

	private PrintStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
}
