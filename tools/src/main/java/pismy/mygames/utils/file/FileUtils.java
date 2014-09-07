package pismy.mygames.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	public static String getNameNoExt(File file) {
		return getNameNoExt(file.getName());
	}
	public static String getNameNoExt(String fileName) {
		int idx = fileName.lastIndexOf('.');
		return idx < 0 ? fileName : fileName.substring(0, idx);
	}
	/**
	 * copies all bytes from input to ouput, then closes both streams
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.flush();
		out.close();
	}
}
