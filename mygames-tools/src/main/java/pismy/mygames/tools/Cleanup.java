package pismy.mygames.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.Files;
import pismy.mygames.dat.IDat;
import pismy.mygames.dat.IGame;
import pismy.mygames.dat.file.ZipDat;
import pismy.mygames.dat.file.ZipGame;
import pismy.mygames.dat.legacy.MameDat;
import pismy.mygames.dat.wrapper.DatWrapper;
import pismy.mygames.dat.xml.DataFile;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.utils.GameUtils;
import pismy.mygames.utils.GameUtils.Comparison;
import pismy.mygames.utils.parse.ParseError;

/**
 * TODO:
 * merge clones
 * @author crhx7117
 */
public class Cleanup {
	public static void makeReport(ZipDat roms, IDat database, File output) throws IOException {
		FileWriter writer = new FileWriter(output);
		
		writer.write("ROM;Clone of;Name;Year;Status;Comment\n");
		
		for(ZipGame zg : roms.getGames()) {
			IGame ref = GameUtils.getGameByName(database, zg.getName());
			// ROM
			writer.write(zg.getName());
			writer.write(";");
			// clone of
			if(ref != null && ref.getCloneOf() != null) {
				writer.write(ref.getCloneOf());
			}
			writer.write(";");
			// name
			if(ref != null) {
				writer.write(ref.getDescription());
			}
			writer.write(";");
			// year
			if(ref != null) {
				writer.write(String.valueOf(ref.getYear()));
			}
			writer.write(";");
			// status;comment
			if(zg.isCorrupt()) {
				writer.write("corrupt;");
			} else if(ref == null) {
				writer.write("unknown;");
			} else {
				// check validity
				Comparison comp = GameUtils.compare(zg, ref);
				writer.write(comp.getStatus().toString());
				writer.write(";");
				writer.write("\"");
				writer.write(comp.toString());
				writer.write("\"");
				
//				// move clones if original game is found
//				if(ref.getCloneOf() != null) {
//					IGame original = GameUtils.getGameByName(roms, ref.getCloneOf());
//					if(original == null) {
//						System.out.println("["+zg.getName()+"] clone of ["+ref.getCloneOf()+"]: move to ./clones");
//					} else {
//						// TODO: choose the clone that matches the best...
//						System.out.println("["+zg.getName()+"] clone of ["+ref.getCloneOf()+"] but original game was not found");
//					}
//				} else {
//					writer.write("ok;\""+(comp)+"\"");
//				}
			}
			writer.write("\n");
		}
		
		writer.flush();
		writer.close();
	}
	private static boolean move(ZipGame game, File targetDir) {
		targetDir.mkdirs();
		game.close();
		return game.getFile().renameTo(new File(targetDir, game.getFile().getName()));
	}
	private static class GameAndComp {
		final ZipGame game;
		final IGame ref;
		final Comparison comp;
		public GameAndComp(ZipGame game, IGame ref, Comparison comp) {
			super();
			this.game = game;
			this.ref = ref;
			this.comp = comp;
		}
		public int score() {
			return comp.getStatus().ordinal() + (ref.getCloneOf() == null ? 0 : 2); 
		}
		@Override
		public String toString() {
			return "["+game.getName()+"] '"+ref.getDescription()+"' ("+(ref.getCloneOf() == null ? "original" : "clone of '"+ref.getCloneOf()+"'")+") / "+comp.getStatus();
		}
	}

