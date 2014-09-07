package pismy.mygames.config;

import javax.xml.bind.annotation.XmlElement;

public class Emulator {
	@XmlElement
	String id;
	@XmlElement
	String name;
	@XmlElement
	String romsDir;
	@XmlElement
	String startCommand;
	@XmlElement
	String dat;
	@XmlElement
	String datType;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRomsDir() {
		return romsDir;
	}

	public String getStartCommand() {
		return startCommand;
	}

	public String getDat() {
		return dat;
	}

	public String getDatType() {
		return datType;
	}

}
