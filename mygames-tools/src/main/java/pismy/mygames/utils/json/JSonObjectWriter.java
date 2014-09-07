package pismy.mygames.utils.json;

/**
 * Interface to serialize object attributes.
 * 
 * @author Pierre Smeyers
 */
public interface JSonObjectWriter {
	/**
	 * Serializes an object attribute, using the {@link Format#AUTO} format.
	 * 
	 * @param name
	 *            attribute name
	 * @param obj
	 *            attribute value
	 */
	void writeAttr(String name, Object obj);

	/**
	 * Serializes an object attribute.
	 * 
	 * @param name
	 *            attribute name
	 * @param obj
	 *            attribute value
	 * @param format
	 *            the expected output format
	 */
	void writeAttr(String name, Object obj, Format format);
}
