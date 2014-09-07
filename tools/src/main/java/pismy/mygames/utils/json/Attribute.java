package pismy.mygames.utils.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a field or method getter as a serializable JSON attribute.
 * 
 * @author Pierre Smeyers
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Attribute {
	/**
	 * Defines the JSON attribute name.
	 * By default the field name (or getter) is used.
	 */
	String value() default "*";

	/**
	 * Defines how the value should be formatted
	 * Default: {@link Format#AUTO}
	 * 
	 * @return
	 */
	Format format() default Format.AUTO;
}
