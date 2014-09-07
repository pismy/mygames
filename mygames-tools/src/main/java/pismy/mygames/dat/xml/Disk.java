package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Pierre Smeyers
 */
public class Disk {
	@XmlAttribute(required=true)
	private String name;

	@XmlAttribute
	private String crc;

	@XmlAttribute
	private String md5;

	@XmlAttribute
	private String sha1;

	@XmlAttribute
	private String merge;
	
	@XmlAttribute
	private String region;
	
	@XmlAttribute
	private long index;
	
	@XmlAttribute
	private RomStatus status = RomStatus.good;
	
	@Override
	public String toString() {
		return "    <disk name='"+name+"' crc='"+crc+"' sha1='"+sha1+"' region='"+region+"'/>";
	}

	public String getName() {
		return name;
	}

	public String getMerge() {
		return merge;
	}

	public String getCrc() {
		return crc;
	}

	public String getSha1() {
		return sha1;
	}

	public String getRegion() {
		return region;
	}

}
