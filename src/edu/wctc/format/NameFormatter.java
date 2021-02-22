package edu.wctc.format;

@FunctionalInterface
public interface NameFormatter {
    String formatName(String first, String middle, String last);
}
