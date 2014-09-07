package pismy.mygames.dat.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Pierre Smeyers
 */
public class Header {
	@XmlElement
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private String category;

	@XmlElement
	private String version;

	@XmlElement
	private String date;

	@XmlElement
	private String author;

	@XmlElement
	private String email;

	@XmlElement
	private String homepage;

	@XmlElement
	private String url;

	@XmlElement
	private String comment;

//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer("  <header");
//
//		sb.append(" runnable='" + runnable + "'");
//
//		if (name != null) {
//			sb.append(" name='" + name + "'");
//		}
//		if (sourcefile != null) {
//			sb.append(" sourcefile='" + sourcefile + "'");
//		}
//		if (cloneof != null) {
//			sb.append(" cloneof='" + cloneof + "'");
//		}
//		if (romof != null) {
//			sb.append(" romof='" + romof + "'");
//		}
//		sb.append(">");
//
//		if (description != null) {
//			sb.append("\n    <description>" + description + "</description>");
//		}
//
//		if (year != 0) {
//			sb.append("\n    <year>" + year + "</year>");
//		}
//
//		if (manufacturer != null) {
//			sb.append("\n    <manufacturer>" + manufacturer + "</manufacturer>");
//		}
//
//		for (BiosSet bs : biossets) {
//			sb.append("\n");
//			sb.append(bs);
//		}
//
//		for (Rom r : roms) {
//			sb.append("\n");
//			sb.append(r);
//		}
//
//		for (Chip c : chips) {
//			sb.append("\n");
//			sb.append(c);
//		}
//
//		if (video != null) {
//			sb.append("\n");
//			sb.append(video);
//		}
//
//		if (sound != null) {
//			sb.append("\n");
//			sb.append(sound);
//		}
//
//		if (input != null) {
//			sb.append("\n");
//			sb.append(input);
//		}
//
//		for (DipSwitch ds : dipswitches) {
//			sb.append("\n");
//			sb.append(ds);
//		}
//
//		if (driver != null) {
//			sb.append("\n");
//			sb.append(driver);
//		}
//		sb.append("\n  </game>");
//
//		return sb.toString();
//	}
}
