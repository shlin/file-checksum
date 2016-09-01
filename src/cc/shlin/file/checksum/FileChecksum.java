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

public class FileChecksum {
	private MessageDigest digest;

	public FileChecksum(String algorithm) throws NoSuchAlgorithmException {
		digest = MessageDigest.getInstance(this.getSupportedAlgorithm().contains(algorithm) ? algorithm : "MD5");
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
		StringBuilder sb = new StringBuilder();

		for (byte b : this.getChecksumBytes(file)) {
			sb.append(String.format("%02X", b));
		}

		return sb.toString();
	}

	public String getChecksumString(String text) throws IOException {
		StringBuilder sb = new StringBuilder();

		for (byte b : this.getChecksumBytes(text)) {
			sb.append(String.format("%02X", b));
		}

		return sb.toString();
	}

	public byte[] getChecksumBytes(File file) throws IOException {
		this.calcFileChecksum(file);
		return this.digest.digest();
	}

	public byte[] getChecksumBytes(String text) throws IOException {
		this.calcTextChecksum(text);
		return this.digest.digest();
	}

	private void calcFileChecksum(File file) throws IOException {
		byte[] buff = new byte[1024];

		this.reset();
		InputStream in = new FileInputStream(file);
		DigestInputStream stream = new DigestInputStream(in, this.digest);

		while (stream.read(buff) != -1)
			;
		stream.close();
		in.close();
	}

	private void calcTextChecksum(String text) throws IOException {
		byte[] buff = new byte[1024];

		this.reset();
		InputStream in = new ByteArrayInputStream(text.getBytes());
		DigestInputStream stream = new DigestInputStream(in, this.digest);

		while (stream.read(buff) != -1)
			;
		stream.close();
		in.close();
	}
}
