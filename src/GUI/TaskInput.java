package GUI;

import Manager.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TaskInput extends JFrame {
	// Text 입력 받는 창
	Container c;
	TextArea ta;
	JButton confirm, cancel;
	CalendarTableManager ctm;
	JTable tblCalendar;
	String task;
	int row, col;

	public TaskInput(CalendarTableManager ctm, JTable tblCalendar, int row, int col) throws IOException {
		this.ctm = ctm; // ctm에서 task 가져옴
		this.row = row;
		this.col = col;
		this.tblCalendar = tblCalendar;
		setTitle(ctm.getDate(row, col));
		setSize(500, 300);
		
		ta = new TextArea(ctm.getTask(row, col));
		c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(ta);
		
		confirm = new JButton("확인");
		cancel = new JButton("취소");
		
		c.add(confirm);
		confirm.addActionListener(new ConFirm_btn());
		c.add(cancel);
		cancel.addActionListener(new Cancel_btn());
		
		setVisible(true);
	}

	class ConFirm_btn implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			task = ta.getText();
			ctm.writeTask(task, row, col);
			tblCalendar.setValueAt(task, row, col);
			dispose();
		}
	}

	class Cancel_btn implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}


}
