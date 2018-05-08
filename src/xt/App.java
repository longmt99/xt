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
		System.out.println("||                              PHÂN TÍCH DỮ LIỆU XT                        ||");
		System.out.println("||                                       + Tổng hợp dữ liệu output          ||");
		System.out.println("||                                       + Phân tích chuỗi XT theo input    ||");
		System.out.println("==============================================================================");
		System.out.println("||            1.0+ $java -jar xt.jar                                        ||");
		System.out.println("||            2.1+ $java -jar xt.jar XXXX                                   ||");
		System.out.println("||            2.2+ $java -jar xt.jar XXXX 65                                ||");
		System.out.println("==============================================================================\n");
		loadConfig();
		System.out.println("1. Nạp cấu hình config ");
		if(args.length>=1){
			size = args[0].length();
		}
		if(args.length==2){
			rate = Integer.parseInt(args[1]);
		}
		System.out.println("\n    ĐỘ DÀI CHUỖI = [" + size + "]\n    TỈ LỆ CHẤP NHẬN = [" + rate + "]");

		buffer = "";
		List<String> list = listData(new File(INPUT));
		System.out.println("\n2. Dữ liệu đầu vào input data " + list + "");
		for (String name : list) {
			buffer += readFile(INPUT + File.separator + name);
		}
		buffer = StringUtils.escape(buffer);
		System.out.println("\n3. Chuỗi dữ liệu sẽ chạy");
		System.out.println("  ===== " + buffer);
		int start = 0;
		int end = 0;
		System.out.println("\n4. Bắt đầu chạy dữ liệu ");
		if (args.length < 1) {
			while (end <= buffer.length() - 2) {
				end = start + size - 1;
				String feed = buffer.substring(start, end);
				if (output.containsKey(feed)) {
					// Have existed
					++start;
					continue;
				}
				System.out.println("\n  ===== Chuỗi thử ban đầu: " + feed);
				Rate result = findString(feed);
				if (result != null) {
					output.put(result.getFeed(), result);
				}
				++start;
			}
			System.out.println("\n5. Lọc kết quả dữ liệu OUTPUT data " + OUTPUT);
			storeOutput();
		} else {
			String feed = args[0];
			System.out.println("\n  ===== Chuỗi thử ban đầu: " + feed);
			Rate result = findString(feed);
			System.out.println("\n5. Phân tích chuỗi XT theo input " + feed);
			System.out.println("     Kết quả ");
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
			System.out.println("     Dữ liệu không đủ so sánh " + feedX + "(" + xCount + ")");
			return result;
		}
		if (tCount == 0) {
			System.out.println("      Dữ liệu không đủ so sánh " + feedT + "(" + tCount + ")");
			return result;
		}
		int total = xCount + tCount;
		int xRate = xCount * 100 / total;
		int tRate = tCount * 100 / total;
		result = new Rate(feed, xCount, tCount, xRate, tRate);
		System.out.println("     Kết quả tìm chuỗi: " + result);
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
