package br.com.fasolti.commons.lang;

public class DownloadException extends Exception {

	public DownloadException(String message) {
		super(message);
	}

	public DownloadException(String message, Throwable cause) {
		super(message, cause);
	}

	public DownloadException(Throwable cause) {
		super(cause);
	}
}
