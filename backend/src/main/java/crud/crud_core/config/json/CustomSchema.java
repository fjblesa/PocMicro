package crud.crud_core.config.json;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitorDecorator;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.validation.AnnotationConstraintResolver;
import com.fasterxml.jackson.module.jsonSchema.validation.ValidationConstraintResolver;

import crud.crud_core.config.json.annotations.IdentityClass;
import crud.crud_core.config.json.annotations.ReadOnly;
import crud.crud_core.entities.WaterUtility;
import crud.crud_core.services.ServiceRegister;

public class CustomSchema extends SchemaFactoryWrapper {

	private ValidationConstraintResolver constraintResolver;

	public CustomSchema() {
		this(new AnnotationConstraintResolver());

	}

	public CustomSchema(ValidationConstraintResolver constraintResolver) {
		super(null, new CustomSchemaFactoryWrapper());
		this.constraintResolver = constraintResolver;

	}

	public CustomSchema(SerializerProvider provider) {
		super(provider);
	}

	@Override
	public JsonStringFormatVisitor expectStringFormat(JavaType convertedType) {
		CustomStringSchema s = new CustomStringSchema();
		this.schema = s;
		return this.visitorFactory.stringFormatVisitor(s);
	}

	@Override
	public JsonBooleanFormatVisitor expectBooleanFormat(JavaType convertedType) {
		CustomBooleanSchema s = new CustomBooleanSchema();
		this.schema = s;
		return this.visitorFactory.booleanFormatVisitor(s);
	}

	@Override
	public JsonNumberFormatVisitor expectNumberFormat(JavaType convertedType) {
		CustomNumberSchema s = new CustomNumberSchema();
		this.schema = s;
		return this.visitorFactory.numberFormatVisitor(s);
	}

	@Override
	public JsonObjectFormatVisitor expectObjectFormat(JavaType convertedType) {
		return new ObjectVisitorDecorator(
				(ObjectVisitor) super.expectObjectFormat(convertedType)) {
			private JsonSchema getPropertySchema(BeanProperty writer) {

				return ((ObjectSchema) getSchema()).getProperties().get(
						writer.getName());
			}

			@Override
			public void optionalProperty(BeanProperty writer)
					throws JsonMappingException {

				super.optionalProperty(writer);
				addValidationConstraints(getPropertySchema(writer), writer);
			}

			@Override
			public void property(BeanProperty writer)
					throws JsonMappingException {
				super.property(writer);
				JsonSchema schemaObject = getPropertySchema(writer);
				addValidationConstraints(schemaObject, writer);

			}
		};
	}

	protected JsonSchema addValidationConstraints(JsonSchema schema,
			BeanProperty prop) {
		if (schema.isArraySchema()) {
			addCustomArrayProperty(schema, prop);
		} else if (schema.isNumberSchema()) {
			addCustomNumberProperty(schema, prop);
		} else if (schema.isStringSchema()) {
			addCustomStringProperty(schema, prop);
		} else if (schema.isBooleanSchema()) {
			addCustomBooleanProperty(schema, prop);
		}

		return schema;
	}

	private void addCustomArrayProperty(JsonSchema schema, BeanProperty prop) {
		ArraySchema arraySchema = schema.asArraySchema();
		arraySchema.setMaxItems(constraintResolver.getArrayMaxItems(prop));
		arraySchema.setMinItems(constraintResolver.getArrayMinItems(prop));
	}

	private void addCustomNumberProperty(JsonSchema schema, BeanProperty prop) {
		CustomNumberSchema numberSchema = (CustomNumberSchema) schema
				.asNumberSchema();
		String views = addViews(prop);
		numberSchema.setViews(views);
		numberSchema
				.setReadonly(prop.getMember().hasAnnotation(ReadOnly.class));
		numberSchema.setMaximum(constraintResolver.getNumberMaximum(prop));
		numberSchema.setMinimum(constraintResolver.getNumberMinimum(prop));
		String defaultValue = addDefaultValue(prop);
		numberSchema.setDefault(defaultValue);
	}

	private void addCustomBooleanProperty(JsonSchema schema, BeanProperty prop) {
		CustomBooleanSchema booleanSchema = (CustomBooleanSchema) schema
				.asBooleanSchema();
		String views = addViews(prop);
		booleanSchema.setViews(views);
		booleanSchema.setReadonly(prop.getMember()
				.hasAnnotation(ReadOnly.class));
		String defaultValue = addDefaultValue(prop);
		booleanSchema.setDefault(defaultValue);
	}

	private void addCustomStringProperty(JsonSchema schema, BeanProperty prop) {
		CustomStringSchema stringSchema = (CustomStringSchema) schema
				.asStringSchema();
		// si existe anotación con entidad añadimos su schema
		JsonSchema[] extendsSchema = addExtendsSchema(schema, prop);
		stringSchema.setExtends(extendsSchema);
		String views = addViews(prop);
		stringSchema.setViews(views);
		stringSchema
				.setReadonly(prop.getMember().hasAnnotation(ReadOnly.class));
		stringSchema.setMaxLength(constraintResolver.getStringMaxLength(prop));
		stringSchema.setMinLength(constraintResolver.getStringMinLength(prop));
		stringSchema.setPattern(constraintResolver.getStringPattern(prop));
		String defaultValue = addDefaultValue(prop);
		stringSchema.setDefault(defaultValue);
	}

	private String addDefaultValue(BeanProperty prop) {
		String defaultValue = "";
		if (prop.getMember().hasAnnotation(JsonProperty.class)) {
			defaultValue = prop.getMember().getAnnotation(JsonProperty.class)
					.defaultValue();
		}

		return defaultValue;
	}

	/**
	 * Function add extends property type entity with its json schema
	 * 
	 * @param schema
	 * @param prop
	 * @return
	 */
	private JsonSchema[] addExtendsSchema(JsonSchema schema, BeanProperty prop) {
		JsonSchema[] listJsonSchema = {};
		if (prop.getMember().hasAnnotation(IdentityClass.class)) {

			IdentityClass annotation = prop.getAnnotation(IdentityClass.class);
			Class<?> identityClass = annotation.value();
			ServiceRegister register = new ServiceRegister();

			String endPointIdentity = register
					.getEndPointRegister(identityClass);
			schema.set$ref(endPointIdentity);
			ParseObject parseObject = ParseObject.getInstance();

			JsonSchema jsonSchema = (JsonSchema) parseObject
					.getJsonSchema(identityClass);

			listJsonSchema = new JsonSchema[] { jsonSchema };
		}

		return listJsonSchema;
	}

	/**
	 * Function search views from property
	 * 
	 * @param prop
	 * @return
	 */
	private String addViews(BeanProperty prop) {
		Set<String> views = new HashSet<String>();
		if (prop.getAnnotation(JsonView.class) != null) {
			Class<?>[] vistas = prop.getAnnotation(JsonView.class).value();
			for (Class<?> class1 : vistas) {
				views.add(class1.getSimpleName());
			}
		}
		String joinedViewsNames = views.stream().collect(
				Collectors.joining(", "));
		return joinedViewsNames;
	}

}
