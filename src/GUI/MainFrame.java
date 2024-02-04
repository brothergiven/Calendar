package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Manager.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFrame extends JFrame {
	static JPanel pnlCalendar, pnlTop;
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JScrollPane stblCalendar;
	static DefaultTableModel mtblCalendar;
	Container thisContainer;
	static CalendarTableManager ctm = new CalendarTableManager();

	public MainFrame() {
		setTitle("Calendar");
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// GUI 전체를 담을 이 컨테이너 : BorderLayout
		thisContainer = getContentPane();
		thisContainer.setLayout(new BorderLayout());

		// 상단 패널
		pnlTop = new JPanel();
		pnlTop.setLayout(new FlowLayout(FlowLayout.CENTER));

		// 상단에 띄울 버튼과 년월
		lblMonth = new JLabel(ctm.getMonthText());
		lblYear = new JLabel(ctm.getYearText());
		btnPrev = new JButton("<<");
		btnNext = new JButton(">>");

		// 상단 패널에 버튼, 년월 추가
		pnlTop.add(btnPrev);
		pnlTop.add(lblMonth);
		pnlTop.add(lblYear);
		pnlTop.add(btnNext);

		// 달력 테이블을 위한 선언
		tblCalendar = ctm.calendarTable;
		stblCalendar = new JScrollPane(tblCalendar);

		// 이전, 다음 버튼 액션리스너 추가
		btnPrev.addActionListener(new Prev_Action());
		btnNext.addActionListener(new Next_Action());

		tblCalendar.addMouseListener(new CellSelected()); // 셀 선택 후 조작을 위한 Listener

		thisContainer.add(pnlTop, BorderLayout.NORTH);
		thisContainer.add(stblCalendar, BorderLayout.CENTER);
		setVisible(true);
	}

	class Prev_Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ctm.refreshTable(false);
			lblMonth.setText(ctm.getMonthText());
			lblYear.setText(ctm.getYearText());
		}
	}

	class Next_Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ctm.refreshTable(true);
			lblMonth.setText(ctm.getMonthText());
			lblYear.setText(ctm.getYearText());
		}
	}

	public static void main(String[] args) {
		new MainFrame();
	}

	class CellSelected extends MouseAdapter {
		Point p;
		int row, col;

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				p = e.getPoint();
				col = tblCalendar.columnAtPoint(p);
				row = tblCalendar.rowAtPoint(p);
				// TaskInput OPEN
				// ctm에서 getTask, 새로 입력된 Task 반환하는 TASKINPUT 함수
				// TaskINPUT 함수 내에서 writeTASK 호출
				// 근데 그 task를
				try {
					if (row % 2 != 1)
						return;
					new TaskInput(ctm, tblCalendar, row, col);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}