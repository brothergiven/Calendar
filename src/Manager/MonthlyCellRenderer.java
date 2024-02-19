package Manager;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MonthlyCellRenderer extends JTextArea implements TableCellRenderer {
	public MonthlyCellRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            if (row % 2 == 0) { 
                if (column == 0) { // 일요일 빨간색 글자
                    setForeground(Color.red);
                } else if (column == 6) { // 토요일 파란색 글자
                    setForeground(Color.blue);
                } else { // 평일 검정색 글자
                    setForeground(Color.black);
                }
            } else { 
                setForeground(Color.black);
            }
            setBackground(table.getBackground());
        }
        if(value != null) setText(value.toString());
        else setText("");
        return this;
    }
}