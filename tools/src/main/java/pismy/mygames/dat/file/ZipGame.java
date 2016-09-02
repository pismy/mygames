package pismy.mygames.dat.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import pismy.mygames.dat.IGame;
import pismy.mygames.utils.file.FileUtils;

public class ZipGame extends IGame<ZipRom> {
	final File file;
	ZipFile zip;
	List<ZipRom> roms;
	Boolean corrupt;

	public ZipGame(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void close() {
		if (zip != null) {
			try {
				zip.close();
			} catch (IOException e) {
			}
			zip = null;
		}
	}

	@Override
	public String getName() {
		return FileUtils.getNameNoExt(file.getName());
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getCategory() {
		return null;
	}

	@Override
	public String getManufacturer() {
		return null;
	}

	@Override
	public int getYear() {
		return 0;
	}

	@Override
	public String getCloneOf() {
		return null;
	}

	public boolean isCorrupt() {
		// make sure zip is loaded
		getRoms();
		return corrupt;
	}

	@Override
	public List<ZipRom> getRoms() {
		if (roms == null) {
			roms = new ArrayList<ZipRom>();
			zip = null;
			try {
				zip = new ZipFile(file);
			} catch (IOException e) {
				corrupt = true;
				return roms;
			}
			corrupt = false;
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				// exclude some entries
				ZipEntry e = entries.nextElement();
				if (e.isDirectory() || e.getName().endsWith(".txt") || e.getName().endsWith(".html"))
					continue;
				roms.add(new ZipRom(zip, e));
			}
		}
		return roms;
	}
}
