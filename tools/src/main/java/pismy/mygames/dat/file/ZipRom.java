package pismy.mygames.dat.file;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import pismy.mygames.dat.IRom;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

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
	private static final String HEX = "0123456789abcdef";
	@Override
	public String getCrc() {
		long value = entry.getCrc();
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < 8; i++, value >>= 4) // shift by four bits as each char represents 1/2 byte
		{
			sb.insert(0, HEX.charAt((int)(value & 0xF)));
		}
		return sb.toString();
	}
	@Override
	public String getMd5() {
		if(md5 == null) {
			try {
				md5 = BaseEncoding.base16().encode(Hashing.md5().hashBytes(ByteStreams.toByteArray(file.getInputStream(entry))).asBytes());
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
				md5 = BaseEncoding.base16().encode(Hashing.sha1().hashBytes(ByteStreams.toByteArray(file.getInputStream(entry))).asBytes());
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
