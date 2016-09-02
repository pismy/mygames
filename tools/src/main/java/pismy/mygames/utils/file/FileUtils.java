package pismy.mygames.utils.file;

import java.io.File;

public class FileUtils {
	public static String getNameNoExt(File file) {
		return getNameNoExt(file.getName());
	}
	public static String getNameNoExt(String fileName) {
		int idx = fileName.lastIndexOf('.');
		return idx < 0 ? fileName : fileName.substring(0, idx);
	}
}
