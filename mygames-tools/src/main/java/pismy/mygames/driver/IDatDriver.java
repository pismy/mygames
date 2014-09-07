package pismy.mygames.driver;

import java.io.File;

/**
 * For a given emulator, know whether a file is a ROM
 */
public interface IDatDriver {
	boolean isRom(File f);
}
