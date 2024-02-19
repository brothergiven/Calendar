package Manager;

import java.time.LocalDate;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.Component;
import java.io.*;
import java.util.*;

public class DailyTableManager {
	LocalDate showing;
	public ArrayList<DailySchedule> schedules = new ArrayList<DailySchedule>();

	public final int countRow = 12;
	public final int countCol = 5;

	static public String destDirectory = System.getProperty("user.home") + "/Desktop/tasks";

	public DefaultTableModel tm = new DefaultTableModel() {
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	};

	public JTable table = new JTable(tm);

	public DailyTableManager(LocalDate date) {
		showing = date;
		tm.addColumn("Times");
		for (int i = 1; i <= 4; i++)
			tm.addColumn(i);
		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);

		table.setRowHeight(60);

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		
		table.setDefaultRenderer(Object.class, new DailyCellRenderer());
		
		readSchedule(); // 스케쥴 읽어서 프로그램에 저장
		updateTable(); // 읽은 스케줄 바탕으로 테이블 업데이트
	}

	public DailySchedule getSchedule(int col) { // col에 따른 새로운 스케쥴 생성
		DailySchedule schedule;
		if (schedules.size() < col) {
			schedule = new DailySchedule(showing);
			schedules.add(schedule);
		} else {
			schedule = schedules.get(col - 1);
		}
		return schedule;
	}

	public void updateTable() {
		for (int i = 0; i < countRow; i++)
			for (int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);

		for (int i = 0; i < 12; i++) {
			tm.setValueAt(String.format("Time %d\n %02d:00-%02d:00", i + 1, i * 2, (i + 1) * 2), i, 0);
		}
		for (int i = 1; i <= schedules.size(); i++) { // schedule size가 col
			// row : start time ~ end time
			DailySchedule s = schedules.get(i - 1);
			System.out.println(s);
			for (int j = s.start; j < s.end; j++) {
				tm.setValueAt(s.name, j, i);
			}
		}
		writeSchedule();
	}

	public void readSchedule() {
		// 그 날의 schedule 읽어서
		try {
			File file = new File(destDirectory + "/" + showing.toString()+".txt");
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null) {
					DailySchedule schedule = parseLine(line);
					if (schedule != null) {
						schedules.add(schedule);
						System.out.println("schedule read : " + schedule.name);
					}

				}
				br.close();
			} else {
				System.out.println("file doesn't exits : " + showing.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	DailySchedule parseLine(String line) {
		DailySchedule schedule = new DailySchedule(showing);
		String[] parts = line.split("%");
		if (parts.length != 4)
			return null;
		schedule.name = parts[0];
		schedule.content = parts[1];
		schedule.start = Integer.parseInt(parts[2]);
		schedule.end = Integer.parseInt(parts[3]);
		return schedule;
	}

	public void writeSchedule() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(destDirectory + "/" + showing.toString() + ".txt", false));
			for (DailySchedule s : schedules) {
				bw.write(s.toString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	class DailyCellRenderer extends JTextArea implements TableCellRenderer {
		public DailyCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value != null)
				setText(value.toString());
			else
				setText("");
			return this;
		}
	}
}
