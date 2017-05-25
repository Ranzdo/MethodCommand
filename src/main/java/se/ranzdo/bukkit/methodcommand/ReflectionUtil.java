package se.ranzdo.bukkit.methodcommand;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getAnnotation(Class<T> clazz, Method method, int parameterIndex) {
		for(Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
			if(annotation.annotationType() == clazz)  {
				return (T) annotation;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getField(Object obj, String fieldName) {
		Class<?> clazz = obj.getClass();
		while(clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				
				field.setAccessible(true);
				
				return (T) field.get(obj);
			}
			catch(Throwable ignore) {}
		}
		
		return null;
	}
}
