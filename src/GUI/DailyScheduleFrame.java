package GUI;

import Manager.*;
import javax.swing.*;
import java.awt.*;

public class DailyScheduleFrame extends JFrame {
	private JTextArea nameText;
	private JTextArea contentText;
	private JComboBox<String> startTime;
	private JComboBox<String> endTime;
	private JButton cancelButton;
	private JButton confirmButton;
	private JButton deleteButton;
	public DailyScheduleFrame(DailySchedule schedule, DailyTableManager daily) { // 수정 또는 삭제 프레임
		setTitle(schedule.date.toString());
		setSize(300, 270);
		setLayout(new FlowLayout());

		nameText = new JTextArea(2, 20);
		nameText.setText(schedule.name);

		contentText = new JTextArea(4, 20);
		contentText.setText(schedule.content);

		startTime = new JComboBox<>();
		endTime = new JComboBox<>();
		for (int i = 0; i <= 24; i += 2) {
			String time = String.format("%02d:00", i);
			startTime.addItem(time);
			endTime.addItem(time);
		}

		cancelButton = new JButton("취소");
		cancelButton.addActionListener(e -> dispose());

		confirmButton = new JButton("확인");
		confirmButton.addActionListener(e -> {
			schedule.content = contentText.getText();
			schedule.name = nameText.getText();
			schedule.start = startTime.getSelectedIndex();
			schedule.end = endTime.getSelectedIndex();
			daily.updateTable();
			dispose();
		});
		
		deleteButton = new JButton("삭제");
		deleteButton.addActionListener(e -> {
			daily.schedules.remove(schedule);
			daily.updateTable();
			dispose();
		});
		
		
		add(new JLabel("Schedule Name:"));
		add(nameText);
		add(new JLabel("Schedule Content:"));
		add(contentText);
		add(new JLabel("Start Time:"));
		add(startTime);
		add(new JLabel("End Time:"));
		add(endTime);
		add(cancelButton);
		add(confirmButton);
		add(deleteButton);
		setVisible(true);
	}
}
