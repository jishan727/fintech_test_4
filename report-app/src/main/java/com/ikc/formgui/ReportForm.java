package com.ikc.formgui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class ReportForm extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color HEADER_FOOTER_COLOR = new Color(230, 235, 240);
    private static final Color BLUE_BUTTON_COLOR = new Color(23, 115, 209);
    private static final Color CONFIG_BUTTON_COLOR = new Color(225, 225, 225);

    private static final Dimension BUTTON_SIZE = new Dimension(120, 35);
    private static final Dimension DATE_PICKER_SIZE = new Dimension(150, 35);
    private static final Dimension TIME_PICKER_SIZE = new Dimension(90, 35);
    private static final Dimension DROPDOWN_SIZE = new Dimension(100, 35);
    private static final Font STANDARD_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    private DatePicker fromDatePicker;
    private TimePicker fromTimePicker;
    private DatePicker toDatePicker;
    private TimePicker toTimePicker;

    public ReportForm() {
        setLayout(new BorderLayout(0, 5));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new MigLayout(
                "insets 10",
                "[][150!][90!][10!][][150!][90!][10!][][100!][20!][120!][120!]",
                "[35!]"));
        headerPanel.setBackground(HEADER_FOOTER_COLOR);

        DatePickerSettings fromSettings = DateUtil.buildDateSettings();
        DatePickerSettings toSettings = DateUtil.buildDateSettings();

        DateHighlightPolicy rangePolicy = date -> {
            if (fromDatePicker == null || toDatePicker == null) return null;
            LocalDate f = fromDatePicker.getDate();
            LocalDate t = toDatePicker.getDate();
            if (f != null && t != null && !date.isBefore(f) && !date.isAfter(t)) {
                return new HighlightInformation(new Color(220, 232, 245), null, "In range");
            }
            return null;
        };
        fromSettings.setHighlightPolicy(rangePolicy);
        toSettings.setHighlightPolicy(rangePolicy);

        fromDatePicker = new DatePicker(fromSettings);
        toDatePicker = new DatePicker(toSettings);
        fromDatePicker.setDate(LocalDate.now());
        toDatePicker.setDate(LocalDate.now());

        TimePickerSettings timeSettings = buildTimeSettings();
        fromTimePicker = new TimePicker(timeSettings);
        toTimePicker = new TimePicker(timeSettings);
        fromTimePicker.setTime(LocalTime.now().withSecond(0).withNano(0));
        toTimePicker.setTime(LocalTime.now().withSecond(0).withNano(0));

        fromDatePicker.setPreferredSize(DATE_PICKER_SIZE);
        toDatePicker.setPreferredSize(DATE_PICKER_SIZE);
        fromTimePicker.setPreferredSize(TIME_PICKER_SIZE);
        toTimePicker.setPreferredSize(TIME_PICKER_SIZE);

        JLabel fromLabel = createStyledLabel("From:");
        headerPanel.add(fromLabel);
        headerPanel.add(fromDatePicker);
        headerPanel.add(fromTimePicker);
        headerPanel.add(new JLabel(" "));

        JLabel toLabel = createStyledLabel("To:");
        headerPanel.add(toLabel);
        headerPanel.add(toDatePicker);
        headerPanel.add(toTimePicker);
        headerPanel.add(new JLabel(" "));

        String[] options = {"All", "Option1", "Option2"};
        JComboBox<String> dropdown = createStyledDropdown(options);
        headerPanel.add(dropdown);
        headerPanel.add(new JLabel(" "));

        JButton generatePdfButton = createStyledButton("Generate PDF", BLUE_BUTTON_COLOR, Color.WHITE);
        generatePdfButton.addActionListener(e -> searchBatchNames());
        JButton configButton = createStyledButton("Config", CONFIG_BUTTON_COLOR, Color.BLACK);
        headerPanel.add(generatePdfButton);
        headerPanel.add(configButton);

        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel contentLabel = new JLabel("Report Content Area", SwingConstants.CENTER);
        contentLabel.setFont(STANDARD_FONT);
        contentLabel.setOpaque(true);
        contentLabel.setBackground(Color.WHITE);
        contentLabel.setBorder(BorderFactory.createEtchedBorder());
        contentPanel.add(contentLabel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new MigLayout("insets 10", "push[120!][10!][120!][10!][120!]", "[35!]"));
        footerPanel.setBackground(HEADER_FOOTER_COLOR);

        JButton onlyDataButton = createStyledButton("Only Data", BLUE_BUTTON_COLOR, Color.WHITE);
        JButton copyGraphButton = createStyledButton("Copy Graph", BLUE_BUTTON_COLOR, Color.WHITE);
        JButton dataAddGraphButton = createStyledButton("Data Add Graph", BLUE_BUTTON_COLOR, Color.WHITE);

        footerPanel.add(onlyDataButton);
        footerPanel.add(new JLabel(" "));
        footerPanel.add(copyGraphButton);
        footerPanel.add(new JLabel(" "));
        footerPanel.add(dataAddGraphButton);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private TimePickerSettings buildTimeSettings() {
        TimePickerSettings tps = new TimePickerSettings(Locale.ENGLISH);
        tps.use24HourClockFormat();
        tps.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.FifteenMinutes, null, null);
        tps.setInitialTimeToNow();
        return tps;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setFont(STANDARD_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private JComboBox<String> createStyledDropdown(String[] options) {
        JComboBox<String> dropdown = new JComboBox<>(options);
        dropdown.setFont(STANDARD_FONT);
        dropdown.setBackground(Color.WHITE);
        dropdown.setPreferredSize(DROPDOWN_SIZE);
        dropdown.setMinimumSize(DROPDOWN_SIZE);
        dropdown.setMaximumSize(DROPDOWN_SIZE);
        dropdown.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        return dropdown;
    }

    private void searchBatchNames() {
        LocalDate fromDate = fromDatePicker.getDate();
        LocalDate toDate = toDatePicker.getDate();
        LocalTime fromTime = fromTimePicker.getTime();
        LocalTime toTime = toTimePicker.getTime();

        System.out.println("From: " + fromDate + " " + fromTime);
        System.out.println("To: " + toDate + " " + toTime);
    }
}

