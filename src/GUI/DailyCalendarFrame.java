package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import Manager.*;
// 그 날짜에 해당하는 파일 읽어서 Schedule들 저장

public class DailyCalendarFrame extends JFrame {
	DailyTableManager daily;
	public DailyCalendarFrame(LocalDate date) {
		daily = new DailyTableManager(date);
		
		setTitle(date.toString());
		setSize(800, 700);
		setLocation(560, 190);
	
		daily.table.addMouseListener(new CellSelected());
		
		add(new JScrollPane(daily.table));
		setVisible(true);
	}
	
	class CellSelected extends MouseAdapter{
		Point p;
		int row, col;
		
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				p = e.getPoint();
				col = daily.table.columnAtPoint(p);
				row = daily.table.rowAtPoint(p);
				new DailyScheduleFrame(daily.getSchedule(col), daily);
			}
		}
	}
	
}
