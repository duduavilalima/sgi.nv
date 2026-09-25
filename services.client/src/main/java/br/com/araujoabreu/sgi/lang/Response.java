package br.com.araujoabreu.sgi.lang;

import java.util.List;

public class Response {

	private List<String> warnings;

	public static class Builder {

		private List<String> warnings = null;

		public Builder warnings(List<String> warnings) {
			this.warnings = warnings;
			return this;
		}

		public Response build() {
			return new Response(this);
		}
	}

	private Response(Builder builder) {
		warnings = builder.warnings;
	}

	public List<String> getWarnings() {
		return warnings;
	}
}
