package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Pierre Smeyers
 */
public class DipValue {
	@XmlAttribute(name="default")
	private YesNo dflt = YesNo.no;
	
	@XmlAttribute
	private String name;
	
	@Override
	public String toString() {
		return "      <dipvalue name='"+name+"' default='"+dflt+"'/>";
	}

	public YesNo getDflt() {
		return dflt;
	}

	public String getName() {
		return name;
	}
	
	

}
