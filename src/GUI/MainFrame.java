package GUI;

import java.util.*;
import javax.swing.*;

import Manager.*;

import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class MainFrame extends JFrame {
	TableManager[] managers = new TableManager[] { new MonthlyTableManager(), new WeeklyTableManager() };
	int showing = 0;

	public MainFrame() {
		setTitle("Calendar");
		setLocation(460, 190);
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 상단 패널
		JPanel topPanel = new JPanel(new FlowLayout());
		JPanel tablePanel = new JPanel(new BorderLayout());
		JLabel monthLabel = new JLabel(managers[showing].getMonthText());
		JLabel yearLabel = new JLabel(managers[showing].getYearText());

		JButton prevBtn = new JButton("<<");
		JButton nextBtn = new JButton(">>");
		
		
		
		// 콤보박스
		JComboBox<String> comboBox = new JComboBox<String>(new String[] { "Monthly", "Weekly" });
		comboBox.addActionListener(e -> {
			showing = comboBox.getSelectedIndex();
			tablePanel.removeAll();
			managers[showing].refreshTable();
			monthLabel.setText(managers[showing].getMonthText());
			yearLabel.setText(managers[showing].getYearText());
			tablePanel.add(new JScrollPane(managers[showing].table), BorderLayout.CENTER);
		});
		
		// 상단 패널에 버튼, 년월 추가
		topPanel.add(prevBtn);
		topPanel.add(monthLabel);
		topPanel.add(yearLabel);
		topPanel.add(nextBtn);
		topPanel.add(comboBox);
		
		// 이전, 다음 버튼 액션리스너 추가
		prevBtn.addActionListener(e -> {
			managers[showing].refreshTable(false);
			monthLabel.setText(managers[showing].getMonthText());
			yearLabel.setText(managers[showing].getYearText());
		});
		nextBtn.addActionListener(e -> {
			managers[showing].refreshTable(true);
			monthLabel.setText(managers[showing].getMonthText());
			yearLabel.setText(managers[showing].getYearText());
		});
		
		add(topPanel, BorderLayout.NORTH);

		tablePanel.add(new JScrollPane(managers[showing].table), BorderLayout.CENTER);
		add(tablePanel);
		setVisible(true);
	}

}
