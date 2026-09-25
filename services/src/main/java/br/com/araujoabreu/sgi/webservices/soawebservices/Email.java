package br.com.araujoabreu.sgi.webservices.soawebservices;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Email extends Serialization implements java.io.Serializable {


	private static final long serialVersionUID = -8889370991907974828L;
	private String email;

	public Email() {
	}

	public Email(String email) {
		this.email = email;
	}

	@JsonProperty("Email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Email [email=" + email + "]";
	}

}
