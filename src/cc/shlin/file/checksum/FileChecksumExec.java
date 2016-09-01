package cc.shlin.file.checksum;

import java.io.File;
import java.security.Security;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author shlin
 * @see https://github.com/shlin/file-checksum
 */
public class FileChecksumExec {

	public static void main(String[] args) throws Exception {
		Options options = getOptions();

		CommandLine cmd = new DefaultParser().parse(options, args);
		File file;
		FileChecksum fileChecksum;
		String strDigest = new String();
		String strFile = new String();
		String text = new String();

		for (Option option : cmd.getOptions()) {
			switch (option.getOpt()) {
			case "digest":
				strDigest = cmd.getOptionValue("digest").toUpperCase();

				if (!Security.getAlgorithms("MessageDigest").contains(strDigest)) {
					System.err.println("不支援該演算法");
					System.exit(1);
				}

				break;
			case "digestlist":
				Security.getAlgorithms("MessageDigest").forEach(digest -> System.out.printf("%s ", digest));
				System.exit(0);
			case "file":
				if (new File(cmd.getOptionValue("file")).exists())
					strFile = cmd.getOptionValue("file");
				break;
			case "help":
				printHelp(options);
			case "text":
				text = cmd.getOptionValue("text");
				break;
			default:
				break;
			}
		}

		fileChecksum = new FileChecksum(strDigest);
		if (text.isEmpty() && strFile.isEmpty()) {
			printHelp(options);
		} else if (!strFile.isEmpty()) {
			file = new File(strFile);
			System.out.printf("%s HASH: %s\n", fileChecksum.getAlgorithm(), fileChecksum.getChecksumString(file));
		} else if (!text.isEmpty()) {
			System.out.printf("%s HASH: %s\n", fileChecksum.getAlgorithm(), fileChecksum.getChecksumString(text));
		}
	}

	private static Options getOptions() {
		Options options = new Options();

		options.addOption("digest", true, "雜湊運算演算法，預設：MD5。");
		options.addOption("digestlist", false, "列出支援的雜湊運算演算法");

		options.addOption("file", true, "檔案路徑");
		options.getOption("file").setArgName("file path");

		options.addOption("help", false, "顯示HELP訊息");

		options.addOption("text", true, "計算純文字內容");
		options.getOption("text").setArgName("string");

		return options;
	}

	private static void printHelp(Options options) {
		new HelpFormatter().printHelp("java -jar file-checksum.jar [OPTION]", options);
		System.exit(0);
	}
}
