package pismy.mygames.dat.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
@XmlRootElement(name = "datafile")
public class DataFile implements IDat {
	
	@XmlAttribute
	private String build;

	@XmlAttribute
	private YesNo debug = YesNo.no;
	
	@XmlElement
	private Header header;

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
		StringBuffer sb = new StringBuffer();
		sb.append("<datafile build='");
		sb.append(build);
		sb.append(">");
		for(Game g : games) {
			sb.append("\n");
			sb.append(g);
		}
		sb.append("\n</datafile>");
		return sb.toString();
	}
	
//	public Game getByRom(String romName) {
//		for(Game g : games) {
//			if(romName.equals(g.getName()))
//				return g;
//		}
//		return null;
//	}
	
//	public static void main(String[] args) throws JAXBException, SAXException, IOException, ParserConfigurationException {
//		DataFile mame = load(new File("E:/perso/MAME/config/.advance/advmame.xml"));
//		System.out.println(mame);
//		System.out.println("games: "+mame.getGames().size());
//		
//		System.out.println("game (baddudes): "+mame.getByRom("baddudes"));
//	}
	
	public static DataFile load(File xml) throws JAXBException, SAXException,
			IOException, ParserConfigurationException {
		JAXBContext jaxbContext = JAXBContext.newInstance(DataFile.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(null);
		DataFile suite = (DataFile) unmarshaller.unmarshal(xml);
		return suite;
	}
}
