package br.com.fasolti.commons.email;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * Processa os templates de e-mail em <code>classpath:/templates</code>. Arquivos <code>.html</code> são processados
 * como HTML e os demais (ex.: <code>.subject</code>) como texto. O modelo é exposto ao template com o nome
 * <code>emailBean</code>.
 */
public class EmailTemplateProcessor {

	public static final String MODEL_VARIABLE = "emailBean";

	private final TemplateEngine engine = new TemplateEngine();

	public EmailTemplateProcessor() {
		engine.addTemplateResolver(resolver(TemplateMode.HTML, "html", 1));
		engine.addTemplateResolver(resolver(TemplateMode.TEXT, null, 2));
	}

	private static ClassLoaderTemplateResolver resolver(TemplateMode mode, String extension, int order) {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setTemplateMode(mode);
		resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resolver.setCacheable(true);
		resolver.setOrder(order);
		if (extension != null) {
			resolver.setResolvablePatterns(java.util.Collections.singleton("*." + extension));
		}
		return resolver;
	}

	public Result process(String templateName, Object model) {
		Context context = new Context();
		context.setVariable(MODEL_VARIABLE, model);
		return new Result(engine.process(templateName, context));
	}

	public static class Result {

		private final String content;

		Result(String content) {
			this.content = content;
		}

		public String getContent() {
			return content;
		}
	}
}
