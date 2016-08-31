package crud.crud_core.config.json.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotation;


@Target({ 
	java.lang.annotation.ElementType.FIELD
	})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface IdentityClass {

	Class<?> value();

}
