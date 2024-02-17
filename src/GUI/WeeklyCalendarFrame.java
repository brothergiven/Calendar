package GUI;

import javax.swing.*;
import javax.swing.table.*;

import Manager.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.util.*;

import java.time.*;

public class WeeklyCalendarFrame extends JFrame {
    private WeeklyCalendarTableManager manager;
    private JLabel yearAndMonthLabel;

    public WeeklyCalendarFrame() {
        manager = new WeeklyCalendarTableManager();
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("이전 주");
        JButton nextButton = new JButton("다음 주");

        prevButton.addActionListener(e -> {
            manager.refreshTable(false);
            updateYearAndMonthLabel();
            updateTimeLabels();
        });

        nextButton.addActionListener(e -> {
            manager.refreshTable(true);
            updateYearAndMonthLabel();
            updateTimeLabels();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        yearAndMonthLabel = new JLabel();
        updateYearAndMonthLabel();
        buttonPanel.add(yearAndMonthLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(manager.calendarTable), BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private void updateYearAndMonthLabel() {
        String yearText = manager.getYearText();
        String monthText = String.format("%02d", manager.currentMonth + 1);
        yearAndMonthLabel.setText(yearText + "년 " + monthText + "월");
    }


    private void updateTimeLabels() {
        for (int i = 0; i < manager.countRow; i++) {
            manager.tm.setValueAt(String.format("%02d:00 - %02d:00", i * 2, i * 2 + 2), i, 0);
        }
    }
}