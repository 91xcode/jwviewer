package jwviewer.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;

public class FieldConvertIntrospector extends AnnotationIntrospector {

	@Override
	public Version version() {
		return Version.unknownVersion();
	}

	@Override
	public Object findSerializer(Annotated am) {
		DateTimeConverter dateTimeConverter = am.getAnnotation(DateTimeConverter.class);
		if (null == dateTimeConverter) {
			return JacksonValueSerializer.class;
		}
		return super.findSerializer(am);
	}

	@Override
	public Object findKeySerializer(Annotated am) {
		return super.findKeySerializer(am);
	}

	@Override
	public Object findDeserializer(Annotated am) {
		return super.findDeserializer(am);
	}
}
