package pismy.mygames.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.google.common.io.Files;
import com.google.common.io.Resources;

public class Downloader {
	
	public static enum DownloadResult {
		DOWNLOADED, FOUND_IN_CACHE
	}
	
	public static DownloadResult downloadAs(URL url, File destFile, boolean force) throws IOException {
		if(force || !destFile.exists()) {
			Files.copy(Resources.newInputStreamSupplier(url), destFile);
			return DownloadResult.DOWNLOADED;
		} else {
			return DownloadResult.FOUND_IN_CACHE;
		}
	}

	public static DownloadResult downloadTo(URL file, File destDir, boolean force) throws IOException {
		String name = file.getPath();
		int idx = name.lastIndexOf('/');
		if(idx >= 0) {
			name = name.substring(idx+1);
		}
		return downloadAs(file, new File(destDir, name), force);
	}
}
