package pismy.mygames.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author Pierre Smeyers
 */
@XmlRootElement(name = "config")
public class Config {
	
	@XmlElement(name="emulators")
	private List<Emulator> emulators = new ArrayList<Emulator>();
	
	public List<Emulator> getEmulators() {
		return emulators;
	}
	
	public static Config load(File xml) throws JAXBException, SAXException,
			IOException, ParserConfigurationException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		Config suite = (Config) unmarshaller.unmarshal(xml);
		return suite;
	}
}
