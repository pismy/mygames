package pismy.mygames.dat.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import pismy.mygames.dat.IDat;

public class ZipDat extends IDat<ZipGame> {
	final File dir;
	final List<ZipGame> games;
	public ZipDat(File dir) {
		this.dir = dir;
		File[] zipFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return /*f.isFile() &&*/ name.endsWith(".zip");
			}
		});
		games = new ArrayList<ZipGame>();
		for(File f : zipFiles) {
			games.add(new ZipGame(f));
		}
	}
	public File getDir() {
		return dir;
	}
	@Override
	public List<ZipGame> getGames() {
		return games;
	}
	
}
