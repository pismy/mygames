package pismy.mygames.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {
	public static byte[] digest(InputStream in, String algorithm, boolean close) throws NoSuchAlgorithmException, IOException {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = in.read(buf)) >= 0) {
				md.update(buf, 0, len);
			}
			return md.digest();
		} finally {
			if(close)
				in.close();
		}
	}
	private static final String HEX = "0123456789abcdef";
	public static void main(String[] args) {
		System.out.println(toHex(new byte[]{1,2,4,8,16,32,64,125}));
	}

	public static String toHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for(byte b : bytes) {
			sb.append(HEX.charAt((b >> 4) & 0xF));
			sb.append(HEX.charAt(b & 0xF));
		}
		return sb.toString();
	}
	
	public static String toHex(long value, int digits) {
		StringBuffer sb = new StringBuffer();
		for ( int i = 0; i < digits; i++, value >>= 4) // shift by four bits as each char represents 1/2 byte
		{
			sb.insert(0, HEX.charAt((int)(value & 0xF)));
		}
		return sb.toString();
	}

}
