package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * <input players="2" control="joy8way" buttons="2" coins="2"/>
 * @author Pierre Smeyers
 */
public class Input {
	@XmlAttribute(required=true)
	private int players;

	@XmlAttribute
	private String control;

	@XmlAttribute
	private int buttons;

	@XmlAttribute
	private int coins;
	
	@XmlAttribute
	private YesNo service = YesNo.no;
	
	@XmlAttribute
	private YesNo tilt = YesNo.no;
	
	@Override
	public String toString() {
		return "    <input players='"+players+"' control='"+control+"' buttons='"+buttons+"' coins='"+coins+"' service='"+service+"' tilt='"+tilt+"'/>";
	}

	public int getPlayers() {
		return players;
	}

	public String getControl() {
		return control;
	}

	public int getButtons() {
		return buttons;
	}

	public int getCoins() {
		return coins;
	}

	public YesNo getService() {
		return service;
	}

	public YesNo getTilt() {
		return tilt;
	}

}
