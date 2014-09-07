package pismy.mygames.dat.xml;

import javax.xml.bind.annotation.XmlAttribute;

import pismy.mygames.dat.IRom;

/**
 * @author Pierre Smeyers
 */
public class Rom implements IRom {
	private String name;

	private String bios;

	private long size;

	private String crc;

	private String md5;

	private String sha1;

	private String merge;
	
	private String region;

	private String offset;
	
	private RomStatus status = RomStatus.good;
	
	private YesNo dispose = YesNo.no;
	
	@Override
	public String toString() {
		return "    <rom name='"+name+"' size='"+size+"' crc='"+crc+"' sha1='"+sha1+"' region='"+region+"' offset='"+offset+"' dispose='"+dispose+"'/>";
	}

	@XmlAttribute
	public YesNo getDispose() {
		return dispose;
	}

	@XmlAttribute(required=true)
	public String getName() {
		return name;
	}

	@XmlAttribute
	public String getMerge() {
		return merge;
	}

	@XmlAttribute(required=true)
	public long getSize() {
		return size;
	}

	@XmlAttribute
	public String getCrc() {
		return crc;
	}
	
	@XmlAttribute
	public String getMd5() {
		return md5;
	}

	@XmlAttribute
	public String getSha1() {
		return sha1;
	}

	@XmlAttribute
	public String getRegion() {
		return region;
	}

	@XmlAttribute
	public String getOffset() {
		return offset;
	}

	@XmlAttribute
	public String getBios() {
		return bios;
	}

	@XmlAttribute
	public RomStatus getStatus() {
		return status;
	}

	public void setStatus(RomStatus status) {
		this.status = status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public void setMerge(String merge) {
		this.merge = merge;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public void setDispose(YesNo dispose) {
		this.dispose = dispose;
	}
	
	public void setBios(String bios) {
		this.bios = bios;
	}

//	@Override
//	public boolean equals(Object obj) {
//		if(obj == null)
//			return false;
//		if(!(obj instanceof IRom))
//			return false;
//		IRom other = (IRom)obj;
//		if (!getName().equals(other.getName()))
//			return false;
//		if(getSize() != other.getSize())
//			return false;
//		// try checksums
//		if(getCrc() != null && other.getCrc() != null && !getCrc().equals(other.getCrc()))
//			return false;
//		if(getMd5() != null && other.getMd5() != null && !getMd5().equals(other.getMd5()))
//			return false;
//		if(getSha1() != null && other.getSha1() != null && !getSha1().equals(other.getSha1()))
//			return false;
//		return true;
//	}
//	@Override
//	public int hashCode() {
//		return name.hashCode() + (int)size;
//	}
}
