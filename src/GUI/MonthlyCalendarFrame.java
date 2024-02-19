package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


import Manager.*;


public class MonthlyCalendarFrame extends JFrame {
	MonthlyTableManager monthly = new MonthlyTableManager();

	public MonthlyCalendarFrame() {
		setTitle("Monthly Calendar");
		setLocation(460, 190);
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// GUI 전체를 담을 이 컨테이너 : BorderLayout
		setLayout(new BorderLayout());

		// 상단 패널
		JPanel topPanel = new JPanel(new FlowLayout());

		// 상단에 띄울 버튼과 년월
		JLabel monthLabel = new JLabel(monthly.getMonthText());
		JLabel yearLabel = new JLabel(monthly.getYearText());
		JButton prevBtn = new JButton("<<");
		JButton nextBtn = new JButton(">>");

		// 상단 패널에 버튼, 년월 추가
		topPanel.add(prevBtn);
		topPanel.add(monthLabel);
		topPanel.add(yearLabel);
		topPanel.add(nextBtn);

		// 이전, 다음 버튼 액션리스너 추가
		prevBtn.addActionListener(e -> {
			monthly.refreshTable(false);
			monthLabel.setText(monthly.getMonthText());
			yearLabel.setText(monthly.getYearText());
		});
		nextBtn.addActionListener(e -> {
			monthly.refreshTable(true);
			monthLabel.setText(monthly.getMonthText());
			yearLabel.setText(monthly.getYearText());
		});

		monthly.table.addMouseListener(new CellSelected()); // 셀 선택 후 조작을 위한 Listener

		add(topPanel, BorderLayout.NORTH);
		add(new JScrollPane(monthly.table), BorderLayout.CENTER);
		setVisible(true);
	}

	class CellSelected extends MouseAdapter {
		Point p;
		int row, col;

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				p = e.getPoint();
				col = monthly.table.columnAtPoint(p);
				row = monthly.table.rowAtPoint(p);
				new DailyCalendarFrame(monthly.getDate(row, col));
			}
		}
	}
}