package com.github.coreycaplan3.thebuzz.utilities.verification;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;

import com.github.coreycaplan3.thebuzz.application.BuzzApplication;
import com.github.coreycaplan3.thebuzz.R;

import java.util.Calendar;

/**
 * Created by Corey on 1/21/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: A utility class that contains methods for ensuring a form was entered properly
 * by the user.
 */
public final class FormValidation {

    private FormValidation() {
    }

    /**
     * @param textInputLayout   The {@link TextInputLayout} whose text will be validated.
     * @param text              The text to be validated.
     * @param minimumTextLength The minimum length of the field.
     * @param maximumTextLength The maximum length of the field.
     * @return True if the text is valid or false if it's invalid.
     */
    public static boolean validateText(TextInputLayout textInputLayout, String text,
                                       int minimumTextLength, int maximumTextLength) {
        if (FormValidation.isEmpty(text)) {
            textInputLayout.setError(BuzzApplication.context().getString(R.string.error_required));
            return false;
        } else if (FormValidation.isTooShort(minimumTextLength, text)) {
            textInputLayout.setError(BuzzApplication.context().getString(R.string.error_too_short));
            return false;
        } else if (FormValidation.isTooLong(maximumTextLength, text)) {
            textInputLayout.setError(BuzzApplication.context().getString(R.string.error_too_long));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    /**
     * Checks if a field is empty (after trimming it).
     *
     * @param field The field that should be checked
     * @return True if the field is empty or false if it is not.
     */
    public static boolean isEmpty(@Nullable String field) {
        return field == null || field.trim().length() == 0;
    }

    /**
     * Checks if a text field is too short (after trimming it).
     *
     * @param minimumLength The minimum length of the field.
     * @param field         The field that should be checked.
     * @return True if the field is too short or false if its length is okay.
     */
    public static boolean isTooShort(int minimumLength, @Nullable String field) {
        return field == null || field.trim().length() < minimumLength;
    }

    /**
     * Checks if a text field is too long (after trimming it).
     *
     * @param maximumLength The minimum length of the field.
     * @param field         The field that should be checked.
     * @return True if the field is too long or false if its length is okay.
     */
    public static boolean isTooLong(int maximumLength, @Nullable String field) {
        return field == null || field.trim().length() > maximumLength;
    }

    /**
     * Checks if a password is invalid.
     *
     * @param password The password to be checked.
     * @return True if the password is invalid, or false if it is valid.
     */
    public static boolean isPasswordInvalid(@Nullable String password) {
        if (password == null) {
            return true;
        }
        int letterCount = 0;
        int digitCount = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                letterCount++;

            } else if (Character.isDigit(password.charAt(i))) {
                digitCount++;
            }
        }
        return letterCount == 0 || digitCount == 0;
    }

