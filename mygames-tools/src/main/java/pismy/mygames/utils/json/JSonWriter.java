package pismy.mygames.utils.json;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

/**
 * Helper class to serialize a Java object model into JSon
 * 
 * @author Pierre Smeyers
 */
public class JSonWriter {
	public static enum Mode {
		/**
		 * Standard JSON mode
		 */
		STANDARD,
		/**
		 * Quirks JSON mode:
		 * - skips double quotes on attributes if not required,
		 * - skips null objects,
		 * - by default Dates are serialized as "new Date(time)".
		 */
		QUIRKS
	};

	private static final long MAX_LONG_NUMBER = 9007199254740992l;
	private static final long MIN_LONG_NUMBER = -9007199254740992l;

	// private boolean niceJs;
	private int objDepth = 0;

	private Mode mode = Mode.STANDARD;
	private Format defaultDateFormat = null; // means auto

	private PrintWriter writer;
	private HashMap<Class<?>, JSonSerializer<?>> class2Writer = new HashMap<Class<?>, JSonSerializer<?>>();

	// "2010-11-05T08:53:41.293Z"
	private static SimpleDateFormat ABS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.S'Z'");
	static {
		ABS_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private class JSONObjectWriterImpl implements JSonObjectWriter {
		private int attrIndex = 0;

		public void writeAttr(String name, Object obj) {
			writeAttr(name, obj, Format.AUTO);
		}

		public void writeAttr(String name, Object obj, Format format) {
			if (obj == null && mode == Mode.QUIRKS)
				// skip attribute (in STANDARD mode, the 'null' value is
				// written)
				return;

			// --- write attribute separator and increment attribute index
			if (attrIndex > 0)
				writer.write(",");
			attrIndex++;

			// --- write the attribute name
			if (mode == Mode.STANDARD) {
				// --- attribute shall be written with double quotes
				writeObj(name);
			} else {
				// --- double quotes only if required (need escaping)
				boolean escaped = false;
				for (int i = 0; i < name.length(); i++) {
					char c = name.charAt(i);
					if (!escaped) {
						switch (c) {
						case '\\':
						case '\"':
						case '\n':
						case '\r':
						case '\t':
						case ' ':
						case '-':
						case '+':
						case '/':
							escaped = true;
							// --- write attribute so far with double quotes
							writer.write("\"");
							if (i > 0)
								writer.write(name.substring(0, i));
							break;
						}
					}
					if (escaped) {
						switch (c) {
						case '\\':
							writer.write("\\\\");
							break;
						case '\"':
							writer.write("\\\"");
							break;
						case '\n':
							writer.write("\\n");
							break;
						case '\r':
							// ignore
							break;
						case '\t':
							writer.write("\\t");
							break;
						default:
							writer.write(c);
							break;
						}
					}
				}
				if (escaped)
					// close string
					writer.write("\"");
				else
					// --- write attribute as is
					writer.write(name);
			}

			writer.write(":");

			writeObj(obj, format);
		};
	};

	/**
	 * Creates a JSon Writer
	 * 
	 * @param writer
	 *            the writer to write to
	 */
	public JSonWriter(PrintWriter writer) {
		this.writer = writer;
	}

	/**
	 * Returns the serialization mode
	 * 
	 * @return
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * Sets the serialization mode
	 * 
	 * @param mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * Returns the default Date serialization format
	 * 
	 * @return
	 */
	public Format getDefaultDateFormat() {
		return defaultDateFormat;
	}

	/**
	 * Sets the default Date serialization format
	 * 
	 * @param dateFormat
	 */
	public void setDateFormat(Format defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	/**
	 * Registers a serializer for a given class.
	 * 
	 * @param <T>
	 * @param c
	 *            the class
	 * @param serializer
	 *            the serializer
	 */
	public <T> void registerSerializer(Class<T> c, JSonSerializer<T> serializer) {
		class2Writer.put(c, serializer);
	}

	/**
	 * Serializes an object, using the {@link Format#AUTO} format.
	 */
	public void writeObj(Object o) {
		writeObj(o, Format.AUTO);
	}

	/**
	 * Serializes an object.
	 * 
	 * @param o
	 *            the object
	 * @param format
	 *            the required output format
	 */
	public void writeObj(Object o, Format format) {
		if (o == null) {
			writer.write("null");
			return;
		} else if (o.getClass().isArray()) {
			int l = Array.getLength(o);
			writer.write("[");
			objDepth++;
			for (int i = 0; i < l; i++) {
				if (i > 0)
					writer.write(",");
				// the format defines array elements
				writeObj(Array.get(o, i), format);
			}
			objDepth--;
			writer.write("]");
			return;
		} else if (o instanceof Collection) {
			Collection<?> coll = (Collection<?>) o;
			Iterator<?> it = coll.iterator();
			writer.write("[");
			objDepth++;
			for (int i = 0; it.hasNext(); i++) {
				if (i > 0)
					writer.write(",");
				// the format defines array elements
				writeObj(it.next(), format);
			}
			objDepth--;
			writer.write("]");
			return;
		} else if (o instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) o;
			writer.write("{");
			objDepth++;
			Iterator<?> it = map.keySet().iterator();
			for (int i = 0; it.hasNext(); i++) {
				if (i > 0)
					writer.write(",");
				Object key = it.next();
				Object val = map.get(key);
				writeObj(key);
				writer.write(":");
				// the format defines map elements
				writeObj(val, format);
			}
			objDepth--;
			writer.write("}");
			return;
		} else if (o instanceof Number) {
			// accepted formats are: DEFAULT or STRING
			if (format == Format.STRING) {
				writer.write('"');
				writer.write(String.valueOf(o));
				writer.write('"');
			} else {
				if (o instanceof Long) {
					long l = ((Long) o).longValue();
					if (l < MIN_LONG_NUMBER || l > MAX_LONG_NUMBER)
						throw new RuntimeException("Long value out of JavaScript Number range: " + l);
				}
				writer.write(String.valueOf(o));
			}
			return;
		} else if (o instanceof Boolean) {
			// accepted formats are: DEFAULT or STRING
			if (format == Format.STRING) {
				writer.write('"');
				writer.write(String.valueOf(o));
				writer.write('"');
			} else {
				writer.write(String.valueOf(o));
			}
			return;
		} else if (o instanceof String) {
			// ignore format: a String is a String !
			String value = (String) o;
			writer.write('"');
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				switch (c) {
				case '\\':
					writer.write("\\\\");
					break;
				case '"':
					writer.write("\\\"");
					break;
				case '\n':
					writer.write("\\n");
					break;
				case '\r':
					// ignore in optimized mode
					if (mode == Mode.STANDARD)
						writer.write("\\r");
					break;
				case '\t':
					writer.write("\\t");
					break;
				case '\b':
					writer.write("\\b");
					break;
				case '\f':
					writer.write("\\f");
					break;
				default: {
					if (mode == Mode.QUIRKS) {
						// --- cross fingers so that the browser uses the
						// --- right charset
						writer.write(c);
					} else {
						// --- encode unicode characters
						if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
							String code = "000" + Integer.toHexString(c);
							writer.write("\\u" + code.substring(code.length() - 4));
						} else {
							writer.write(c);
						}
					}
					break;
				}
				}
			}
			writer.write('"');
			return;
		} else if (o instanceof Enum) {
			// accepted formats are: DEFAULT (int value) or STRING (enum name)
			if (format == Format.STRING) {
				writer.write('"');
				writer.write(String.valueOf(((Enum<?>) o).name()));
				writer.write('"');
			} else {
				writer.write(String.valueOf(((Enum<?>) o).ordinal()));
			}
		} else if (o instanceof Date) {
			Date d = (Date) o;
			if (format == Format.AUTO) {
				// --- use default date format
				format = defaultDateFormat == null ? (mode == Mode.STANDARD ? Format.TIMESTAMP : Format.NEWDATE)
						: defaultDateFormat;
			}
			switch (format) {
			case STRING:
				// --- a date is encoded as: "2010-11-05T08:53:41.293Z"
				writer.write('"');
				writer.write(ABS_DATE_FORMAT.format(d));
				writer.write('"');
				break;
			case NEWDATE:
				// --- encode as 'new Date(time)'
				writer.write("new Date(");
				writer.write(String.valueOf(d.getTime()));
				writer.write(")");
				break;
			case TIMESTAMP:
				writer.write(String.valueOf(d.getTime()));
				break;
			}
			return;
		}

