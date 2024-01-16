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
	
	String[] Months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep", "Oct.", "Nov.", "Dec." };
	String[] Days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	String destDirectory = System.getProperty("user.home")+"/Desktop/tasks";
	
	public JTable calendarTable;
	public DefaultTableModel tm;
	
	public CalendarTableManager() {
		tm = new DefaultTableModel() { public boolean isCellEditable(int row, int col) { return false; }};
		calendarTable = new JTable(tm);
		
		DaysRenderer daysRenderer = new DaysRenderer();
		calendarTable.setDefaultRenderer(Object.class, daysRenderer);

		// TableModel에 열 제목 추가 : Sun, Mon, ...
		for(int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);
		
		// 행 개수 6, 열 개수 7로 설정
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		refreshTable(currentYear, currentMonth);
		
		// 행 높이 60픽셀로 설정
		for(int i = 1; i < countRow; i+=2)
			calendarTable.setRowHeight(i, 60);
		
		calendarTable.getTableHeader().setReorderingAllowed(false);
		calendarTable.getTableHeader().setResizingAllowed(false);
		
	}
	
	public String getMonthText() { return Months[currentMonth]; }
	public String getYearText() { return Integer.toString(currentYear); }
	
	public void refreshTable(int year, int month) { // Table 각 셀의 값을 변경된 currentYear, currentMonth에 따라 refresh
		currentYear = year;
		currentMonth = month;
		initTable();
		readTask();
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
		for(int i = 0; i < countRow; i++)
			for(int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);
		
		int nod, som;
		GregorianCalendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); // 출력하려는 달의 최대 일 수 저장
		som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장
		
		for(int i = 1; i <= nod; i++) {
			int row = (i+som-2)/7 * 2;
			int column = (i+som-2)%7;
			tm.setValueAt(i, row, column);
		}
		
	}
	
	public void writeTask(String task, int row, int col) {
		try {
			FileWriter fw;
			String fileName = currentYear+"."+currentMonth+"."+row+"."+col+".txt";
			File dest = new File(destDirectory+"/"+fileName);
			if(task.trim().equals("")) { // task로 빈 문자열이 입력된다면 텍스트 파일삭제.
				// 이때 원래 파일이 존재하지 않을 수도 있는데, 이를 확인하기 위해 모든 파일명을 검사하기 보다
				// 파일을 그대로 덮어써서 생성한 후에 삭제하는것이 낫다고 판단
		    	if(dest.exists()){
		    		System.gc();
		    		if(dest.delete()){
		    			System.out.println(fileName+" deleted");
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
			System.out.println("\""+task+"\"wrote at ("+row+","+col+")");
			fw.close();
		} catch(IOException e) {
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
			for(int i = 0; i < list.length; i++) {
				task = "";
				String[] parts = list[i].getName().split("\\.");
				System.out.println(list[i].getName()  +" length of parts : " + parts.length);
				if(parts.length == 5) {
					bf = new FileReader(list[i]);
					fileYear = Integer.parseInt(parts[0]);
					fileMonth = Integer.parseInt(parts[1]);
					if(fileYear != currentYear || fileMonth != currentMonth) continue;
					if(( fileRow = Integer.parseInt(parts[2]) ) % 2 == 0) continue;
					fileCol = Integer.parseInt(parts[3]);
					int intVal;
					while((intVal = bf.read()) != -1) {
					    char c = (char) intVal;
					    task = task + c;
					}
					
					tm.setValueAt(task, fileRow, fileCol);
					bf.close();
					
				} else {
					System.out.println("Invalid FileName :"+list[i].getName());
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Checked Files : "+list.length);
	}
	
	class DaysRenderer extends DefaultTableCellRenderer{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	        if (row % 2 == 0) { // 짝수 번째 줄은 오른쪽 정렬
		        setHorizontalAlignment(JLabel.RIGHT);
		        if(column == 0) { // 일요일 빨간색 글자
		        	setForeground(Color.red);
		        } else if (column == 6) { // 토요일 파란색 글자
		        	setForeground(Color.blue);
		        } else { // 평일 검정색 글자
		        	setForeground(Color.black);
		        }
	        } else { // 홀수 번째 줄은 왼쪽 정렬, 모두 검정색 글자
	        	setHorizontalAlignment(JLabel.LEFT);
	        	setForeground(Color.black);
	        }
	        return this;
	    }
	}
}


