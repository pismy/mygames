package pismy.mygames.tools2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.google.common.base.Joiner;

import pismy.mygames.MyMameEnv;
import pismy.mygames.dat.mamedb.MameDB;
import pismy.mygames.dat.mamedb.MameDB.MameDbGame;
import pismy.mygames.utils.file.Downloader;
import pismy.mygames.utils.parse.ParseError;

public class Consolidate {
	/**
	 * Grabs ROM information and covers (title & snapshot) from MameDB, and
	 * produces an xml dat file
	 */
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		Set<String> allRoms = load(MyMameEnv.getAdvMameLst(), MyMameEnv.getFbaLst(), MyMameEnv.getMame4AllLst(), MyMameEnv.getGnGeoLst());
		// Set<String> allRoms = new java.util.HashSet<String>();
		// allRoms.add("sf2");
		// allRoms.add("1944d");

		File snapsDir = new File("browse/images/mamedb/snaps");
		File titlesDir = new File("browse/images/mamedb/titles");
		snapsDir.mkdirs();
		titlesDir.mkdirs();

		// Mame mamedb = new Mame();
		List<String> failed = new ArrayList<String>();
		List<String> dblist = new ArrayList<String>();
		dblist.add("name|cloneof|title|category|manufacturer|year|titleImg|snapImg");

		for (String rom : allRoms) {
			downloadGame(snapsDir, titlesDir, failed, dblist, allRoms, rom);
		}

		System.out.println("failed: " + failed.size() + "/" + allRoms.size());

		// finally save
		// mamedb.save(new File("data/mamedb/mamedb2.xml"));
		save(failed, new File("data/mamedb/roms_not_found.lst"));
		save(dblist, new File("data/mamedb/mamedb.lst"));
	}

	private static void downloadGame(File snapsDir, File titlesDir, List<String> failed, List<String> dblist, Set<String> allRoms, String rom)
			throws TransformerFactoryConfigurationError {
		// --- grab game
		try {
			MameDbGame game = MameDB.get(rom);

			// download parent rom if not in list
			if (game.getCloneOf() != null && !allRoms.contains(game.getCloneOf())) {
				System.err.println("downloading missing parent rom: " + game.getCloneOf());
				downloadGame(snapsDir, titlesDir, failed, dblist, allRoms, game.getCloneOf());
			}

			// normalize
			// normalize category
			if (game.getCategory() != null && game.getCategory().endsWith("*Mature*")) {
				game.setCategory(game.getCategory().substring(0, game.getCategory().length() - 8).trim());
			}

			// copy snap
			if (game.getSnapshotImage() == null) {
				System.out.println(" ... snapshot image not found for: " + rom);
			} else {
				File snapFile = new File(snapsDir, game.getSnapshotImage() + ".png");
				try {
					URL snapUrl = new URL("http://www.mamedb.com/snap/" + game.getSnapshotImage() + ".png");
					Downloader.downloadAs(snapUrl, snapFile, false);
					System.out.println(" ... downloaded snap '" + game.getSnapshotImage() + "' for " + rom);
				} catch (Exception ioe) {
					System.err.println("Error while getting snap '" + game.getSnapshotImage() + "' for " + rom);
				}
			}

			// copy title
			if (game.getTitleImage() == null) {
				System.out.println(" ... title image not found for: " + rom);
			} else {
				File titleFile = new File(titlesDir, game.getTitleImage() + ".png");
				try {
					URL titleUrl = new URL("http://www.mamedb.com/titles/" + game.getTitleImage() + ".png");
					Downloader.downloadAs(titleUrl, titleFile, false);
					System.out.println(" ... downloaded title '" + game.getTitleImage() + "' for " + rom);
				} catch (Exception ioe) {
					System.err.println("Error while getting title '" + game.getTitleImage() + "' for " + rom);
				}
			}

			// finally write to file
			StringBuilder line = new StringBuilder();
			line.append(rom);
			line.append('|');
			line.append(orBlank(game.getCloneOf()));
			line.append('|');
			line.append(orBlank(game.getDescription()));
			line.append('|');
			line.append(orBlank(game.getCategory()));
			line.append('|');
			line.append(orBlank(game.getManufacturer()));
			line.append('|');
			line.append(game.getYear());
			line.append('|');
			line.append(orBlank(game.getTitleImage()));
			line.append('|');
			line.append(orBlank(game.getSnapshotImage()));
			dblist.add(line.toString());
			// mamedb.getGames().add(game);
			// System.out.println("... grabbed '"+rom+"'");

		} catch (Exception e) {
			System.err.println("Error while getting '" + rom + "'");
			// e.printStackTrace();
			failed.add(rom);
		}
	}

	private static String orBlank(String s) {
		return s == null ? "" : s;
	}
	public static Set<String> load(File... listFiles) throws IOException {
		Set<String> set = new LinkedHashSet<String>();
		for(File f : listFiles) {
			set.addAll(com.google.common.io.Files.readLines(f, Charset.defaultCharset()));
		}
		return set;
	}
	public static void save(List<String> lines, File to) throws IOException {
		com.google.common.io.Files.write(Joiner.on('\n').join(lines), to, Charset.defaultCharset());
	}

}
