package crud.crud_core.utils.validations.notblank;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import crud.crud_core.utils.validations.notblank.NotBlankValidator;

public class TestNotBlankAspect {

    /*
     * Test para probar el aspecto de no blancos
     */

    @InjectMocks
    private NotBlankValidator notBlank;
    @Mock
    private List<?> list;
    @Mock
    private ConstraintValidatorContext context;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void notBlankParameterString() throws Throwable {

        String testString = "Prueba con valor";
        /* Llamamos al aspecto con una cadena rellena */
        Assert.assertTrue(notBlank.isValid(testString, context));
    }

    @Test
    public void blankParameterString() throws Throwable {
        String testString = "";
        /* Llamamos al aspecto con una cadena vacia */
        Assert.assertFalse(notBlank.isValid(testString, context));
    }

    @Test
    public void fiveblanksParameterString() throws Throwable {
        String testString = "     ";
        /* Llamamos al aspecto con una cadena rellena con 5 blancos */
        Assert.assertFalse(notBlank.isValid(testString, context));
    }

    @Test
    public void notBlankParameterCollection() throws Throwable {
        /* Llamamos al aspecto con una lista rellena */
        Assert.assertTrue(notBlank.isValid(list, context));
    }

    @Test
    public void blankParameterCollection() throws Throwable {
        list = new ArrayList<>();
        /* Llamamos al aspecto con una lista vacía */
        Assert.assertFalse(notBlank.isValid(list, context));
    }

    @Test
    public void nullParameterCollection() throws Throwable {
        /* Llamamos al aspecto con un parámetro a null */
        Assert.assertTrue(notBlank.isValid(null, context));
    }
}
