package br.com.fasolti.commons.lang;

public class SearchNotAllowedException extends RestException {

	public SearchNotAllowedException() {
		super("Service do not allow searchs");
	}

}
