package pismy.mygames.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.MyMameEnv;
import pismy.mygames.dat.file.ZipDat;
import pismy.mygames.dat.file.ZipGame;
import pismy.mygames.dat.xml.Game;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.utils.GameComparator;
import pismy.mygames.utils.json.JSonObjectWriter;
import pismy.mygames.utils.json.JSonSerializer;
import pismy.mygames.utils.json.JSonWriter;

public class Dat2Json {

	/**
	 * Converts a dat file into JSON
	 */
	public static void main(String[] args) throws JAXBException, SAXException, IOException, ParserConfigurationException {
		Mame mamedbDat = Mame.load(MyMameEnv.getMameDBDat());
		ZipDat myroms = new ZipDat(MyMameEnv.getAdvMameRomsDir());
		System.out.println("my advmame ROMs: "+myroms.getGames().size());
		
		List<Game> gamesIhave = new ArrayList<Game>();
		for(ZipGame g : myroms.getGames()) {
			Game g2 = mamedbDat.byName(g.getName());
			if(g2 != null)
				gamesIhave.add(g2);
		}
		
		JSonWriter writer = new JSonWriter(new PrintWriter(new File("roms.json")));
		writer.registerSerializer(Game.class, new GameSerializer());
		writer.writeObj(gamesIhave);
		writer.flush();
		writer.close();
	}
	
	private static class GameSerializer implements JSonSerializer<Game> {
		@Override
		public void serialize(Game object, JSonObjectWriter writer) {
			writer.writeAttr("id", object.getName());
			writer.writeAttr("name", object.getDescription());
			writer.writeAttr("manuf", object.getManufacturer());
			writer.writeAttr("cat", object.getCategory());
			writer.writeAttr("year", object.getYear());
			writer.writeAttr("cloneof", object.getCloneOf());
		}
	}

}
