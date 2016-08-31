package crud.crud_core.utils.validations.notblank;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * Aspect to control empty or whites
 * 
 * @author fjblesa
 *
 */

public class NotBlankValidator implements ConstraintValidator<NotBlank, Object> {

    private final static String VACIO = "";

    @Override
    public void initialize(NotBlank notBlank) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        boolean isValid = true;
        if (value == null)
            return isValid;
        if (value instanceof Collection<?>)
        {
            Collection<?> list = (Collection<?>) value;
            if (list.isEmpty())
                isValid = false;
        }

        if (value.toString().trim().equals(VACIO))
            isValid = false;
        return isValid;
    }
}
