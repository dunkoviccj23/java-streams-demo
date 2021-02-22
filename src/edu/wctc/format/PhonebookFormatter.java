package edu.wctc.format;

public class PhonebookFormatter implements NameFormatter {

    // formats like "READ, Stacy E."
    @Override
    public String formatName(String first, String middle, String last) {
        return String.format("%s, %s %c.",
                last.toUpperCase(),
                first,
                middle.charAt(0));
    }
}
