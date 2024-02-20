package Manager;

import java.util.*;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.*;
import javax.swing.*;
import javax.swing.table.*;

import GUI.DailyCalendarFrame;

public class MonthlyTableManager extends TableManager {
	final int countRow = 12;
	final int countCol = 7;

	
	public LocalDate startOfMonth = showing.withDayOfMonth(1);
	public ArrayList<DailySchedule>[] schedules = new ArrayList [31];
	
	
	public MonthlyTableManager() {
		table.setDefaultRenderer(Object.class, new MonthlyCellRenderer());

		for (int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		for (int i = 1; i < countRow; i += 2)
			table.setRowHeight(i, 80);
		
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.addMouseListener(new CellSelected());
		readSchedule();
		updateTable();

	}

	@Override
	public void refreshTable(boolean next) {
		if(next) {
			showing = showing.plusMonths(1);
		} else {
			showing = showing.minusMonths(1);
		}
		readSchedule();
		updateTable();
	}
	
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
	public void refreshTable() {
		readSchedule();
		updateTable();
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
	public void updateTable() {
		initTable();
		LocalDate startOfMonth = showing.withDayOfMonth(1);
		int maxDays = getMaxDaysOfMonth(startOfMonth);
		for(int i = 0; i < maxDays; i++) {
			ArrayList<DailySchedule> list = schedules[i];
			for(DailySchedule s : list) {
				int row = getRowCol(s.date)[0];
				int col = getRowCol(s.date)[1];
				String value = (String) tm.getValueAt(row, col);
				if(value == null)
					value = s.getMonthlyLine();
				else {
					value += "\n";
					value += s.getMonthlyLine();
				}
				tm.setValueAt(value, row, col);
			}
			
		}
		
	}

	@Override
	public void readSchedule() {
		LocalDate startOfMonth = showing.withDayOfMonth(1);
		LocalDate date;
		int maxDays = getMaxDaysOfMonth(showing);
		for (int i = 0; i < maxDays; i++)
			schedules[i] = new ArrayList<DailySchedule>();
		for (int i = 0; i < maxDays; i++) {
			date = startOfMonth.plusDays(i);
			try {
				File file = new File(destDirectory + "/" + date.toString()+".txt");
				if (file.exists()) {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					while ((line = br.readLine()) != null) {
						DailySchedule schedule = parseLine(line);
						schedule.date = date;
						if (schedule != null) {
							schedules[i].add(schedule);
							System.out.println("schedule read : " + schedule.name + schedule.date);
						}
					}
					br.close();
				} else {
					System.out.println("file doesn't exits : " + date.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	int getMaxDaysOfMonth(LocalDate date) {
        return date.getMonth().length(date.isLeapYear());
    }

	class CellSelected extends MouseAdapter {
		Point p;
		int row, col;

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				p = e.getPoint();
				col = table.columnAtPoint(p);
				row = table.rowAtPoint(p);
				new DailyCalendarFrame(getDate(row, col));
			}
		}
	}

}
