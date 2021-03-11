package jwviewer.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JacksonValueSerializer extends StdSerializer<String> {

	protected JacksonValueSerializer(Class<String> t) {
		super(t);
	}

	public JacksonValueSerializer() {
		super(String.class);
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		System.out.println("ser.value:" + value);
	}

//	@Override
//	public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
////		FilterProvider filterProvider = provider.getFilterProvider();
////		PropertyFilter propertyFilter = filterProvider.findPropertyFilter("updateTime", value);
////		System.out.println();
////		LocalDateTime localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(d)),
////				ZoneId.systemDefault());
////		String format = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
////		System.out.println(format);
//
//		AnnotationIntrospector introspector = provider.getAnnotationIntrospector();
//		provider.getConfig().System.out.println("ser:" + value);
//		gen.writeString(value);
//	}

}
