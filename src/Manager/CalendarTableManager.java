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
	final int countRow = 6;
	final int countCol = 7;
	public int currentYear = realYear;
	public int currentMonth = realMonth;
	
	String[] Months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep", "Oct.", "Nov.", "Dec." };
	String[] Days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	String destDirectory = System.getProperty("user.home")+"\\Desktop\\tasks";
	
	public JTable calendarTable;
	public DefaultTableModel tm;
	
	public CalendarTableManager() {
		tm = new DefaultTableModel() { public boolean isCellEditable(int row, int col) { return false; }};
		calendarTable = new JTable(tm);
		
		// TableModel에 열 제목 추가 : Sun, Mon, ...
		for(int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);
		
		// 행 개수 6, 열 개수 7로 설정
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		initTable();
		
		// 행 높이 60픽셀로 설정
		calendarTable.setRowHeight(60);
		
	}
	
	public String getMonthText() { return Months[currentMonth]; }
	public String getYearText() { return Integer.toString(currentYear); }
	
	public void refreshTable(int year, int month) { // Table 각 셀의 값을 변경된 currentYear, currentMonth에 따라 refresh
		currentYear = year;
		currentMonth = month;
		initTable();
		// 텍스트 파일에서 task 정보 가져오는 코드 추가
	//	readTask();
	}
	public void refreshTable(boolean next) { // next가 true라면 현재 월의 다음 월, false라면 이전 월로 가서 refresh
		if(next) {
			if(currentMonth == 11) {
				currentMonth = 0;
				currentYear++;
			} else currentMonth++;
		} else {
			if(currentMonth == 0) {
				currentMonth = 11;
				currentYear--;
			} else currentMonth--;
		}
		initTable();
		readTask();
	}
	
	public void initTable() {
		// Table 각 셀을 변경된 currentMonth, currentYear에 따라 초기화
		for(int i = 0; i < 6; i++)
			for(int j = 0; j < 7; j++)
				tm.setValueAt(null, i, j);
		
		int nod, som;
		GregorianCalendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); // 출력하려는 달의 최대 일 수 저장
		som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장
		
		for(int i = 1; i <= nod; i++) {
			int row = (i+som-2)/7;
			int column = (i+som-2)%7;
			tm.setValueAt(i, row, column);
		}
		
	}
	
	public void writeTask(String task, int row, int col) {
		try {
			FileWriter fw;
			String fileName = currentYear+"."+currentMonth+"."+row+"."+col+".txt";
			File dest = new File(destDirectory+"\\"+fileName);
			fw = new FileWriter(dest);
			fw.write(task);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("IO Exception Caught");
		}
		System.out.println("\""+task+"\"wrote at ("+row+","+col+")");
	}
	
	public void readTask() {
		// destDirectory에 있는 모든 파일 검사 혹은 모든 행, 렬 검사
		File[] list = new File(destDirectory).listFiles();
		int fileYear;
		int fileMonth;
		int fileRow;
		int fileCol;
		for(int i = 0; i < list.length; i++) {
			String[] parts = list[i].getName().trim().split(".");
			if(parts.length == 4) {
				fileYear = Integer.parseInt(parts[0].trim());
				fileMonth = Integer.parseInt(parts[1].trim());
				if(fileYear != currentYear || fileMonth != currentMonth) continue;
				fileRow = Integer.parseInt(parts[2].trim());
				fileCol = Integer.parseInt(parts[3].trim());
			} else {
				System.out.println("Invalid FileName :"+list[i].getName());
			}
		}
	}
	
	
}


