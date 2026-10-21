package br.com.fasolti.commons.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class LoggerPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

		final Logger logger = LoggerFactory.getLogger(bean.getClass());

		ReflectionUtils.doWithFields(bean.getClass(),
			new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					ReflectionUtils.makeAccessible(field);
					ReflectionUtils.setField(field, bean, logger);
				}
			},
			new ReflectionUtils.FieldFilter() {
				@Override
				public boolean matches(Field field) {
					return field.isAnnotationPresent(Autowired.class) && Logger.class.equals(field.getType());
				}
			}
		);

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
		return bean;
	}
}
