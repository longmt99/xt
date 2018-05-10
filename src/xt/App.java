package xt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class App {
	private static final String INPUT = "input";
	private static final String CONFIG = "config.txt";
	private static final String OUTPUT = "output.txt";
	private static int size = 14;
	private static int rate = 80;
	private static String buffer = "";
	private static Map<String, Rate> output = new HashMap<String, Rate>();

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		System.out.println("==============================================================================");
		System.out.println("||                              PHAN TICH DU LIEU XT                        ||");
		System.out.println("||                                       + Tong hop du lieu output          ||");
		System.out.println("||                                       + Phan tich chuoi XT theo input    ||");
		System.out.println("==============================================================================");
		System.out.println("||            1.0+ $java -jar xt.jar                                        ||");
		System.out.println("||            2.1+ $java -jar xt.jar XXXX                                   ||");
		System.out.println("||            2.2+ $java -jar xt.jar XXXX 65                                ||");
		System.out.println("==============================================================================\n");
		loadConfig();
		System.out.println("1. Nap cau hinh config ");
		if(args.length>=1){
			size = args[0].length();
		}
		if(args.length==2){
			rate = Integer.parseInt(args[1]);
		}
		System.out.println("\n    DO DAI CHUOI = [" + size + "]\n    TI LE CHAP NHAN = [" + rate + "]");

		buffer = "";
		List<String> list = listData(new File(INPUT));
		System.out.println("\n2. Du lieu dau vao input data " + list + "");
		for (String name : list) {
			buffer += readFile(INPUT + File.separator + name);
		}
		buffer = StringUtils.escape(buffer);
		System.out.println("\n3. Chuoi du lieu se chay");
		System.out.println("  ===== " + buffer);
		int start = 0;
		int end = 0;
		System.out.println("\n4. Bat dau chay du lieu ");
		if (args.length < 1) {
			while (end <= buffer.length() - 2) {
				end = start + size - 1;
				String feed = buffer.substring(start, end);
				if (output.containsKey(feed)) {
					// Have existed
					++start;
					continue;
				}
				System.out.println("\n  ===== Chuoi thu ban dau: " + feed);
				Rate result = findString(feed);
				if (result != null) {
					output.put(result.getFeed(), result);
				}
				++start;
			}
			System.out.println("\n5. Loc ket qua du lieu OUTPUT data " + OUTPUT);
			storeOutput();
		} else {
			String feed = args[0];
			System.out.println("\n  ===== Chuoi thu ban dau: " + feed);
			Rate result = findString(feed);
			System.out.println("\n5. Phan tich chuoi XT theo input " + feed);
			System.out.println("     Ket qua ");
			if (result!=null && (result.getXrate() >= rate || result.getTrate() >= rate)) {
				System.out.print("     " + result);
			}
		}

	}

	private static void storeOutput() throws Exception {
		Properties prop = new Properties();
		OutputStream out = null;
		try {
			out = new FileOutputStream(OUTPUT);
			int found = 0;
			for (Map.Entry<String, Rate> entry : output.entrySet()) {
				Rate result = entry.getValue();
				if (result.getXrate() >= rate || result.getTrate() >= rate) {
					found++;
					prop.setProperty(String.valueOf(found), result.toOuput());
					System.out.println("  " + found + "." + result);
				}
			}
			prop.store(out, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			out.close();
		}

	}

	/**
	 * X or T or ""
	 * 
	 * @param feed
	 * @param rate
	 * @return
	 */
	public static Rate findString(String feed) {
		Rate result = null;

		String feedX = feed + "X";
		String feedT = feed + "T";
		int xCount = StringUtils.countMatches(buffer, feedX);
		int tCount = StringUtils.countMatches(buffer, feedT);
		if (xCount == 0) {
			System.out.println("     Du lieu khong du so sanh " + feedX + "(" + xCount + ")");
			return result;
		}
		if (tCount == 0) {
			System.out.println("      Du lieu khong du so sanh " + feedT + "(" + tCount + ")");
			return result;
		}
		int total = xCount + tCount;
		int xRate = xCount * 100 / total;
		int tRate = tCount * 100 / total;
		result = new Rate(feed, xCount, tCount, xRate, tRate);
		System.out.println("     Ket qua tim chuoi: " + result);
		return result;

	}

	public static void loadConfig() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(CONFIG);
			prop.load(input);
			size = Integer.parseInt(prop.getProperty("size"));
			rate = Integer.parseInt(prop.getProperty("rate"));
		} catch (Exception ex) {
			System.out.println("Get DEFAULT config");
		}
	}

	public static List<String> listData(final File folder) {
		List<String> list = new ArrayList<>();
		for (final File file : folder.listFiles()) {
			String name = file.getName();
			list.add(name);
		}
		return list;
	}

	public static String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
}
