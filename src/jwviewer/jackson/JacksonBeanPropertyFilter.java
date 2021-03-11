package jwviewer.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class JacksonBeanPropertyFilter extends SimpleBeanPropertyFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		System.out.println();
		return super.include(writer);
	}

	@Override
	protected boolean include(PropertyWriter writer) {
		System.out.println();
		return super.include(writer);
	}

	@Override
	protected boolean includeElement(Object elementValue) {
		System.out.println();
		return super.includeElement(elementValue);
	}
}
