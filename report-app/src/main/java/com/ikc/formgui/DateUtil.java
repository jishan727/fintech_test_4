package com.ikc.formgui;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;

public final class DateUtil {
    private DateUtil() {}

    public static DatePickerSettings buildDateSettings() {
        DatePickerSettings settings = new DatePickerSettings(Locale.ENGLISH);
        settings.setAllowKeyboardEditing(false);
        settings.setWeekNumbersDisplayed(true, true);
        settings.setFormatForDatesCommonEra("dd/MM/yyyy");
        settings.setFirstDayOfWeek(DayOfWeek.MONDAY);

        settings.setHighlightPolicy(new WeekendHighlightPolicy());
        return settings;
    }

    private static class WeekendHighlightPolicy implements DateHighlightPolicy {
        @Override
        public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
            DayOfWeek dow = date.getDayOfWeek();
            if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                return new HighlightInformation(new Color(245, 245, 245), null, "Weekend");
            }
            return null;
        }
    }
}

