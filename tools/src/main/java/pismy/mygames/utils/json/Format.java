package pismy.mygames.utils.json;

/**
 * Enumeration that defines how to serialize a (Java) value into JSON
 * 
 * @author Pierre Smeyers
 * 
 */
public enum Format {
	/**
	 * The auto(matic) format is dependent on the Java object type:
	 * <ul>
	 * <li>java.lang.String is serialized to a JavaScript String,
	 * <li>boolean and java.lang.Boolean are serialized to a JavaScript Boolean,
	 * <li>all numeric primitive types and their associated class are serialized
	 * to a JavaScript Number,
	 * <li>native Java arrays ([]) are serialized into JavaScript Array,
	 * <li>an {@code enum} is serialized into its internal value (Number),
	 * <li>java.util.Collection (and subclasses such as java.util.List) are
	 * serialized into JavaScript Array,
	 * <li>java.util.Map (and subclasses) are serialized into a JavaScript
	 * associative Object,
	 * </ul>
	 */
	AUTO,
	/**
	 * The value is serialized as String:
	 * <ul>
	 * <li>For an array or a java.util.Collection (and subclasses): applies the
	 * format to the array elements,
	 * <li>For java.util.Map (and subclasses): applies the format to the map
	 * values,
	 * <li>For a Date: "2010-11-05T08:53:41.293Z",
	 * <li>For an Enum: the enum name,
	 * <li>For other types: object.toString().
	 * </ul>
	 */
	STRING,
	/**
	 * Reserved for Date: serialized as the absolute timestamp
	 */
	TIMESTAMP,
	/**
	 * Reserved for Date: serialized as 'new Date(time)'
	 * Warning: this will only work with eval(...) method
	 */
	NEWDATE
}
