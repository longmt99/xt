package xt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class App {
	private static final String INPUT = "input";
	private static final String CONFIG = "config.txt";
	private static final String OUTPUT = "output.json";
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
		if (args.length >= 1) {
			size = args[0].length();
		}
		if (args.length == 2) {
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
			if (result != null && (result.getXrate() >= rate || result.getTrate() >= rate)) {
				System.out.print("     " + result);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static void storeOutput() throws Exception {

		JSONObject obj = new JSONObject();
		// String general
		// ="{'files':'','input-1':'-0','input-2':'0','input-3':'-1','input-4':'0','input-5':false,'input-6':'5','input-7':'5','input-8':true,'input-9':'1','input-10':true,'input-11':'2','input-12':true,'input-13':'3','input-14':true,'input-15':'4','input-16':true,'input-17':'5','manyMoney':false,'lessMoney':true,'manyPerson':false,'lessPerson':false,'lessMoneyManyPerson':false,'lessMoneyLessPerson':false,'number-xucxac-tai':'0,1,5,9','number-xucxac-xiu':'3,4,6,7','endtongxucxac':true,'xucxac1':false,'xucxac2':false,'xucxac3':false,'tongxucxac12':false,'tongxucxac13':false,'tongxucxac23':false,'tongxucxac123':false,'input-100':'(1-3-6),(4-4-6),(1-1-5,2-3-6),(3-5-6,6-6-6)','input-101':'(1-1-1,1-1-2),(1-2-3),(1-2-4,3-4-6),(2-3-5),(1-3-5)','input-18':'5000','input-19':'8','input-20':'99','input-21':'99','guessTo':'95','guessFrom':'55','input-22':false,'input-23':'1','input-24':'1','input-25':false,'undefined':'','input-30':false,'input-31':false,'cStringCustom':'TNXGT?T-XXTT','input-32':false,'input-33':'10','input-34':'1000','input-35':true,'input-36':false,'input-37':'10','input-38':'1','input-39':'100.000','input-102':'100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000','input-40':false,'input-41':'15','input-42':'10','input-43':'0-0','input-44':'0-0','input-45':'99','input-46':false,'input-47':false,'input-48':'10','input-49':'2','input-50':'1.000','input-103':'1000,2100,4200,8500,17200,34700,70200,141800,286500,578800,1169400,2362700,4773600,9644600,19486100,39369800,79543100,160709500,324698800,656024100,1325436500,2677922700,5410496900,10931412100,22085914200','input-51':'10','input-52':'0-0','loaiCau':'11'}";
		JSONObject generalObj = new JSONObject();
		generalObj.put("files", "");
		generalObj.put("input-1", "-0");
		generalObj.put("input-2", "0");
		generalObj.put("input-3", "-1");
		generalObj.put("input-4", "0");
		generalObj.put("input-5", false);
		generalObj.put("input-6", "5");
		generalObj.put("input-7", "5");
		generalObj.put("input-8", true);
		generalObj.put("input-9", "1");
		generalObj.put("input-10", true);
		generalObj.put("input-11", "2");
		generalObj.put("input-12", true);
		generalObj.put("input-13", "3");
		generalObj.put("input-14", true);
		generalObj.put("input-15", "4");
		generalObj.put("input-16", true);
		generalObj.put("input-17", "5");
		generalObj.put("manyMoney", false);
		generalObj.put("lessMoney", true);
		generalObj.put("manyPerson", false);
		generalObj.put("lessPerson", false);
		generalObj.put("lessMoneyManyPerson", false);
		generalObj.put("lessMoneyLessPerson", false);
		generalObj.put("number-xucxac-tai", "0,1,5,9");
		generalObj.put("number-xucxac-xiu", "3,4,6,7");
		generalObj.put("endtongxucxac", true);
		generalObj.put("xucxac1", false);
		generalObj.put("xucxac2", false);
		generalObj.put("xucxac3", false);
		generalObj.put("tongxucxac12", false);
		generalObj.put("tongxucxac13", false);
		generalObj.put("tongxucxac23", false);
		generalObj.put("tongxucxac123", false);
		generalObj.put("input-100", "(1-3-6),(4-4-6),(1-1-5,2-3-6),(3-5-6,6-6-6)");
		generalObj.put("input-101", "(1-1-1,1-1-2),(1-2-3),(1-2-4,3-4-6),(2-3-5),(1-3-5)");
		generalObj.put("input-18", "5000");
		generalObj.put("input-19", "8");
		generalObj.put("input-20", "99");
		generalObj.put("input-21", "99");
		generalObj.put("guessTo", "95");
		generalObj.put("guessFrom", "55");
		generalObj.put("input-22", false);
		generalObj.put("input-23", "1");
		generalObj.put("input-24", "1");
		generalObj.put("input-25", false);
		generalObj.put("undefined", "");
		generalObj.put("input-30", false);
		generalObj.put("input-31", false);
		generalObj.put("cStringCustom", "TNXGT?T-XXTT");
		generalObj.put("input-32", false);
		generalObj.put("input-33", "10");
		generalObj.put("input-34", "1000");
		generalObj.put("input-35", true);
		generalObj.put("input-36", false);
		generalObj.put("input-37", "10");
		generalObj.put("input-38", "1");
		generalObj.put("input-39", "100.000");
		generalObj.put("input-102",
				"100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000,100000");
		generalObj.put("input-40", false);
		generalObj.put("input-41", "15");
		generalObj.put("input-42", "10");
		generalObj.put("input-43", "0-0");
		generalObj.put("input-44", "0-0");
		generalObj.put("input-45", "99");
		generalObj.put("input-46", false);
		generalObj.put("input-47", false);
		generalObj.put("input-48", "10");
		generalObj.put("input-49", "2");
		generalObj.put("input-50", "1.000");
		generalObj.put("input-103",	"1000,2100,4200,8500,17200,34700,70200,141800,286500,578800,1169400,2362700,4773600,9644600,19486100,39369800,79543100,160709500,324698800,656024100,1325436500,2677922700,5410496900,10931412100,22085914200");
		generalObj.put("input-51", "10");
		generalObj.put("input-52", "0-0");
		generalObj.put("loaiCau", "11");
		generalObj.
		obj.put("general", generalObj);

		JSONArray nangCao = new JSONArray();

		int found = 0;
		for (Map.Entry<String, Rate> entry : output.entrySet()) {
			Rate result = entry.getValue();
			if (result.getXrate() >= rate) {
				found++;
				nangCao.add(result.getFeed() + "-X");
				System.out.println("  " + found + "." + result);
			} else if (result.getTrate() >= rate) {
				found++;
				nangCao.add(result.getFeed() + "-T");
				System.out.println("  " + found + "." + result);
			}
		}

		obj.put("nangCao", nangCao);

		try (FileWriter file = new FileWriter(OUTPUT)) {

			file.write(obj.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
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
