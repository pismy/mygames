package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Pierre Smeyers
 */
public class Sound {
	@XmlAttribute
	private int channels;

	@Override
	public String toString() {
		return "    <sound channels='"+channels+"'/>";
	}

	public int getChannels() {
		return channels;
	}

}
