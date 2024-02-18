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
    private WeeklyTableManager weekly;

    public WeeklyCalendarFrame() {
    	weekly = new WeeklyTableManager();
    	setTitle("Weekly Calendar");
        setSize(1000, 700);		
        setLocation(460, 190);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
        
        JButton prevButton = new JButton("<<");
        JButton nextButton = new JButton(">>");
        JLabel monthLabel = new JLabel(weekly.getMonthText());
        JLabel yearLabel = new JLabel(weekly.getYearText());
        
        prevButton.addActionListener(e -> {
        	weekly.refreshTable(false);
            monthLabel.setText(weekly.getMonthText());
        	yearLabel.setText(weekly.getYearText());
        });

        nextButton.addActionListener(e -> {
        	weekly.refreshTable(true);
            monthLabel.setText(weekly.getMonthText());
        	yearLabel.setText(weekly.getYearText());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        buttonPanel.add(prevButton);
        buttonPanel.add(monthLabel);
        buttonPanel.add(yearLabel);
        buttonPanel.add(nextButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(weekly.table), BorderLayout.CENTER);

        setVisible(true);
    }
}