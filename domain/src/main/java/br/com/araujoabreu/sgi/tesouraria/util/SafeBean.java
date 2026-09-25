package br.com.araujoabreu.sgi.tesouraria.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SafeBean {

	private static final Log LOGGER = LogFactory.getLog(SafeBean.class);

	private Object bean = "";

	private SafeBean(Object bean) {
		this.bean = bean;
	}

	public static SafeBean of(Object bean) {
		return new SafeBean(bean);
	}

	private static SafeBean doGet(String fieldname, SafeBean safeBean) {
		if (safeBean != null) {
			Object bean = safeBean.get();
			try {
				String methodName = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1, fieldname.length());
				Method method = bean.getClass().getMethod(methodName);
				method.setAccessible(true);
				Object obj = method.invoke(bean);

				if (obj == null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Setando nulo para o campo " + fieldname);
					}
					return new SafeBean("");
				}

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Conseguiu pegar valor para o campo " + fieldname);
				}
				return new SafeBean(obj);
			} catch (Throwable e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Falha ao pegar campo " + fieldname);
					LOGGER.error("Mensagem: " + e.getMessage());
				}
				LOGGER.error(e);
			}
		}

		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn("Retornando nulo para o campo " + fieldname);
		}
		return new SafeBean("");
	}

	public SafeBean get(String name) {
		String[] fieldnames = name.split("\\.");
		SafeBean newSafeBean = new SafeBean(bean);
		for (String fieldname : fieldnames) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Tentando pegar valor para o campo " + fieldname);
			}
			newSafeBean = doGet(fieldname, newSafeBean);
		}
		return newSafeBean;
	}

	public SafeBean get(int index) {
		if (bean instanceof List<?>) {
			List<?> list = (List) bean;
			Object extracted = list.get(index);
			return new SafeBean(extracted);
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn("Retornando nulo para um item de coleção com indexador " + index);
		}
		return new SafeBean("");
	}

	public Object get() {
		return bean;
	}

	public <T> T castFor(Class<T> theClass) {
		return theClass.cast(get());
	}

	@SuppressWarnings("unchecked")
	public <T> T cast() {
		return (T) get();
	}

	@Override
	public String toString() {
		return get().toString().trim();
	}

	public Date toDate() {
		return (Date) get();
	}

	public BigDecimal toBigDecimal() {
		return (BigDecimal) get();
	}

}
