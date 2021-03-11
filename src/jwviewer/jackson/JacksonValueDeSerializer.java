package jwviewer.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JacksonValueDeSerializer extends StdDeserializer<String> {

	protected JacksonValueDeSerializer(Class<?> vc) {
		super(vc);
	}

	public JacksonValueDeSerializer() {
		super(String.class);
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		System.out.println("de");
		return null;
	}

}
