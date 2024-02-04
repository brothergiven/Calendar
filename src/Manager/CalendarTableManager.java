package Manager;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.util.*;

import java.time.*;

public class CalendarTableManager {
	// 현재 출력중인 년, 달, 월 저장, 이에 기반하여 Table 생성 후 MainFrame으로 전달
	static LocalDate now = LocalDate.now();

	static final int realYear = now.getYear();
	static final int realMonth = now.getMonthValue() - 1;
	final int countRow = 12;
	final int countCol = 7;
	public int currentYear = realYear;
	public int currentMonth = realMonth;

	String[] Months = { "Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep", "Oct.", "Nov.", "Dec." };
	String[] Days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	public static String destDirectory = System.getProperty("user.home") + "/Desktop/tasks";

	public JTable calendarTable;
	public DefaultTableModel tm;

	public CalendarTableManager() {
		tm = new DefaultTableModel() {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		calendarTable = new JTable(tm);

		calendarTable.setDefaultRenderer(Object.class, new CustomCellRenderer());

		// TableModel에 열 제목 추가 : Sun, Mon, ...
		for (int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);

		// 행 개수 6, 열 개수 7로 설정
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		refreshTable(currentYear, currentMonth);

		// 행 높이 60픽셀로 설정
		for (int i = 1; i < countRow; i += 2)
			calendarTable.setRowHeight(i, 80);

		calendarTable.getTableHeader().setReorderingAllowed(false);
		calendarTable.getTableHeader().setResizingAllowed(false);

	}

	public String getMonthText() {
		return Months[currentMonth];
	}

	public String getYearText() {
		return Integer.toString(currentYear);
	}

	public void refreshTable(int year, int month) { // Table 각 셀의 값을 변경된 currentYear, currentMonth에 따라 refresh
		currentYear = year;
		currentMonth = month;
		initTable();
		readTask();
	}

	public void refreshTable(boolean next) { // next가 true라면 현재 월의 다음 월, false라면 이전 월로 가서 refresh
		if (next) {
			if (currentMonth == 11) {
				currentMonth = 0;
				currentYear++;
			} else
				currentMonth++;
		} else {
			if (currentMonth == 0) {
				currentMonth = 11;
				currentYear--;
			} else
				currentMonth--;
		}
		initTable();
		readTask();
	}

	public void initTable() {
		// Table 각 셀을 변경된 currentMonth, currentYear에 따라 초기화
		for (int i = 0; i < countRow; i++)
			for (int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);

		int nod, som;
		GregorianCalendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); // 출력하려는 달의 최대 일 수 저장
		som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장

		for (int i = 1; i <= nod; i++) {
			int row = (i + som - 2) / 7 * 2;
			int column = (i + som - 2) % 7;
			tm.setValueAt(i, row, column);
		}
	}

	public String getTask(int row, int col) throws IOException { // row, col에 해당하는 task 반환
		String task = "";
		File dest = new File(destDirectory + "/" + currentYear + "." + currentMonth + "." + row + "." + col + ".txt");
		if (!dest.exists())
			return task;
		BufferedReader br = new BufferedReader(new FileReader(dest));
		String tmp;
		while ((tmp = br.readLine()) != null) {
			task = task + tmp + "\r\n";
		}
		br.close();
		return task;
	}

	public String getDate(int row, int col) {
		String year = Integer.toString(currentYear);
		String month = getMonthText();
		// currentYear, currentCol에 따라서 몇 월 몇 일인지
		GregorianCalendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
		int som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장
		int day = (row / 2) * 7 + col - som + 2;
		String suffix;
		if (day % 10 == 1 && day / 10 != 1)
			suffix = "st";
		else if (day % 10 == 2 && day / 10 != 1)
			suffix = "nd";
		else if (day % 10 == 3 && day / 10 != 1)
			suffix = "rd";
		else
			suffix = "th";

		return month + " " + day + suffix + ", " + year;

	}

	public void writeTask(String task, int row, int col) {
		try {
			FileWriter fw;
			String fileName = currentYear + "." + currentMonth + "." + row + "." + col + ".txt";
			File dest = new File(destDirectory + "/" + fileName);
			if (task.trim().equals("")) { // task로 빈 문자열이 입력된다면 텍스트 파일삭제.
				// 이때 원래 파일이 존재하지 않을 수도 있는데, 이를 확인하기 위해 모든 파일명을 검사하기 보다
				// 파일을 그대로 덮어써서 생성한 후에 삭제하는것이 낫다고 판단
				if (dest.exists()) {
					System.gc();
					if (dest.delete()) {
						System.out.println(fileName + " deleted");
					} else {
						System.out.println(fileName + " delete failed");
					}
				} else {
					System.out.println(fileName + " doesn't exists");
				}
				return;
			}
			fw = new FileWriter(dest);
			fw.write(task);
			System.out.println("\"" + task + "\"wrote at (" + row + "," + col + ")");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO Exception Caught");
		}

	}

	public void readTask() {
		// destDirectory에 있는 모든 파일 검사 혹은 모든 행, 렬 검사
		File[] list = new File(destDirectory).listFiles();
		StringTokenizer tokens;
		FileReader bf;
		int fileYear;
		int fileMonth;
		int fileRow;
		int fileCol;
		String task;
		try {
			for (int i = 0; i < list.length; i++) {
				task = "";
				String[] parts = list[i].getName().split("\\.");
				System.out.println(list[i].getName() + " length of parts : " + parts.length);
				if (parts.length == 5) {
					bf = new FileReader(list[i]);
					fileYear = Integer.parseInt(parts[0]);
					fileMonth = Integer.parseInt(parts[1]);
					if (fileYear != currentYear || fileMonth != currentMonth)
						continue;
					if ((fileRow = Integer.parseInt(parts[2])) % 2 == 0)
						continue;
					fileCol = Integer.parseInt(parts[3]);
					int intVal;
					while ((intVal = bf.read()) != -1) {
						char c = (char) intVal;
						task = task + c;
					}
					tm.setValueAt(task, fileRow, fileCol);
					bf.close();
				} else {
					System.out.println("Invalid FileName :" + list[i].getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Checked Files : " + list.length);
	}
}
