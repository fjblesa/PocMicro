package crud.crud_core.utils.validations.notblank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * Este aspecto gestionará el control de vacíos o blancos en aquellos campos en los que se indique
 * 
 * @author fjblesa
 *
 */
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankValidator.class)
public @interface NotBlank {

    String message() default "Dato Obligatorio";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
