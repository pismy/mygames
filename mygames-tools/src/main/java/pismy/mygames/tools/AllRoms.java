package pismy.mygames.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.Files;
import pismy.mygames.dat.legacy.MameDat;
import pismy.mygames.dat.xml.DataFile;
import pismy.mygames.dat.xml.Game;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.utils.GameUtils;
import pismy.mygames.utils.GameUtils.Comparison;
import pismy.mygames.utils.GameUtils.Status;
import pismy.mygames.utils.parse.ParseError;

public class AllRoms {
	/**
	 * Writes all roms managed by advmame, mame4all, FBA into a csv file
	 */
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		Mame advmame = Mame.load(Files.getAdvMameDat());
		System.out.println("advmame ROMs: "+advmame.getGames().size());
		
		MameDat mame4all = MameDat.load(Files.getMame4AllDat());
		System.out.println("mame4all ROMs: "+mame4all.getGames().size());

		DataFile fba = DataFile.load(Files.getFbaDat());
		System.out.println("FBA ROMs: "+fba.getGames().size());

		// now build table
		FileWriter writer = new FileWriter(new File("allroms.csv"));
		
//		writer.write("ROM;Name;Year;System;advmame;mame4all;gngeo\n");
//		writer.write("ROM;Clone Of;Name;Year;advmame;mame4all;gngeo;fba\n");
		writer.write("ROM;Clone Of;Name;Year;mame4all;gngeo;fba\n");
		
		for(Game advMameGame : advmame.getGames()) {
			String name = advMameGame.getName();
			Game gngeoGame = "neogeo".equals(advMameGame.getRomOf()) ? advMameGame : null;
			pismy.mygames.dat.legacy.Game mame4allGame = GameUtils.getGameByName(mame4all, name);
			Game fbaGame = GameUtils.getGameByName(fba,name);
			if(gngeoGame == null && mame4allGame == null && fbaGame == null)
				continue;
			
			// ROM
			writer.write(name);
			writer.write(";");
			// clone of
			if(advMameGame != null && advMameGame.getCloneOf() != null)
				writer.write(advMameGame.getCloneOf());
			writer.write(";");
			// name
			if(advMameGame != null) {
				writer.write(advMameGame.getDescription());
			}
			writer.write(";");
			// year
			if(advMameGame != null) {
				writer.write(String.valueOf(advMameGame.getYear()));
			}
//			writer.write(";");
//			// system
//			if(advMameGame != null) {
//				writer.write(advMameGame.getRomof());
//			}
			writer.write(";");
//			// advmame
//			if(advMameGame == null) {
//				writer.write("N/A");
//			} else if(advMameGame.getRunnable() == YesNo.no) {
//				writer.write("not runnable");
//			}
//			writer.write(";");
			// mame4all
			if(mame4allGame == null) {
				writer.write("N/A");
			} else {
				Comparison cmp = GameUtils.compare(mame4allGame, advMameGame);
				writer.write(cmp.dump(Status.incompatible, true));
			}
			writer.write(";");
			// gngeo
			if(gngeoGame == null) {
				writer.write("N/A");
			}
			writer.write(";");
			// FBA
			if(fbaGame == null) {
				writer.write("N/A");
			} else {
				Comparison cmp = GameUtils.compare(fbaGame, advMameGame);
				writer.write(cmp.dump(Status.incompatible, true));
			}
			writer.write("\n");
		}
		
		writer.flush();
		writer.close();
		
		// 
//		System.out.println("Specific Mame4All ROM:");
//		RomsList mame4allroms = RomsList.loadFromDir(Config.getMame4AllRomsDir());
//		for(String r2 : mame4allroms.getRoms()) {
//			boolean found = false;
//			for(String r1 : advmameroms.getRoms()) {
//				if(r1.equals(r2)) {
//					found = true;
//					break;
//				}
//			}
//			if(!found) {
//				System.out.println(" - "+r2);
//			}
//		}
	}


}
