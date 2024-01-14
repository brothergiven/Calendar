package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Calendar;
public class MainFrame extends JFrame{
	static JPanel pnlCalendar, pnlTop;
	
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JScrollPane stblCalendar;
	static DefaultTableModel mtblCalendar;
	Container thisContainer;
	int currentYear = 2024;
	int currentMonth = 0;
	static String[] Months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sep", "Oct.", "Nov.", "Dec." };
	static String[] Days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	public MainFrame() {
		setTitle("Calendar");
		setSize(500, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// GUI 전체를 담을 이 컨테이너 : BorderLayout		
		thisContainer = getContentPane();
		thisContainer.setLayout(new BorderLayout());
		
		// 상단 패널
		pnlTop = new JPanel();
		pnlTop.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// 상단에 띄울 버튼과 년월
		lblMonth = new JLabel(Integer.toString(currentYear));
		lblYear = new JLabel(Months[currentMonth]);
		btnPrev = new JButton("<<");
		btnNext = new JButton(">>");
		
		// 상단 패널에 버튼, 년월 추가
		pnlTop.add(btnPrev);
		pnlTop.add(lblMonth);
		pnlTop.add(lblYear);
		pnlTop.add(btnNext);
		
		// 달력 테이블을 위한 선언
		mtblCalendar = new DefaultTableModel() { public boolean isCellEditable(int row, int col) {return false;}};
		tblCalendar = new JTable(mtblCalendar);
		stblCalendar = new JScrollPane(tblCalendar);
		
		// TableModel에 열 제목 추가 : Sun, Mon, ...
		for(int i = 0; i < Days.length; i++)
			mtblCalendar.addColumn(Days[i]);
		
		// 행 높이 60픽셀로 설정
		tblCalendar.setRowHeight(60);
		// 행 개수 6, 열 개수 7로 설정
		mtblCalendar.setRowCount(6);
		mtblCalendar.setColumnCount(7);
		

		// 이전, 다음 버튼 액션리스너 추가
		btnPrev.addActionListener(new Prev_Action());
		btnNext.addActionListener(new Next_Action());

	
		refreshCalendar(currentMonth, currentYear);
		pnlCalendar.add(pnlTop, BorderLayout.NORTH);
		pnlCalendar.add(stblCalendar, BorderLayout.CENTER);
		tblCalendar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					Point p = e.getPoint();
					int row = tblCalendar.rowAtPoint(p);
					int col = tblCalendar.columnAtPoint(p);
					Object value = tblCalendar.getValueAt(row, col);
					System.out.println("DoubleClicked : "+row+","+col);
					if(value != null) {
						String str = JOptionPane.showInputDialog("Enter new value");
						if(str != null) {
							mtblCalendar.setValueAt(str, row, col);
						}
					}
				}
			}
		});
		
		thisContainer.add(pnlTop, BorderLayout.NORTH);
		thisContainer.add(stblCalendar, BorderLayout.CENTER);
		setVisible(true);
	}
	
	class Prev_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentMonth == 0) {
				currentYear--;
				currentMonth = 11;
			}
			else currentMonth -= 1;
			refreshCalendar(currentMonth, currentYear);
		}
	}
	class Next_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentMonth == 11) {
				currentYear++;
				currentMonth = 0;
			}
			else currentMonth += 1;
			refreshCalendar(currentMonth, currentYear);
		}
	}
	public static void main(String[] args) {
		new MainFrame();
	}
	
	public static void refreshCalendar(int month, int year) {
		lblMonth.setText(Months[month]);
		lblYear.setText(Integer.toString(year));
		
		for(int i = 0; i < 6; i++)
			for(int j = 0; j < 7; j++)
				mtblCalendar.setValueAt(null, i, j);
		
		int nod, som;
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); // 출력하려는 달의 최대 일 수 저장
		som = cal.get(GregorianCalendar.DAY_OF_WEEK); // 출력하려는 달의 첫 번째 요일 저장
		
		for(int i = 1; i <= nod; i++) {
			int row = (i+som-2)/7;
			int column = (i+som-2)%7;
			mtblCalendar.setValueAt(i, row, column);
		}
		
	}
}