package Manager;

import javax.swing.*;
import javax.swing.table.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;

public abstract class TableManager {
	// 달력 매니저, 테이블로 바꿔줌
	// 주간 달력, 월간 달력으로 넘기기 위함
	public LocalDate showing = LocalDate.now();
	
	public final int realYear = showing.getYear();
	public final int realMonth = showing.getMonthValue() - 1;



	public String[] Days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	public String[] Months = { "Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep", "Oct.", "Nov.",
			"Dec." };
	static public String destDirectory = System.getProperty("user.home") + "/Desktop/tasks";

	public DefaultTableModel tm = new DefaultTableModel() {
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	};
	
	public JTable table = new JTable(tm);


	public String getMonthText() {
		return Months[showing.getMonthValue() - 1];
	}

	public String getYearText() {
		return Integer.toString(showing.getYear());
	}
	
	abstract public void refreshTable(boolean next);
	
	public abstract void updateTable();

	public abstract void readSchedule();
	
	DailySchedule parseLine(String line) {
		DailySchedule schedule = new DailySchedule(showing);
		
		String[] parts = line.split("%");
		if (parts.length != 4)
			return null;
		schedule.name = parts[0];
		schedule.content = parts[1];
		schedule.start = Integer.parseInt(parts[2]);
		parts[3] = parts[3].substring(0, parts[3].length()); //".txt"
		schedule.end = Integer.parseInt(parts[3]);
		System.out.println("Parsing Line OF " + showing + schedule);
		return schedule;
	}
	
	
	public abstract LocalDate getDate(int row, int col);

	public abstract int[] getRowCol(LocalDate date);

}