		// --- is there a dedicated serializer for this class?
		JSonSerializer serializer = getSerializer(o.getClass());
		if (serializer != null) {
			JSONObjectWriterImpl objWriter = new JSONObjectWriterImpl();
			writer.write("{");
			objDepth++;
			serializer.serialize(o, objWriter);
			objDepth--;
			writer.write("}");
			return;
		}

		// --- serialize with annotations
		if (o.getClass().isAnnotationPresent(Serializable.class)) {
			JSONObjectWriterImpl objWriter = new JSONObjectWriterImpl();
			writer.write("{");
			objDepth++;
			writeAnnotatedAttributes(o, o.getClass(), objWriter);
			objDepth--;
			writer.write("}");
			return;
		}

		// --- cannot handle this class
		throw new RuntimeException("Unsupported class: " + o.getClass().getName());
	}

	/**
	 * Writes a string to the underlying writer.
	 * 
	 * @param s
	 *            a string to write
	 */
	public void write(String s) {
		writer.write(s);
	}

	/**
	 * Flushes the underlying writer.
	 */
	public void flush() {
		writer.flush();
	}

	/**
	 * Closes the underlying writer.
	 */
	public void close() {
		writer.close();
	}

	/**
	 * Looks for any dedicated serializer associated to the given class
	 */
	private JSonSerializer<?> getSerializer(Class<?> clazz) {
		JSonSerializer<?> serializer = class2Writer.get(clazz);
		if (serializer != null)
			return serializer;
		// --- look in interfaces
		Class<?>[] itfs = clazz.getInterfaces();
		if (itfs != null) {
			for (Class<?> itf : itfs) {
				serializer = class2Writer.get(itf);
				if (serializer != null)
					return serializer;
			}
		}
		// --- look into ancestor
		if (clazz != Object.class && clazz.getSuperclass() != null)
			return getSerializer(clazz.getSuperclass());
		return null;
	}

	/**
	 * Writes all class attributes with annotation {@Attribute}
	 */
	private void writeAnnotatedAttributes(Object o, Class<?> c, JSonObjectWriter writer) {
		// --- 1: process declared fields
		for (Field f : o.getClass().getDeclaredFields()) {
			Attribute jsonattr = f.getAnnotation(Attribute.class);
			if (jsonattr == null)
				continue;
			f.setAccessible(true);
			String name = jsonattr.value();
			if ("*".equals(name)) {
				// --- JS attribute name should be the Java field name
				name = f.getName();
			}
			try {
				Object val = f.get(o);
				if (val == null && mode == Mode.QUIRKS)
					// do not write attribute
					continue;
				writer.writeAttr(name, val, jsonattr.format());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Error while serializing field '" + f.getName() + "' in object " + o, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error while serializing field '" + f.getName() + "' in object " + o, e);
			}
		}
		for (Method m : o.getClass().getDeclaredMethods()) {
			Attribute jsonattr = m.getAnnotation(Attribute.class);
			if (jsonattr == null)
				continue;
			m.setAccessible(true);
			if (m.getParameterTypes().length > 0)
				throw new RuntimeException("Error: attribute method '" + m.getName()
						+ "' is not a getter (has params) in object " + o);
			// --- method has to start wth "get" or "is" (or boolean value)
			String pptyName = null;
			if (m.getName().startsWith("get") && m.getName().length() > 3) {
				pptyName = String.valueOf(Character.toLowerCase(m.getName().charAt(3))) + m.getName().substring(4);
			} else if ((m.getReturnType() == Boolean.class || m.getReturnType() == Boolean.TYPE)
					&& m.getName().startsWith("is") && m.getName().length() > 2) {
				pptyName = String.valueOf(Character.toLowerCase(m.getName().charAt(2))) + m.getName().substring(3);
			} else {
				throw new RuntimeException("Error: attribute method '" + m.getName()
						+ "' has to be a getter (ex: getXXX()) in object " + o);
			}
			String name = jsonattr.value();
			if ("*".equals(name)) {
				// --- JS attribute name should be the Java attribute name
				name = pptyName;
			}
			try {
				Object val = m.invoke(o);
				if (val == null && mode == Mode.QUIRKS)
					// do not write attribute
					continue;
				writer.writeAttr(name, val, jsonattr.format());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Error while serializing attribute from method '" + m.getName()
						+ "' in object " + o, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error while serializing attribute from method '" + m.getName()
						+ "' in object " + o, e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Error while serializing attribute from method '" + m.getName()
						+ "' in object " + o, e.getTargetException());
			}
		}
		// --- 2: process superclass
		if (c.getSuperclass() != null && c.getSuperclass() != Object.class)
			writeAnnotatedAttributes(o, c.getSuperclass(), writer);
	}
}
