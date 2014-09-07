package pismy.mygames.tools2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.dat.IDat;
import pismy.mygames.dat.IGame;
import pismy.mygames.utils.parse.ParseError;

/**
 * builds basic ROMs lists for every emulator
 */
public class RomsList {
	public static void main(String[] args) throws JAXBException, SAXException, IOException, ParserConfigurationException, ParseError {
//		makeList(Mame.load(Files.getAdvMameDat()), Files.getAdvMameLst());
//		makeList(MameDat.load(Files.getMame4AllDat()), Files.getMame4AllLst());
//		makeList(DataFile.load(Files.getFbaDat()), Files.getFbaLst());
//		Set<String> all = load(Files.getAdvMameLst(), Files.getFbaLst(), Files.getMame4AllLst(), Files.getGnGeoLst());
//		System.out.println(all);
		Set<String> set = new LinkedHashSet<String>();
		set.add("AAA");
		set.add("BBB");
		set.add("DDD");
		set.add("AAA");
		set.add("CCC");
		System.out.println(set);
	}
	public static void makeList(IDat dat, File to) throws IOException {
		List<String> roms = new ArrayList();
		for(IGame game : dat.getGames()) {
			if(roms.contains(game.getName())) {
				System.err.println("game doubled: "+game.getName());
			} else {
				roms.add(game.getName());
			}
		}
		Collections.sort(roms);
		save(roms, to);
	}
	public static Set<String> load(File... listFiles) throws IOException {
		Set<String> set = new LinkedHashSet<String>();
		for(File f : listFiles) {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line = null;
			while((line = r.readLine()) != null) {
				set.add(line);
			}
			r.close();
		}
		return set;
	}
	public static void save(List<String> lines, File to) throws IOException {
		Writer w = new FileWriter(to);
		for(String r : lines) {
			w.write(r);
			w.write("\n");
		}
		w.flush();
		w.close();
	}
}