    /**
     * Checks if a start date is before the end date.
     *
     * @param startDateMillis The start date that should be checked.
     * @param endDateMillis   The end date that should be checked.
     * @return True if the start date is on the same day or earlier than the end date.
     */
    public static boolean isStartDateBeforeEndDate(long startDateMillis, long endDateMillis) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(startDateMillis);
        endDate.setTimeInMillis(endDateMillis);
        int startYear = startDate.get(Calendar.YEAR);
        int startDay = startDate.get(Calendar.DAY_OF_YEAR);
        int endYear = endDate.get(Calendar.YEAR);
        int endDay = endDate.get(Calendar.DAY_OF_YEAR);
        return startYear < endYear || (startYear == endYear && startDay <= endDay);
    }

    /**
     * Checks if a start date is the same as the end date.
     *
     * @param startDateMillis The start date that should be checked.
     * @param endDateMillis   The end date that should be checked.
     * @return True if the start date is on the same day or earlier than the end date.
     */
    public static boolean isStartDateSameAsEndDate(long startDateMillis, long endDateMillis) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(startDateMillis);
        endDate.setTimeInMillis(endDateMillis);
        int startYear = startDate.get(Calendar.YEAR);
        int startDay = startDate.get(Calendar.DAY_OF_YEAR);
        int endYear = endDate.get(Calendar.YEAR);
        int endDay = endDate.get(Calendar.DAY_OF_YEAR);
        return startYear == endYear && startDay == endDay;
    }

    /**
     * Checks if an event's start date is on the same day as the end date.
     *
     * @param startDate The event's start date.
     * @param endDate   The event's end date.
     * @return True if they're on the same day or false if they are not.
     */
    public static boolean isTimeOneDayLong(long startDate, long endDate) {
        Calendar startDateCalendar = Calendar.getInstance();
        Calendar endDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        endDateCalendar.setTimeInMillis(endDate);
        int startYear = startDateCalendar.get(Calendar.YEAR);
        int startDay = startDateCalendar.get(Calendar.DAY_OF_YEAR);
        int endYear = endDateCalendar.get(Calendar.YEAR);
        int endDay = endDateCalendar.get(Calendar.DAY_OF_YEAR);

        return (startYear == endYear) && (startDay == endDay);
    }

    /**
     * Checks if the given event's start time is within 15 minutes or less of the current time.
     *
     * @param startDate The start date of the event.
     * @param startTime The start time of the event.
     * @return True if the start time is too close to the present time (within 30 minutes) or is
     * before it. Returns false if the start time is after the current time (it's valid).
     */
    public static boolean isEventStartTimeBeforeCurrentTime(Calendar startDate,
                                                            Calendar startTime) {
        Calendar currentTime = Calendar.getInstance();
        Calendar fullStartTime = Calendar.getInstance();
        fullStartTime.setTimeInMillis(startDate.getTimeInMillis());
        fullStartTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
        fullStartTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
        long millisecondsInMinute = 60 * 1000;
        long thirtyMinutesInMillis = millisecondsInMinute * 30;

        return fullStartTime.getTimeInMillis() -
                (thirtyMinutesInMillis + currentTime.getTimeInMillis()) < 0;
    }

    /**
     * Checks if the start day is after the end day.
     *
     * @param startDate The event's start date.
     * @param endDate   The event's end date.
     * @return True if the event's start time is after the end time, or false if it is equal to
     * or before it.
     */
    public static boolean isStartDateAfterEndDate(Calendar startDate, Calendar endDate) {
        if (startDate.get(Calendar.DAY_OF_YEAR) > endDate.get(Calendar.DAY_OF_YEAR) &&
                startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) {
            return true;
        } else if (startDate.get(Calendar.YEAR) > endDate.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the start time is after the end time. Note, this method assumes the two times are
     * on the same day.
     *
     * @param startTime The event's start time
     * @param endTime   The event's end time
     * @return True if the event's start time is after the end time, or false if it is equal to
     * or before it.
     */
    public static boolean isStartTimeAfterEndTime(Calendar startTime, Calendar endTime) {
        if (startTime.get(Calendar.HOUR_OF_DAY) > endTime.get(Calendar.HOUR_OF_DAY)) {
            return true;
        } else if (startTime.get(Calendar.HOUR_OF_DAY) == endTime.get(Calendar.HOUR_OF_DAY)) {
            return startTime.get(Calendar.MINUTE) >= endTime.get(Calendar.MINUTE);
        }
        return false;
    }

    /**
     * Checks if the start time is equal to the end time.
     *
     * @param startTime The event's start time
     * @param endTime   The event's end time
     * @return True if the event's start time is equal to the end time.
     */
    public static boolean isStartTimeEqualToEndTime(Calendar startTime, Calendar endTime) {
        return startTime.get(Calendar.HOUR_OF_DAY) == endTime.get(Calendar.HOUR_OF_DAY) &&
                startTime.get(Calendar.MINUTE) == endTime.get(Calendar.MINUTE);
    }

    /**
     * Checks if a given tag contains any spaces between words/letters.
     *
     * @param tag The tag that should be validated.
     * @return True if the tag is valid or false if it is not.
     */
    public static boolean isTagValid(@Nullable String tag) {
        if (tag == null) {
            return false;
        }
        tag = tag.trim();
        for (int i = 0; i < tag.length(); i++) {
            if (tag.charAt(i) == ' ') {
                return false;
            }
        }
        return true;
    }

}
