package pismy.mygames;

import java.io.File;

public class Files {
	public static File getAdvMameRomsDir() {
//		return new File("E:/perso/MAME/config/roms");
		return new File("C:/My Program Files/MameUI64/ROMs");
	}
	public static File getFbaRomsDir() {
		return new File("E:/perso/MAME/config/emulators/fba/roms");
	}
	public static File getMame4AllRomsDir() {
		return new File("E:/perso/MAME/config/emulators/mame4all-pi/roms");
	}
	public static File getMameDBDat() {
		return new File("../data/mamedb/mamedb.xml");
	}
	public static File getMameDBLst() {
		return new File("../data/mamedb/mamedb.lst");
	}
	public static File getAdvMameDat() {
		return new File("../data/advmame/advmame.xml");
	}
	public static File getAdvMameLst() {
		return new File("../data/advmame/roms.lst");
	}
	public static File getFbaDat() {
		return new File("../data/fba/fba_029671_od_release_10_working_roms.dat.xml");
	}
	public static File getFbaLst() {
		return new File("../data/fba/roms.lst");
	}
	public static File getMame4AllDat() {
		return new File("../data/mame4all/mame4all.dat");
	}
	public static File getMame4AllLst() {
		return new File("../data/mame4all/roms.lst");
	}
//	public static void main(String[] args) {
//		for(File f : getAdvMameRomsDir().listFiles()) {
//			System.out.println(f.getName());
//		}
//	}
	public static File getGnGeoLst() {
		return new File("../data/gngeo/roms.lst");
	}
}
