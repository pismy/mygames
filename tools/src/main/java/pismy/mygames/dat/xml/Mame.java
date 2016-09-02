package pismy.mygames.dat.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.dat.IDat;

/**
 * @author Pierre Smeyers
 */
@XmlRootElement(name = "mame")
public class Mame extends IDat<Game> {
	
	@XmlAttribute
	private String build;

	@XmlElement(name="game")
	private List<Game> games = new ArrayList<Game>();
	
	public String getBuild() {
		return build;
	}
	public List<Game> getGames() {
		return games;
	}

	@Override
	public String toString() {
		try {
			StringWriter writer = new StringWriter();
			JAXBContext jaxbContext = JAXBContext.newInstance(Mame.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(this, writer);
			return writer.toString();
		} catch(JAXBException e) {
			StringBuffer sb = new StringBuffer();
			sb.append("<mame build='");
			sb.append(build);
			sb.append(">");
			for(Game g : games) {
				sb.append("\n");
				sb.append(g);
			}
			sb.append("\n</mame>");
			return sb.toString();
		}
	}
	
//	public static void main(String[] args) throws JAXBException, SAXException, IOException, ParserConfigurationException {
//		Mame mame = load(new File("E:/perso/MAME/config/.advance/advmame.xml"));
//		System.out.println(mame);
//		System.out.println("games: "+mame.getGames().size());
//		
//		System.out.println("game (baddudes): "+mame.getByRom("baddudes"));
//	}
	
	public static Mame load(File xml) throws JAXBException, SAXException,
			IOException, ParserConfigurationException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Mame.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Mame suite = (Mame) unmarshaller.unmarshal(xml);
		return suite;
	}
	public void save(File xml) throws JAXBException, SAXException,
	IOException, ParserConfigurationException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Mame.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, xml);
	}
}
