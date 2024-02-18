package Manager;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.Component;
import java.time.*;

import java.io.*;


public class WeeklyTableManager extends TableManager {
	public final int countRow = 13;
	public final int countCol = 8;

	public WeeklyTableManager() {
		tm.addColumn("Times");
		for (int i = 0; i < Days.length; i++)
			tm.addColumn(Days[i]);

		tm.setRowCount(countRow);
		tm.setColumnCount(countCol);
		refreshTable();

		for (int i = 1; i < countRow; i++)
			table.setRowHeight(i, 60);

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (row == 0)
					return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
				else
					return super.getTableCellRendererComponent(table,
							String.format("%02d:00 - %02d:00", (row - 1) * 2, (row - 1) * 2 + 2), isSelected, hasFocus,
							row, column);
			}
		});
	}

	@Override
	public void initTable() {
		for (int i = 0; i < countRow; i++)
			for (int j = 0; j < countCol; j++)
				tm.setValueAt(null, i, j);
		
		LocalDate startOfWeek = showing
				.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));

		for (int i = 0; i < 7; i++) {
			LocalDate day = startOfWeek.plusDays(i);
			tm.setValueAt(day.getDayOfMonth(), 0, i + 1);
		}
	}

	
	
	@Override
	public void writeTask(String task, LocalDate date) {
		try {
			File dest = new File(destDirectory + "/" + date.toString());
			FileReader fr;
			fr = new FileReader(dest);
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void readTask() {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalDate getDate(int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRowCol(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshTable(boolean next) {
		if(next) {
			showing = showing.plusWeeks(1);
		} else {
			showing = showing.minusWeeks(1);
		}
		initTable();
		readTask();		
	}

	@Override
	public void refreshTable() {
		initTable();
		readTask();
	}

}
