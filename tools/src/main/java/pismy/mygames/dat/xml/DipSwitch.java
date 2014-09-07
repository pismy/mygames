package pismy.mygames.dat.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Pierre Smeyers
 */
public class DipSwitch {
	@XmlAttribute
	private String name;
	
	@XmlElement(name="dipvalue")
	private List<DipValue> dipvalues = new ArrayList<DipValue>();

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("    <dipswitch name='"+name+"'>");
		
		for(DipValue dv : dipvalues) {
			sb.append("\n");
			sb.append(dv);
		}
		sb.append("\n    </dipswitch>");
		
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public List<DipValue> getDipvalues() {
		return dipvalues;
	}
	
	
}
