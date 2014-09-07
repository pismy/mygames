package pismy.mygames.dat.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import pismy.mygames.dat.IGame;

/**
 * @author Pierre Smeyers
 */
public class Game implements IGame {
	private YesNo runnable = YesNo.yes;

//	@XmlAttribute
//	private YesNo isbios = YesNo.no;

	private String name;

	private String sourcefile;

	private String cloneof;

	private String romof;

	private String sampleof;

	private String category;

	private String description;

	private int year;

	private String manufacturer;

	private Integer history;

	private Video video;
	
	private Sound sound;
	
	private Input input;
	
	private Driver driver;

	private List<DipSwitch> dipswitches = new ArrayList<DipSwitch>();

	private List<BiosSet> biossets = new ArrayList<BiosSet>();

	private List<Rom> roms = new ArrayList<Rom>();

	private List<Disk> disks = new ArrayList<Disk>();

	private List<Chip> chips = new ArrayList<Chip>();


	@XmlAttribute
	public YesNo getRunnable() {
		return runnable;
	}

	@XmlAttribute(required=true)
	public String getName() {
		return name;
	}

	@XmlAttribute
	public String getSourcefile() {
		return sourcefile;
	}

	@XmlAttribute(name="sampleof")
	public String getSampleOf() {
		return sampleof;
	}
	
	@XmlAttribute(name="cloneof")
	public String getCloneOf() {
		return cloneof;
	}

	@XmlAttribute(name="romof")
	public String getRomOf() {
		return romof;
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}

	@XmlElement
	public String getCategory() {
		return category;
	}
	
	@XmlElement
	public int getYear() {
		return year;
	}

	@XmlElement
	public String getManufacturer() {
		return manufacturer;
	}
	
	@XmlElement
	public Integer getHistory() {
		return history;
	}
	
	@XmlElement
	public Video getVideo() {
		return video;
	}

	@XmlElement
	public Sound getSound() {
		return sound;
	}

	@XmlElement
	public Input getInput() {
		return input;
	}

	@XmlElement
	public Driver getDriver() {
		return driver;
	}
	
	@XmlElement(name="dipswitch")
	public List<DipSwitch> getDipswitches() {
		return dipswitches;
	}
	
	@XmlElement(name = "biosset")
	public List<BiosSet> getBiossets() {
		return biossets;
	}
	
	@XmlElement(name = "rom")
	public List<Rom> getRoms() {
		return roms;
	}
	
	@XmlElement(name = "chip")
	public List<Chip> getChips() {
		return chips;
	}
	
	@XmlElement(name = "disk")
	public List<Disk> getDisks() {
		return disks;
	}


	public void setRunnable(YesNo runnable) {
		this.runnable = runnable;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSourcefile(String sourcefile) {
		this.sourcefile = sourcefile;
	}

	public void setCloneOf(String cloneof) {
		this.cloneof = cloneof;
	}

	public void setRomof(String romof) {
		this.romof = romof;
	}

	public void setSampleOf(String sampleof) {
		this.sampleof = sampleof;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("  <game");

		sb.append(" runnable='" + runnable + "'");

		if (name != null) {
			sb.append(" name='" + name + "'");
		}
		if (sourcefile != null) {
			sb.append(" sourcefile='" + sourcefile + "'");
		}
		if (cloneof != null) {
			sb.append(" cloneof='" + cloneof + "'");
		}
		if (sampleof != null) {
			sb.append(" sampleof='" + sampleof + "'");
		}
		if (romof != null) {
			sb.append(" romof='" + romof + "'");
		}
		sb.append(">");

		if (description != null) {
			sb.append("\n    <description>" + description + "</description>");
		}

		if (category != null) {
			sb.append("\n    <category>" + category + "</category>");
		}

		if (year != 0) {
			sb.append("\n    <year>" + year + "</year>");
		}

		if (manufacturer != null) {
			sb.append("\n    <manufacturer>" + manufacturer + "</manufacturer>");
		}

		for (BiosSet bs : biossets) {
			sb.append("\n");
			sb.append(bs);
		}

		for (Rom r : roms) {
			sb.append("\n");
			sb.append(r);
		}

		for (Chip c : chips) {
			sb.append("\n");
			sb.append(c);
		}

		if (video != null) {
			sb.append("\n");
			sb.append(video);
		}

		if (sound != null) {
			sb.append("\n");
			sb.append(sound);
		}

		if (input != null) {
			sb.append("\n");
			sb.append(input);
		}

		for (DipSwitch ds : dipswitches) {
			sb.append("\n");
			sb.append(ds);
		}

		if (driver != null) {
			sb.append("\n");
			sb.append(driver);
		}
		sb.append("\n  </game>");

		return sb.toString();
	}

//	@Override
//	public boolean equals(Object obj) {
//		return obj != null && obj instanceof IGame && GameUtils.compare(this, (IGame)obj) == GameComp.equals;
//	}
//	@Override
//	public int hashCode() {
//		return name.hashCode();
//	}
}
