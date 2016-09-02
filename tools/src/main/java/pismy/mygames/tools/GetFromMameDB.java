package pismy.mygames.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import pismy.mygames.MyMameEnv;
import pismy.mygames.dat.mamedb.MameDB;
import pismy.mygames.dat.xml.Game;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.utils.file.Downloader;
import pismy.mygames.utils.parse.ParseError;

public class GetFromMameDB {
	/**
	 * Grabs ROM information and covers (title & snapshot) from MameDB, and produces an xml dat file
	 */
	public static void main(String[] args) throws IOException, ParseError, JAXBException, SAXException, ParserConfigurationException {
		Mame advmame = Mame.load(MyMameEnv.getAdvMameDat());
		
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
			// copy snap
			File snapFile = new File(snapsDir, advMameGame.getName()+".png");
			try {
				URL snapUrl = new URL("http://www.mamedb.com/snap/"+advMameGame.getName()+".png");
				Downloader.downloadAs(snapUrl, snapFile, false);
			} catch(Exception ioe) {
				System.err.println("Error while getting '"+advMameGame.getName()+"' snap image");
			}
			
			// copy title
			File titleFile = new File(titlesDir, advMameGame.getName()+".png");
			try {
				URL titleUrl = new URL("http://www.mamedb.com/titles/"+advMameGame.getName()+".png");
				Downloader.downloadAs(titleUrl, titleFile, false);
			} catch(Exception ioe) {
				System.err.println("Error while getting '"+advMameGame.getName()+"' title image");
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
		
		return mamedb;
	}

}
