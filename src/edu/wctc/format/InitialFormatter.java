package edu.wctc.format;

public class InitialFormatter implements NameFormatter {
    // Formats like "SER"
    @Override
    public String formatName(String first, String middle, String last) {
        return String.format("%c%c%c", first.charAt(0), middle.charAt(0), last.charAt(0));
    }
}
