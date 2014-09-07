package pismy.mygames.dat.file;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import pismy.mygames.dat.IRom;
import pismy.mygames.utils.DigestUtils;

public class ZipRom implements IRom {
	final ZipFile file;
	final ZipEntry entry;
	String md5;
	String sha1;
	public ZipRom(ZipFile file, ZipEntry entry) {
		this.file = file;
		this.entry = entry;
	}
	@Override
	public String getName() {
		return entry.getName();
	}
	@Override
	public String getCrc() {
		return DigestUtils.toHex(entry.getCrc(), 8);
	}
	@Override
	public String getMd5() {
		if(md5 == null) {
			try {
				md5 = DigestUtils.toHex(DigestUtils.digest(file.getInputStream(entry), "MD5", true));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return md5;
	}
	@Override
	public String getSha1() {
		if(sha1 == null) {
			try {
				sha1 = DigestUtils.toHex(DigestUtils.digest(file.getInputStream(entry), "SHA-1", true));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sha1;
	}
	@Override
	public long getSize() {
		return entry.getSize();
	}
	
}
