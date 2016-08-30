package pismy.mygames.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import pismy.mygames.Files;
import pismy.mygames.dat.mamedb.MameDB;
import pismy.mygames.dat.xml.Game;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.utils.file.FileUtils;
import pismy.mygames.utils.parse.ParseError;

public class GetFromMameDB {
	/**
	 * Grabs ROM information and covers (title & snapshot) from MameDB, and produces an xml dat file
	 */
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		Mame advmame = Mame.load(Files.getAdvMameDat());
		
		File snapsDir = new File("browse/images/snaps");
		File titlesDir = new File("browse/images/titles");
		
		Mame mamedb = getGamesFromMameDb(advmame, snapsDir, titlesDir);
		// finally save
		mamedb.save(new File("data/mamedb.xml"));
	}

	public static Mame getGamesFromMameDb(Mame advmame, File snapsDir, File titlesDir) throws TransformerFactoryConfigurationError, JAXBException,
			SAXException, IOException, ParserConfigurationException {
		snapsDir.mkdirs();
		titlesDir.mkdirs();
		Mame mamedb = new Mame();
		
		for(Game advMameGame : advmame.getGames()) {
			getGameFromMameDb(snapsDir, titlesDir, advMameGame);
		}
		
		return mamedb;
	}

	public static Game getGameFromMameDb(File snapsDir, File titlesDir, Game advMameGame) throws TransformerFactoryConfigurationError {
		// copy snap
		File snapFile = new File(snapsDir, advMameGame.getName()+".png");
		if(!snapFile.exists()) {
			try {
				URL snapUrl = new URL("http://www.mamedb.com/snap/"+advMameGame.getName()+".png");
				FileUtils.copy(snapUrl.openStream(), new FileOutputStream(snapFile));
			} catch(Exception ioe) {
				System.err.println("Error while getting '"+advMameGame.getName()+"' snap image");
			}
		}

		// copy title
		File titleFile = new File(titlesDir, advMameGame.getName()+".png");
		if(!titleFile.exists()) {
			try {
				URL titleUrl = new URL("http://www.mamedb.com/titles/"+advMameGame.getName()+".png");
				FileUtils.copy(titleUrl.openStream(), new FileOutputStream(titleFile));
			} catch(Exception ioe) {
				System.err.println("Error while getting '"+advMameGame.getName()+"' title image");
			}
		}

		// --- grab game
		try {
			Game mamedbGame = MameDB.get(advMameGame.getName());
			mamedb.getGames().add(mamedbGame);
			System.out.println("... grabbed '"+advMameGame.getName()+"'");
		} catch (Exception e) {
			System.err.println("Error while getting '"+advMameGame.getName()+"'");
			e.printStackTrace();
		}
	}


}
