package pismy.mygames.db;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.xml.sax.SAXException;

import pismy.mygames.MyMameEnv;
import pismy.mygames.dat.IGame;
import pismy.mygames.dat.legacy.MameDat;
import pismy.mygames.dat.xml.DataFile;
import pismy.mygames.dat.xml.Mame;
import pismy.mygames.db.model.Emulator;
import pismy.mygames.db.model.PlayableRom;
import pismy.mygames.db.model.Rom;
import pismy.mygames.utils.parse.ParseError;

import com.google.common.base.Function;

public class GamesDb {

	public static void main(String[] args) {
		GamesDb db = new GamesDb(new GamesDbConfig() {
			File romsFile = MyMameEnv.getMameDBDat();
			Mame mame;
			@Override
			public long getRomsLastModified() {
				return romsFile.lastModified();
			}

			@Override
			public List<? extends IGame> getRoms() {
				if (mame == null) {
					try {
						mame = Mame.load(MyMameEnv.getMameDBDat());
					} catch (JAXBException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return mame.getGames();
			}

			@Override
			public long getEmulatorsLastModified() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public List<EmulatorConfig> getEmulators() {
				List<EmulatorConfig> emulators = new ArrayList<GamesDb.EmulatorConfig>();
				emulators.add(new EmulatorConfig() {
					List<String> roms;
					@Override
					public String getTitle() {
						return "Advance Mame";
					}

					@Override
					public List<String> getSupportedRoms() {
						if (roms == null) {
							roms = new ArrayList<String>();
							try {
								Mame mame = Mame.load(MyMameEnv.getAdvMameDat());
								for (IGame<?> g : mame.getGames()) {
									roms.add(g.getName());
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JAXBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						return roms;
					}

					@Override
					public String getRunCommand() {
						return "/user/pi/emulators/advmame/advmame %rom%";
					}

					@Override
					public String getRomsDir() {
						return "C:/My Program Files/MameUI64/ROMs";
					}

					@Override
					public String getId() {
						return "advmame";
					}

					@Override
					public String getIconImg() {
						return "default_emu.png";
					}
				});
				emulators.add(new EmulatorConfig() {
					List<String> roms;

					@Override
					public String getTitle() {
						return "Mame4All";
					}

					@Override
					public List<String> getSupportedRoms() {
						if (roms == null) {
							roms = new ArrayList<String>();
							try {
								MameDat mame = MameDat.load(MyMameEnv.getMame4AllDat());
								for (IGame g : mame.getGames()) {
									roms.add(g.getName());
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseError e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						return roms;
					}

					@Override
					public String getRunCommand() {
						return "/user/pi/emulators/mame4all/mame4all %rom%";
					}

					@Override
					public String getRomsDir() {
						return "C:/My Program Files/MameUI64/ROMs";
					}

					@Override
					public String getId() {
						return "mame4all";
					}

					@Override
					public String getIconImg() {
						return "default_emu.png";
					}
				});
				emulators.add(new EmulatorConfig() {
					List<String> roms;

					@Override
					public String getTitle() {
						return "FBA";
					}
					
					@Override
					public List<String> getSupportedRoms() {
						if (roms == null) {
							roms = new ArrayList<String>();
							try {
								DataFile mame = DataFile.load(MyMameEnv.getFbaDat());
								for (IGame g : mame.getGames()) {
									roms.add(g.getName());
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JAXBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						return roms;
					}
					
					@Override
					public String getRunCommand() {
						return "/user/pi/emulators/fba/fba %rom%";
					}
					
					@Override
					public String getRomsDir() {
						return "C:/My Program Files/MameUI64/ROMs";
					}
					
					@Override
					public String getId() {
						return "fba";
					}
					
					@Override
					public String getIconImg() {
						return "default_emu.png";
					}
				});
				return emulators;
			}
		});

		db.start();
		System.out.println("all emulators: " + db.getAllEmulators());
		System.out.println("all roms: " + db.getAllRoms().size());
		db.stop();
	}

	public static final Logger LOGGER = Logger.getLogger(GamesDb.class.getName());

	public static final SessionFactory sessionFactory;

	static {
		try {
			// Cr�ation de la SessionFactory � partir de hibernate.cfg.xml
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	interface GamesDbConfig {
		long getEmulatorsLastModified();

		List<EmulatorConfig> getEmulators();

		long getRomsLastModified();

		List<? extends IGame> getRoms();
	}

	interface EmulatorConfig {
		/**
		 * the Emulator ID
		 */
		String getId();

		/**
		 * the Emulator title
		 */
		String getTitle();

		String getIconImg();

		String getRunCommand();

		String getRomsDir();

		List<String> getSupportedRoms();
	}

	static GamesDb INST;

	public static GamesDb get() {
		return INST;
	}

	final GamesDbConfig config;
	final Properties settings;
	final File settingsFile;

	public GamesDb(GamesDbConfig config) {
		this.config = config;

		settingsFile = new File("settings.properties");
		settings = new Properties();
		if (settingsFile.exists()) {
			try {
				Reader rd = new FileReader(settingsFile);
				settings.load(rd);
				rd.close();
			} catch (IOException ioe) {
				LOGGER.log(Level.SEVERE, "failed loading settings", ioe);
			}
		}
	}

	public void start() {
		LOGGER.info("loading ...");

		// 1: check database exists
		// not needed: done by HIBERNATE

		// 2: update list of emulators
		updateRomsTable();
		updateEmulatorsTable();

		// 3: check last scan date per rom dir
		updateGamesAvailability();

		LOGGER.info(" ... done");
		INST = this;
	}

	public void stop() {
		LOGGER.info("stop");
	}

	private void buildDbAndSchema() {

	}

	private static <T> Map<String, T> toMap(List<? extends T> list, Function<T, String> keyFunction) {
		Map<String, T> map = new LinkedHashMap<String, T>();
		for (T t : list) {
			map.put(keyFunction.apply(t), t);
		}
		return map;
	}

	private boolean updateRomsTable() {
		long lastModified = Long.parseLong(settings.getProperty("roms.lastModified", "0"));
		if (lastModified >= config.getRomsLastModified()) {
			LOGGER.info(" ... ROMs configuration unchanged");
			return false;
		}

		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		boolean changed = false;

		LOGGER.info(" ... updating ROMs ...");
		Map<String, ? extends IGame> romsFromCfg = toMap(config.getRoms(), new Function<IGame, String>() {
			@Override
			public String apply(IGame game) {
				return game.getName();
			}
		});
		Map<String, Rom> romsInDb = toMap((List<Rom>) session.createCriteria(Rom.class).list(),
				new Function<Rom, String>() {
					@Override
					public String apply(Rom rom) {
						return rom.getId();
					}
				});
		for (IGame rom : romsFromCfg.values()) {
			Rom romInDb = romsInDb.get(rom.getName());
			if (romInDb == null) {
				romInDb = new Rom();
				romInDb.setId(rom.getName());
				romInDb.setTitle(rom.getDescription());
				romInDb.setCategory(rom.getCategory());
				romInDb.setManufacturer(rom.getManufacturer());
				romInDb.setYear(rom.getYear());
				if (rom.getCloneOf() != null) {
					Rom cloneInDb = (Rom) session.byId(Rom.class).load(rom.getCloneOf());
					if (cloneInDb == null) {
						LOGGER.severe(" ... parent rom [" + rom.getCloneOf() + "] for [" + rom.getName()
								+ "] not found !");
					} else {
						romInDb.setCloneof(cloneInDb);
					}
				}
				session.save(romInDb);
				changed = true;
				LOGGER.info(" ... [" + rom.getName() + "] added");
			} else {
				// already in db: remove to mark as treated
				romsInDb.remove(rom.getName());
			}
		}
		LOGGER.info(" ... removing ROMs ...");
		for (Rom rom : romsInDb.values()) {
			LOGGER.info(" ... [" + rom.getId() + "] removed");
			session.delete(rom);
		}

		// commit changes
		session.flush();
		tx.commit();
		session.close();

		// update last modified date
		settings.setProperty("roms.lastModified", String.valueOf(System.currentTimeMillis()));
		saveSettings();
		LOGGER.info(" ... done");
		return changed;
	}

	private void saveSettings() {
		try {
			Writer wr = new FileWriter(settingsFile);
			settings.store(wr, "generated");
			wr.flush();
			wr.close();
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, "failed saving settings", ioe);
		}
	}

	private boolean updateEmulatorsTable() {
		long lastModified = Long.parseLong(settings.getProperty("emulators.lastModified", "0"));
		if (lastModified >= config.getEmulatorsLastModified()) {
			LOGGER.info(" ... emulators configuration unchanged");
			return false;
		}

		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		boolean changed = false;

		LOGGER.info(" ... updating emulators ...");
		Map<String, EmulatorConfig> emusFromCfg = toMap(config.getEmulators(), new Function<EmulatorConfig, String>() {
			@Override
			public String apply(EmulatorConfig emu) {
				return emu.getId();
			}
		});
		Map<String, Emulator> emusInDb = toMap((List<Emulator>) session.createCriteria(Emulator.class).list(),
				new Function<Emulator, String>() {
					@Override
					public String apply(Emulator emu) {
						return emu.getId();
					}
				});

		for (EmulatorConfig emu : emusFromCfg.values()) {
			Emulator emuInDb = emusInDb.get(emu.getId());
			if (emuInDb == null) {
				emuInDb = new Emulator();
				emuInDb.setId(emu.getId());
				emuInDb.setTitle(emu.getTitle());
				emuInDb.setIconImg(emu.getIconImg());
				emuInDb.setRomsDir(emu.getRomsDir());
				emuInDb.setRunCommand(emu.getRunCommand());
				LOGGER.info(" ... emulator [" + emu.getId() + "] added");
				session.save(emuInDb);

				// --- then add all emulator supported games
				for (String romId : emu.getSupportedRoms()) {
					// Game gameInDb = getGame(romId, emu.getId());
					// if (gameInDb == null) {
					Rom romInDb = (Rom) session.byId(Rom.class).load(romId);
					if (romInDb == null) {
						LOGGER.severe(" ... rom [" + romId + "] for [" + emu.getId() + "] not found !");
					} else {
						PlayableRom gameInDb = new PlayableRom();
						gameInDb.setEmulator(emuInDb);
						gameInDb.setRom(romInDb);

						LOGGER.info(" ... playable rom [" + romInDb + "] added");
						session.save(gameInDb);
					}
					// }
				}

				changed = true;
			} else {
				// already in db: remove to mark as treated
				emusInDb.remove(emu.getId());
			}
		}
		LOGGER.info(" ... removing emulators ...");
		for (Emulator emu : emusInDb.values()) {
			LOGGER.info(" ... [" + emu.getId() + "] removed");
			session.delete(emu);
		}

		// commit changes
		session.flush();
		tx.commit();
		session.close();

		// update last modified date
		settings.setProperty("emulators.lastModified", String.valueOf(System.currentTimeMillis()));
		saveSettings();
		LOGGER.info(" ... done");
		return changed;
	}

	private boolean updateGamesAvailability() {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		boolean changed = false;

		List<Emulator> emulators = session.createCriteria(Emulator.class).list();
		for (Emulator emu : emulators) {
			long lastScan = Long.parseLong(settings.getProperty("scan." + emu.getId() + ".lastModified", "0"));
			File romsDir = new File(emu.getRomsDir());
			if (!romsDir.exists()) {
				LOGGER.warning(" ... ROMs dir for [" + emu.getRomsDir() + "] doesn't exist");
				continue;
			}

			if (lastScan >= romsDir.lastModified()) {
				LOGGER.warning(" ... ROMs dir for [" + emu.getRomsDir() + "] didn't change since last scan");
				continue;
			}

			// do scan
			LOGGER.info(" ... scanning [" + emu.getRomsDir() + "] for [" + emu.getId() + "]  ...");
			List<PlayableRom> supportedRoms = emu.getGames();
			for (PlayableRom g : supportedRoms) {
				File f = new File(romsDir, g.getRom().getId() + ".zip");
				if (g.isAvailable() != f.exists()) {
					g.setAvailable(f.exists());
					session.update(g);
					changed = true;
				}
			}

			// update last modified date
			settings.setProperty("scan." + emu.getId() + ".lastModified", String.valueOf(System.currentTimeMillis()));
			saveSettings();
			LOGGER.info(" ... done");
		}
		// commit changes
		session.flush();
		tx.commit();
		session.close();

		return changed;
	}

	public List<Emulator> getAllEmulators() {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<Emulator> ret = session.createCriteria(Emulator.class).list();
		tx.commit();
		return ret;
	}

	public Emulator getEmulator(String id) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Emulator ret = (Emulator) session.byId(Emulator.class).load(id);
		tx.commit();
		return ret;
	}

	public List<Rom> getAllRoms() {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<Rom> ret = session.createCriteria(Rom.class).list();
		tx.commit();
		return ret;
	}

	public Rom getRom(String id) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		Rom ret = (Rom) session.byId(Rom.class).load(id);
		tx.commit();
		return ret;
	}
	//
	// private Game getGame(String romId, String id) {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
