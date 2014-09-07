package pismy.mygames.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.Files;
import pismy.mygames.dat.IGame;
import pismy.mygames.dat.file.ZipDat;
import pismy.mygames.dat.legacy.MameDat;
import pismy.mygames.dat.xml.DataFile;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.utils.parse.ParseError;

/**
 * TODO:
 * merge clones
 * @author crhx7117
 */
public class Search {
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		Mame advmame = Mame.load(Files.getAdvMameDat());
		System.out.println("advmame ROMs: "+advmame.getGames().size());
		
		MameDat mame4all = MameDat.load(Files.getMame4AllDat());
		System.out.println("mame4all ROMs: "+mame4all.getGames().size());

		DataFile fba = DataFile.load(Files.getFbaDat());
		System.out.println("FBA ROMs: "+fba.getGames().size());

		ZipDat advmameroms = new ZipDat(Files.getAdvMameRomsDir());
		System.out.println("my advmame ROMs: "+advmameroms.getGames().size());
		
//		// ROMs >= 1990 that I don't have
//		System.out.println("non-clone ROMs >= 1990 that I don't have:");
//		List<IGame> selected = new ArrayList<IGame>();
//		for(IGame g : advmame.getGames()) {
//			if(g.getCloneOf() == null && g.getYear() >= 1990 && !advmameroms.getRoms().contains(g.getName()) && (mame4all.getByRom(g.getName()) != null || fba.getByRom(g.getName()) != null)) {
//				selected.add(g);
//			}
//		}
//		// sort by year
//		Collections.sort(selected, new Comparator<IGame>() {
//			@Override
//			public int compare(IGame o1, IGame o2) {
//				return o1.getYear() - o2.getYear();
//			}
//		});
//		
//		int idx = 0;
//		for(IGame g : selected)
//			System.out.println(" "+(idx++)+"- ["+g.getYear()+"/"+g.getName()+"] "+g.getDescription()+" / mame4all: "+(mame4all.getByRom(g.getName()) != null)+" / FBA: "+(fba.getByRom(g.getName()) != null));
//		
//		// dump all clone ROMs I own
//		System.out.println("my clone ROMs:");
//		idx = 0;
//		for(String rom : myroms.getRoms()) {
//			Game g = advmame.getByRom(rom);
//			if(g == null) {
//				System.out.println(" "+(idx++)+"- ["+rom+"] unkown ROM");
//			} else if(g.getCloneOf() != null) {
//				System.out.println(" "+(idx++)+"- ["+rom+"] ('"+g.getDescription()+"') clone of ["+g.getCloneOf()+"]; have original: "+myroms.getRoms().contains(g.getCloneOf()));
//			}
//		}
//		
//		// compare ROM sets
//		System.out.println("Different ROMs (advmame/mame4all):");
//		idx=0;
//		for(Game g : advmame.getGames()) {
//			romsmgr.dat.legacy.Game g2 = mame4all.getByRom(g.getName());
//			if(g2 != null && !g.equals(g2)) {
//				System.out.println(" "+(idx++)+"-["+g.getName()+"] "+g.getDescription());
//			}
//		}
//		System.out.println("Different ROMs (advmame/fba):");
//		idx=0;
//		for(Game g : advmame.getGames()) {
//			Game g3 = fba.getByRom(g.getName());
//			if(g3 != null && !g.equals(g3)) {
//				System.out.println(" "+(idx++)+"-["+g.getName()+"] "+g.getDescription());
//			}
//		}
//		
	}
}
