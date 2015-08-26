package bureau_analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class text_analysis {
	// private static String test = null;

	public static Integer[] reg(String str) {
		int overdue_result = 0;
		int DPD90_result = 0;
		Integer[] x = new Integer[2];
		x[0] = 0;
		x[1] = 0;
		Pattern pattern = Pattern.compile("overdue:\\[.*?\\]\\}");
		Matcher match = pattern.matcher(str);
		while (match.find()) {
			// System.out.println(match.group());
			// System.out.println((match.group().length()));
			if (match.group().length() > 11) {
				// System.out.println("overdue!");
				Pattern pattern2 = Pattern.compile("\\d*?(?=个月处于逾期状态)");
				Matcher match2 = pattern2.matcher(str);
				List<String> groupAllList = new ArrayList<String>();
				List<String> groupdpd = new ArrayList<String>();

				while (match2.find()) {
					groupAllList.add(match2.group(0));

				}
				// System.out.println(groupAllList);
				// System.out.println("length="+groupAllList.size());
				if (groupAllList.size() > 1) {
					for (int i = 0; i < groupAllList.size(); i++) {
						// System.out.println(i+"with"+groupAllList.get(i));
						overdue_result = overdue_result
								+ Integer.valueOf(groupAllList.get(i));
						i++;

					}
				}

				// System.out.println("overdue=" + overdue_result);

				Pattern pattern3 = Pattern.compile("\\d*?(?=个月逾期超过90天)");
				Matcher match3 = pattern3.matcher(str);
				while (match3.find()) {
					groupdpd.add(match3.group());

				}
				if (groupdpd.size() > 1) {
					for (int i = 0; i < groupdpd.size(); i++) {
						DPD90_result = DPD90_result
								+ Integer.valueOf(groupdpd.get(i));
						i++;
					}
				}

				// System.out.println("dpd90_result=" + DPD90_result);
				x[0] = overdue_result;
				x[1] = DPD90_result;
				return x;

			} else {
				// System.out.println("unoverdue!");
				// System.out.println("overdue=" + overdue_result);
				// System.out.println("dpd90_result=" + DPD90_result);
				x[0] = overdue_result;
				x[1] = DPD90_result;
				return x;
			}
		}
		return x;

	}

	private static int Integer(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void main(String[] args) {
		jxl.Workbook readwb = null;
		String test = null;
		try {
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			InputStream instream = new FileInputStream(
					"/Users/chenxi/Downloads/credit_final.xls");
			readwb = Workbook.getWorkbook(instream);

			// 获取第一张Sheet表,Sheet下表从0开始
			Sheet readsheet = readwb.getSheet(0);
			// 获取Sheet表中所包含的总列数
			int rsColumns = readsheet.getColumns();
			// System.out.println(rsColumns);
			// 获取Sheet表中所包含的总行数
			int rsRows = readsheet.getRows();
			// System.out.println(rsRows);
			// 获取指定单元格的对象引用
			// 第23列为creditcards逾期情况
			FileWriter filewriter = new FileWriter(
					"/Users/chenxi/Downloads/credit_final_result.txt");
			for (int i = 0; i < rsRows; i++) {
				for (int j = 0; j < rsColumns; j++) {
					if (j == 22) {
						Cell cell = readsheet.getCell(j, i);
						// System.out.print(cell.getContents() + " ");
						test = cell.getContents();
						// System.out.println(test);
						Integer[] result = reg(test);
						System.out.println("row:" + i + ";overdue:" + result[0]
								+ ";dpd:" + result[1]);
						filewriter.write(i + ";" + result[0] + ";" + result[1]
								+ "\n");
					}
				}
			}
			filewriter.flush();
			filewriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readwb.close();
		}
	}

}