	public static void cleanup(ZipDat roms, IDat database, boolean doClean) {
		System.out.println("Cleaning ROM dir: "+roms.getDir()+" ...");
		
		File corruptDir = new File(roms.getDir(), "corrupt");
		File unkDir = new File(roms.getDir(), "unknown");
		File errorsDir = new File(roms.getDir(), "errors");
		File clonesDir = new File(roms.getDir(), "clones");
		
		Map<String, List<GameAndComp>> parentRom2ValidGames = new HashMap<String, List<GameAndComp>>();
		for(ZipGame zg : roms.getGames()) {
			IGame ref = GameUtils.getGameByName(database, zg.getName());
			if(zg.isCorrupt()) {
				// --- move corrupt games
				System.out.println("["+zg.getName()+"] corrupt: move to ./corrupt");
				if(doClean) {
					move(zg, corruptDir);
				}
			} else if(ref == null) {
				System.out.println("["+zg.getName()+"] unkown: move to ./unknown");
				if(doClean) {
					move(zg, unkDir);
				}
			} else {
				// check validity
				Comparison comp = GameUtils.compare(zg, ref);
//				Status st = comp.getStatus();
//				if(st != Status.matches) {
////					System.out.println("["+zg.getName()+"] does not match: \n"+comp);
//					
//					if(st == Status.incompatible) {
////						if(doClean) {
////							move(zg.getFile(), errorsDir);
////						}
//						continue;
//					}
//					
//					if(st.equals(Status.can_be_fixed)) {
//						if(!comp.fix()) {
////							System.out.println(" ... fix failed");
//							continue;
//						}
//					}
//				}
				
				String masterName = ref.getCloneOf() != null ? ref.getCloneOf() : zg.getName();
				List<GameAndComp> games = parentRom2ValidGames.get(masterName);
				if(games == null) {
					games = new ArrayList<GameAndComp>();
					parentRom2ValidGames.put(masterName, games);
				}
				games.add(new GameAndComp(zg, ref, comp));
				
//				// move clones if original game is found
//				if(ref.getCloneOf() != null) {
////					IGame original = GameUtils.getGameByName(roms, ref.getCloneOf());
////					if(original == null) {
////						System.out.println("["+zg.getName()+"] clone of ["+ref.getCloneOf()+"]: move to ./clones");
////					} else {
////						// TODO: choose the clone that matches the best...
////						System.out.println("["+zg.getName()+"] clone of ["+ref.getCloneOf()+"] but original game was not found");
////					}
//					
//				}
			}
			
		}
		
		// --- finally process clones by selecting the most appropriate game
		for(Entry<String, List<GameAndComp>> entry : parentRom2ValidGames.entrySet()) {
			if(entry.getValue().size() == 1) {
				// leave
			} else {
				// select best ROM
				List<GameAndComp> clones = entry.getValue();
				Collections.sort(clones, new Comparator<GameAndComp>() {
					@Override
					public int compare(GameAndComp o1, GameAndComp o2) {
						return o1.score() - o2.score();
					}
				});
				// leave first, move all others in ./clones dir
				System.out.println("Clones found for ["+entry.getKey()+"]:");
				for(int i=0; i<clones.size(); i++) {
					GameAndComp cg = clones.get(i);
					System.out.println(" "+i+") "+cg+": "+(i == 0 ? "keep" : "move to ./clones"));
					if(i > 0 && doClean) {
						move(cg.game, clonesDir);
					}
				}
			}
		}
		// --- 
	}
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		DatWrapper advmame = new DatWrapper(Mame.load(Files.getAdvMameDat()), null);
		System.out.println("advmame ROMs: "+advmame.getGames().size());
		
		ZipDat advmameroms = new ZipDat(Files.getAdvMameRomsDir());
		System.out.println("my advmame ROMs: "+advmameroms.getGames().size());
		
//		cleanup(advmameroms, advmame, true);
		makeReport(advmameroms, advmame, new File("advmame_roms.csv"));
		
		DatWrapper mame4all = new DatWrapper(MameDat.load(Files.getMame4AllDat()), advmame);
		System.out.println("mame4all ROMs: "+mame4all.getGames().size());

		ZipDat mame4allroms = new ZipDat(Files.getMame4AllRomsDir());
		System.out.println("my mame4all ROMs: "+mame4allroms.getGames().size());
		
//		cleanup(mame4allroms, mame4all, true);
		makeReport(mame4allroms, mame4all, new File("mame4all_roms.csv"));
		
		DatWrapper fba = new DatWrapper(DataFile.load(Files.getFbaDat()), advmame);
		System.out.println("FBA ROMs: "+fba.getGames().size());

		ZipDat fbaroms = new ZipDat(Files.getFbaRomsDir());
		System.out.println("my fba ROMs: "+fbaroms.getGames().size());
		
//		cleanup(fbaroms, fba, true);
		makeReport(fbaroms, fba, new File("fba_roms.csv"));
	}
}
