package pismy.mygames.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.Files;
import pismy.mygames.dat.IGame;
import pismy.mygames.dat.file.ZipDat;
import pismy.mygames.dat.legacy.MameDat;
import pismy.mygames.dat.xml.DataFile;
import pismy.mygames.dat.xml.Game;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.dat.xml.YesNo;
import pismy.mygames.utils.GameUtils;
import pismy.mygames.utils.parse.ParseError;

public class MyRoms {
	/**
	 * Lists all the ROMS I have
	 */
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		MameDat mame4all = MameDat.load(Files.getMame4AllDat());
		System.out.println("mame4all ROMs: "+mame4all.getGames().size());

		Mame advmame = Mame.load(Files.getAdvMameDat());
		System.out.println("advmame ROMs: "+advmame.getGames().size());

		DataFile fba = DataFile.load(Files.getFbaDat());
		System.out.println("FBA ROMs: "+fba.getGames().size());

		ZipDat advmameroms = new ZipDat(Files.getAdvMameRomsDir());
		System.out.println("my advmame ROMs: "+advmameroms.getGames().size());
		
		// now build table
		FileWriter writer = new FileWriter(new File("roms.csv"));
		
//		writer.write("ROM;Name;Year;System;advmame;mame4all;gngeo\n");
		writer.write("ROM;Clone Of;Name;Year;advmame;mame4all;gngeo;fba\n");
		
		for(IGame game : advmameroms.getGames()) {
			String name = game.getName();
			Game advMameGame = GameUtils.getGameByName(advmame, name);
			
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
			// advmame
			if(advMameGame == null) {
				writer.write("N/A");
			} else if(advMameGame.getRunnable() == YesNo.no) {
				writer.write("not runnable");
			}
			writer.write(";");
			// mame4all
			pismy.mygames.dat.legacy.Game mame4allGame = GameUtils.getGameByName(mame4all, name);
			if(mame4allGame == null) {
				writer.write("N/A");
			}
			writer.write(";");
			// gngeo
			if(advMameGame != null && !"neogeo".equals(advMameGame.getRomOf())) {
				writer.write("N/A");
			}
			writer.write(";");
			// FBA
			Game fbaGame = GameUtils.getGameByName(fba,name);
			if(fbaGame == null) {
				writer.write("N/A");
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
