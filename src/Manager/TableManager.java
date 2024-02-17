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



	public String getTask(int row, int col) { // row, col에 해당하는 task 반환
		try {
			String task = "";
			File dest = new File(
					destDirectory + "/" + getDate(row, col).toString());
			if (!dest.exists())
				return task;
			BufferedReader br = new BufferedReader(new FileReader(dest));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				task = task + tmp + "\r\n";
			}
			br.close();
			return task;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	abstract public void refreshTable(boolean next);
	
	abstract public void refreshTable();
	
	public abstract void initTable();

	public abstract void writeTask(String task, LocalDate date);

	public abstract void readTask();

	public abstract LocalDate getDate(int row, int col);

	public abstract int[] getRowCol(LocalDate date);

}
