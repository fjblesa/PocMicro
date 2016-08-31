package crud.crud_core.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SerializableStatusEnum extends JsonSerializer<Status>{
	 
	public void serialize(Status value, JsonGenerator generator, 
			SerializerProvider provider) throws IOException, JsonProcessingException {
       generator.writeStartObject();
       generator.writeFieldName("id");
       generator.writeNumber(value.getId());
       generator.writeEndObject();
   }
}

