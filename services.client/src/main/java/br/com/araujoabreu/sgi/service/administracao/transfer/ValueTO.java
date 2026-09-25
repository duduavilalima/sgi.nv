package br.com.araujoabreu.sgi.service.administracao.transfer;

public class ValueTO {

	private Object value;

	public ValueTO(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static class Builder {

		// requerido
		private Object value;

		public Builder() {
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder value(Object value) {
			this.value = value;
			return this;
		}

		public ValueTO build() {
			return new ValueTO(this);
		}
	}

	private ValueTO(Builder builder) {
		this.value = builder.value;
	}
}
