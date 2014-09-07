package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Pierre Smeyers
 */
public class BiosSet {
	@XmlAttribute(name = "default")
	private YesNo dflt = YesNo.no;

	@XmlAttribute(required=true)
	private String name;

	@XmlAttribute
	private String description;

	@Override
	public String toString() {
		return "    <biosset name='" + name + "' description='" + description
				+ "' default='" + dflt + "'/>";
	}

	public YesNo getDflt() {
		return dflt;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
