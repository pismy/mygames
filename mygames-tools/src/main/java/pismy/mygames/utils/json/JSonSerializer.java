package pismy.mygames.utils.json;

/**
 * Interface of an object able to serialize a Java object into JSon.
 * 
 * @author Pierre Smeyers
 * 
 * @param <T>
 */
public interface JSonSerializer<T> {
	/**
	 * Main method to serialize an object of the registered type into
	 * JSON.
	 * 
	 * @param object
	 * @param writer
	 */
	void serialize(T object, JSonObjectWriter writer);
}
