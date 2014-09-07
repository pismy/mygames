package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Pierre Smeyers
 */
public class Chip {
	@XmlAttribute
	private String name;

	@XmlAttribute
	private String type;

	@XmlAttribute
	private int clock;
	
	@Override
	public String toString() {
		return "    <rom name='"+name+"' type='"+type+"' clock='"+clock+"'/>";
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getClock() {
		return clock;
	}

}
