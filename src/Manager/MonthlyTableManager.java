package Manager;

import java.util.*;
import java.io.*;
import java.time.*;
import javax.swing.*;
import javax.swing.table.*;

public class MonthlyTableManager extends TableManager {
	final int countRow = 12;
	final int countCol = 7;

	public MonthlyTableManager() {
		table.setDefaultRenderer(Object.class, new MonthlyCellRenderer());

		for (int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		refreshTable();
		for (int i = 1; i < countRow; i += 2)
			table.setRowHeight(i, 80);

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
	}

	@Override
	public void refreshTable(boolean next) {
		if(next) {
			showing = showing.plusMonths(1);
		} else {
			showing = showing.minusMonths(1);
		}

		initTable();
		readTask();
	}
	@Override
	public void refreshTable() {
		initTable();
		readTask();
	}
	
	@Override
	public void initTable() {
		// Row, Col 개수만큼 셀 생성
		for (int i = 0; i < countRow; i++)
			for (int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);
		// 자동으로 달력에 day of month 추가
		int nod, som;
		GregorianCalendar cal = new GregorianCalendar(showing.getYear(), showing.getMonthValue() - 1, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); // 출력하려는 달의 최대 일 수 저장
		som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장

		for (int i = 1; i <= nod; i++) {
			int row = (i + som - 2) / 7 * 2;
			int column = (i + som - 2) % 7;
			tm.setValueAt(i, row, column);
		}
	}

	@Override
	public LocalDate getDate(int row, int col) {
		GregorianCalendar cal = new GregorianCalendar(showing.getYear(), showing.getMonthValue() - 1, 1);
		int som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장
		int day = (row / 2) * 7 + col - som + 2;

		return LocalDate.of(showing.getYear(), showing.getMonthValue(), day);
	}

	// to Row, Col
	@Override
	public int[] getRowCol(LocalDate date) {
		GregorianCalendar cal = new GregorianCalendar(date.getYear(), date.getMonthValue() - 1, 1);
		int firstDay = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장
		
		// 시작 : firstDay
		int row = (date.getDayOfMonth() + firstDay - 2) / 7 * 2 + 1; // 행
		int col = (date.getDayOfMonth() + firstDay - 2) % 7; // 열
		
		
		return new int[] {row, col};
	}

	@Override
	public void writeTask(String task, LocalDate date) {
		try {
			File dest = new File(destDirectory + "/" + date.toString());
			if (task.trim().equals("")) {
				if (dest.exists()) {
					System.gc();
					dest.delete();
				}
				return;
			}
			FileWriter fw = new FileWriter(dest);
			fw.write(task);
			System.out.println(date + " wrote");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readTask() {
		try {
			File[] list = new File(destDirectory).listFiles();
			int fileYear, fileMonth, fileDay;

			for (int i = 0; i < list.length; i++) {
				String[] parts = list[i].getName().split("-");
				if (parts.length == 3) {
					String task = "";
					FileReader fr = new FileReader(list[i]);
					fileYear = Integer.parseInt(parts[0]);
					fileMonth = Integer.parseInt(parts[1]);
					fileDay = Integer.parseInt(parts[2]);
					if (fileYear != showing.getYear() || fileMonth != showing.getMonthValue())
						continue;
					int val;
					while ((val = fr.read()) != -1) {
						task += (char) val;
					}
					int[] rowcol = getRowCol(LocalDate.of(fileYear, fileMonth, fileDay));
					tm.setValueAt(task, rowcol[0], rowcol[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
