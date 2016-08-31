package crud.crud_core.config.json;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.factories.VisitorContext;
import com.fasterxml.jackson.module.jsonSchema.factories.WrapperFactory;

public class CustomSchemaFactoryWrapper extends WrapperFactory {

	@Override
	public SchemaFactoryWrapper getWrapper(SerializerProvider provider,
			VisitorContext rvc) {
		CustomSchema customMapper = new CustomSchema(provider);
		return customMapper.setVisitorContext(rvc);

	}

}
