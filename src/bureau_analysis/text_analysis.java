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
				Pattern pattern2 = Pattern.compile("\\d*?(?=���´�������״̬)");
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

				Pattern pattern3 = Pattern.compile("\\d*?(?=�������ڳ���90��)");
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
			// ����Workbook����, ֻ��Workbook����
			// ֱ�Ӵӱ����ļ�����Workbook
			InputStream instream = new FileInputStream(
					"/Users/chenxi/Downloads/credit_final.xls");
			readwb = Workbook.getWorkbook(instream);

			// ��ȡ��һ��Sheet��,Sheet�±��0��ʼ
			Sheet readsheet = readwb.getSheet(0);
			// ��ȡSheet������������������
			int rsColumns = readsheet.getColumns();
			// System.out.println(rsColumns);
			// ��ȡSheet������������������
			int rsRows = readsheet.getRows();
			// System.out.println(rsRows);
			// ��ȡָ����Ԫ��Ķ�������
			// ��23��Ϊcreditcards�������
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
