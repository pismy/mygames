package pismy.mygames.app;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pismy.mygames.config.Config;
import pismy.mygames.config.Emulator;
import pismy.mygames.db.GamesDb;
import pismy.mygames.driver.DriverFactory;
import pismy.mygames.driver.IDatDriver;

public class Gamz {
	public void start(String... args) throws JAXBException, SAXException, IOException, ParserConfigurationException {
		// --- open web front with "loading..." page
		// TODO
		
		// --- load configuration
		Config cfg = Config.load(new File("config.xml"));
		
		// --- check configuration (dirs and files exist)
		// TODO
		
		// --- load settings
		File settingsFile = new File("settings.properties");
		Properties settings = new Properties();
		if (settingsFile.exists()) {
			Reader rd = new FileReader(settingsFile);
			settings.load(rd);
			rd.close();
		}

		// --- init database
		GamesDb db = GamesDb.get();
		db.start();
		
		// --- check emulators from DB that where removed
		// TODO
		
		// --- add new emulators to DB
		// TODO
		
		// --- mark ROMs from DB that where removed from file system as 'unavailable'
		// TODO
		
		// --- scan roms directories
		// first, arrange in a map (not to scan several times the same dir)
		Map<String, List<IDatDriver>> dir2Emulators = new HashMap<String, List<IDatDriver>>();
		for(Emulator e : cfg.getEmulators()) {
			List<IDatDriver> es = dir2Emulators.get(e.getRomsDir());
			if(es == null) {
				es = new ArrayList<IDatDriver>();
				dir2Emulators.put(e.getRomsDir(), es);
			}
			es.add(DriverFactory.getByType(e.getDatType(), e.getDat()));
		}
		for(Entry<String, List<IDatDriver>> entry : dir2Emulators.entrySet()) {
			// check last scan date of this dir, compare to lastModified date
			File dir = new File(entry.getKey());
			long lastScan = 0;
			String lastScanKey = entry.getKey().replace('\\', '_').replace('/', '_')+".lastScan";
			String lastScanStr = settings.getProperty(lastScanKey);
			if(lastScanStr != null) {
				lastScan = Long.parseLong(lastScanStr);
			}
			if(lastScan >= dir.lastModified()) {
				System.out.println(" ... ROMs dir "+entry.getKey()+" unchanged since last scan");
				continue;
			}
			System.out.println(" ... scanning "+entry.getKey());
			
			for(File f : dir.listFiles()) {
				boolean isDownloaded = false;
				for(IDatDriver dd : entry.getValue()) {
					if(dd.isRom(f)) {
						// download info
						if(!isDownloaded) {
							// TODO
//							Game g = MameDB.get(f.getName()) {
//								
//							}
							isDownloaded = true;
						}
						// add playable to DB
						// TODO
					}
				}
			}
			
			settings.setProperty(lastScanKey, String.valueOf(System.currentTimeMillis()));
			// save settings
			Writer w = new FileWriter(settingsFile);
			settings.store(w, "saved after a scan");
			w.close();
		}
		
	}
}
