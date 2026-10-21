package br.com.fasolti.commons.email;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Email {

	private final List<InternetAddress> from;
	private final List<InternetAddress> to;
	private final String subject;
	private final String htmlBody;
	private final String templateName;
	private final Object templateModel;
	private final List<File> attachments;

	private Email(Builder builder) {
		this.from = Collections.unmodifiableList(new ArrayList<>(builder.from));
		this.to = Collections.unmodifiableList(new ArrayList<>(builder.to));
		this.subject = builder.subject;
		this.htmlBody = builder.htmlBody;
		this.templateName = builder.templateName;
		this.templateModel = builder.templateModel;
		this.attachments = Collections.unmodifiableList(new ArrayList<>(builder.attachments));
	}

	public List<InternetAddress> getFrom() {
		return from;
	}

	public List<InternetAddress> getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public String getTemplateName() {
		return templateName;
	}

	public Object getTemplateModel() {
		return templateModel;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public static class Builder {

		private final List<InternetAddress> from = new ArrayList<>();
		private final List<InternetAddress> to = new ArrayList<>();
		private final List<File> attachments = new ArrayList<>();
		private String subject;
		private String htmlBody;
		private String templateName;
		private Object templateModel;

		public Builder addTo(InternetAddress address) {
			to.add(address);
			return this;
		}

		public Builder addFrom(String address) {
			from.add(InternetAddresses.create(address));
			return this;
		}

		public Builder addFrom(InternetAddress address) {
			from.add(address);
			return this;
		}

		public Builder withSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public Builder withHtmlBody(String htmlBody) {
			this.htmlBody = htmlBody;
			return this;
		}

		public Builder withHtmlTemplate(String templateName, Object model) {
			this.templateName = templateName;
			this.templateModel = model;
			return this;
		}

		public Builder addAttachmentFiles(File... files) {
			if (files != null) {
				Collections.addAll(attachments, files);
			}
			return this;
		}

		public Email build() {
			return new Email(this);
		}

		public Email htmlBody(String htmlBody) {
			return withHtmlBody(htmlBody).build();
		}

		public Email htmlTemplate(String templateName, Object model) {
			return withHtmlTemplate(templateName, model).build();
		}
	}
}
