package Manager;

import java.awt.*;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class WeeklyCellRenderer extends JTextArea implements TableCellRenderer {
	public WeeklyCellRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			if (row == 0) {
				if (column == 1) { // 일요일 빨간색 글자
					setForeground(Color.red);
				} else if (column == 7) { // 토요일 파란색 글자
					setForeground(Color.blue);
				} else { // 평일 검정색 글자
					setForeground(Color.black);
				}
			} else {
				setForeground(Color.black);
			}
			setBackground(table.getBackground());
		}
		if (value != null)
			setText(value.toString());
		else
			setText("");
		return this;
	}
}