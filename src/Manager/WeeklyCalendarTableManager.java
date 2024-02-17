package Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.util.*;

import java.time.*;

public class WeeklyCalendarTableManager {
	// 현재 출력중인 년, 월, 일 저장, 이에 기반하여 Table 생성 후 MainFrame으로 전달
	static LocalDate now = LocalDate.now();

	static final int realYear = now.getYear();
	static final int realMonth = now.getMonthValue() - 1;
	static final int realDay = now.getDayOfMonth();
    public final int countRow = 13;
    final int countCol = 8;
	public int currentYear = realYear;
	public int currentMonth = realMonth;
	public int currentDay = realDay;

	String[] Days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	public static String destDirectory = System.getProperty("user.home") + "/Desktop/tasks";

	public JTable calendarTable;
	public DefaultTableModel tm;

	public WeeklyCalendarTableManager() {
		tm = new DefaultTableModel() {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		calendarTable = new JTable(tm);

		calendarTable.setDefaultRenderer(Object.class, new CustomCellRenderer());

		// TableModel에 열 제목 추가 : Sun, Mon, ...
		tm.addColumn("Times");
		for (int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);

		// 행 개수 2, 열 개수 7로 설정
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		refreshTable(currentYear, currentMonth, currentDay);

		// 행 높이 60픽셀로 설정
        for (int i = 1; i < countRow; i++)
            calendarTable.setRowHeight(i, 60);
        
		calendarTable.getTableHeader().setReorderingAllowed(false);
		calendarTable.getTableHeader().setResizingAllowed(false);

		calendarTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (row == 0)
                    return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                else
                    return super.getTableCellRendererComponent(table, String.format("%02d:00 - %02d:00", (row - 1) * 2, (row - 1) * 2 + 2), isSelected, hasFocus, row, column);
            }
        });
	}

	public String getYearText() {
		return Integer.toString(currentYear);
	}

	public void refreshTable(int year, int month, int day) { // Table 각 셀의 값을 변경된 currentYear, currentMonth에 따라 refresh
		currentYear = year;
		currentMonth = month;
		currentDay = day;
		initTable();
	//	readTask();
	}

	public void refreshTable(boolean next) { // next가 true라면 현재 주의 다음 주, false라면 이전 주로 가서 refresh
		LocalDate current = LocalDate.of(currentYear, currentMonth + 1, currentDay);
		if (next) {
			current = current.plusWeeks(1);
		} else {
			current = current.minusWeeks(1);
		}
		currentYear = current.getYear();
		currentMonth = current.getMonthValue() - 1;
		currentDay = current.getDayOfMonth();
		initTable();
	//	readTask();
	}

	public void initTable() {
		// Table 각 셀을 변경된 currentMonth, currentYear에 따라 초기화
		for (int i = 0; i < countRow; i++)
			for (int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);

		LocalDate current = LocalDate.of(currentYear, currentMonth + 1, currentDay);
		LocalDate startOfWeek = current
				.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));

		for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            tm.setValueAt(day.getDayOfMonth(), 0, i + 1);
        }
        for (int i = 0; i < countRow; i++) {
            tm.setValueAt(String.format("%02d:00 - %02d:00", i * 2, i * 2 + 2), i, 0);
        }
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