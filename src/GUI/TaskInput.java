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
	TableManager manager;
	JTable tblCalendar;
	String task;
	int row, col;

	public TaskInput(TableManager manager, JTable tblCalendar, int row, int col) {
		this.manager = manager; // ctm에서 task 가져옴
		this.row = row;
		this.col = col;
		this.tblCalendar = tblCalendar;
		setTitle(manager.getDate(row, col).toString());
		setSize(500, 300);
		
		ta = new TextArea(manager.getTask(row, col));
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
			manager.writeTask(task,manager.getDate(row, col));
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
