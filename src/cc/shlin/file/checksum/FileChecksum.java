package cc.shlin.file.checksum;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;

/**
 * @author shlin
 * @see https://github.com/shlin/file-checksum
 */
public class FileChecksum {
	private MessageDigest digest;

	public FileChecksum(String algorithm) {
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException noAlgorithmEx) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	public Set<String> getSupportedAlgorithm() {
		return Security.getAlgorithms("MessageDigest");
	}

	public String getAlgorithm() {
		return this.digest.getAlgorithm();
	}

	public void reset() {
		this.digest.reset();
	}

	public String getChecksumString(File file) throws IOException {
		this.calcChecksum(new FileInputStream(file));
		return this.getByteString(this.digest.digest());
	}

	public String getChecksumString(String text) throws IOException {
		this.calcChecksum(new ByteArrayInputStream(text.getBytes()));
		return this.getByteString(this.digest.digest());
	}

	private void calcChecksum(InputStream in) throws IOException {
		byte[] buff = new byte[4096];

		this.reset();
		DigestInputStream stream = new DigestInputStream(in, this.digest);

		while (stream.read(buff) != -1)
			;
		stream.close();
		in.close();
	}

	private String getByteString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (byte b : bytes)
			sb.append(String.format("%02X", b));

		return sb.toString();
	}
}
