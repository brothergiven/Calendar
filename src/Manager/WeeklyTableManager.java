package Manager;

import javax.swing.*;
import javax.swing.table.*;

import GUI.DailyCalendarFrame;
import Manager.MonthlyTableManager.CellSelected;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.*;

import java.io.*;
import java.util.*;
// 현재 출력중인 주 파일만 읽어서 schedule 객체로 파싱 후 저장

public class WeeklyTableManager extends TableManager {
	public final int countRow = 13;
	public final int countCol = 8;
	
	public LocalDate startOfWeek = showing.with(DayOfWeek.SUNDAY).minusWeeks(1);
	public ArrayList<DailySchedule>[] schedules = new ArrayList[7]; // 그 주 7일의 스케줄 ArrayList

	public WeeklyTableManager() {
		tm.addColumn("Times");
		for (int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);

		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);

		for (int i = 1; i < countRow; i++)
			table.setRowHeight(i, 60);

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		table.setDefaultRenderer(Object.class, new WeeklyCellRenderer());
		table.addMouseListener(new CellSelected());
		table.setFillsViewportHeight(true);
		readSchedule();
		updateTable();
		
	}
	
	@Override
	public void refreshTable() {
		readSchedule();
		updateTable();
	}
	
	@Override
	public void updateTable() {
		for (int i = 0; i < countRow; i++)
			for (int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);
		startOfWeek = showing.with(DayOfWeek.SUNDAY).minusWeeks(1);

		for (int i = 0; i < 7; i++) { // 일 저장
			LocalDate day = startOfWeek.plusDays(i);
			tm.setValueAt(day.getDayOfMonth(), 0, i + 1);
		}
		for (int i = 1; i <= 12; i++) { // 시간 저장
			tm.setValueAt(String.format("Time %d\n %02d:00-%02d:00", i, (i - 1) * 2, i * 2), i, 0);
		}

		for (int i = 0; i < 7; i++) {
			ArrayList<DailySchedule> list = schedules[i];
			for (DailySchedule s : list) {
				int col = i + 1;
				for (int time = s.start; time < s.end; time++) {
					int row = time + 1;
					String value = (String) tm.getValueAt(row, col);
					if (value == null)
						value = s.name;
					else {
						value += "\n";
						value += s.name;
					}
					tm.setValueAt(value, row, col);
				}
			}
		}
	}



	@Override
	public void readSchedule() {
		// startOfWeek부터 시작하여서 7일 읽기
		startOfWeek = showing.with(DayOfWeek.SUNDAY).minusWeeks(1);
		LocalDate date;
		for (int i = 0; i < 7; i++)
			schedules[i] = new ArrayList<DailySchedule>();
		for (int i = 0; i < 7; i++) {
			date = startOfWeek.plusDays(i);
			try {
				File file = new File(destDirectory + "/" + date.toString()+".txt");
				if (file.exists()) {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					while ((line = br.readLine()) != null) {
						DailySchedule schedule = parseLine(line);
						if (schedule != null) {
							schedules[i].add(schedule);
							System.out.println("schedule read : " + schedule.name);
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

	@Override
	public LocalDate getDate(int row, int col) {
		return startOfWeek.plusDays(col - 1);
	}

	@Override
	public int[] getRowCol(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshTable(boolean next) {
		if (next) {
			showing = showing.plusWeeks(1);
		} else {
			showing = showing.minusWeeks(1);
		}
		readSchedule();
		updateTable();
	}

    class CellSelected extends MouseAdapter{
    	Point p;
    	int row, col; // row, col에 따른 date 반환해서
    	
    	public void mouseClicked(MouseEvent e) {
    		if(e.getClickCount() == 2) {
    			p = e.getPoint();
    			col = table.columnAtPoint(p);
    			row = table.rowAtPoint(p);
    			new DailyCalendarFrame(getDate(row, col));
    		}
    		
    	}
    }

}
