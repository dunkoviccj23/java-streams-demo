package edu.wctc;

import edu.wctc.format.InitialFormatter;
import edu.wctc.format.NameFormatter;
import edu.wctc.format.PhonebookFormatter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.DoubleStream;

public class FunctionalDemo {
    private final List<Country> countryList = new ArrayList<>();
    private final String[] stringArray = {"Milwaukee", "Dane", "Waukesha", "Brown", "Racine"};
    private final List<Double> doubleList = new ArrayList<>();

    public FunctionalDemo(List<Country> countryList) {
        this.countryList.addAll(countryList);

        // Loading a list up with doubles for local FM radio stations
        DoubleStream.of(95.7, 102.1, 89.7, 88.9, 100.7, 99.1, 94.5).forEach(doubleList::add);

        sorting();

        functionalInterface();

        objectWithMaxMin();
    }

    public void functionalInterface() {
        String first = "Stacy", middle = "Elizabeth", last = "Read";

        // old school, create classes implementing the interface
        NameFormatter initialFormat = new InitialFormatter();
        System.out.println(initialFormat.formatName(first, middle, last));

        NameFormatter phonebookFormat = new PhonebookFormatter();
        System.out.println(phonebookFormat.formatName(first, middle, last));

        // New way, create a lambda function (an instance of a functional interface)
        NameFormatter initialLambda =
                (f, m, l) -> String.format("%c%c%c", f.charAt(0), m.charAt(0), l.charAt(0));
        System.out.println(initialLambda.formatName(first, middle, last));

        // BinaryOperator has one method named 'apply' that takes two operands
        // and returns a result of the same data type
        BinaryOperator<String> celebCoupleName = (celeb1, celeb2) -> {
            String firstHalf = celeb1.substring(0, celeb1.length() / 2);
            String secondHalf = celeb2.substring(celeb2.length() / 2);
            return firstHalf + secondHalf;
        };

        System.out.println(celebCoupleName.apply("Brad", "Angelina"));
        System.out.println(celebCoupleName.apply("Kim", "Kanye"));
        System.out.println(celebCoupleName.apply("Harry", "Meghan"));
    }

    private void objectWithMaxMin() {
        // find country with max population
        Country hasMaxPopulation =
                Collections.max(countryList, Comparator.comparingInt(Country::getPopulation));
        System.out.println("Max population: " + hasMaxPopulation);

        // find country with min area, breaking ties by dictionary order of country name
        Country hasMinArea =
                Collections.min(countryList,
                        Comparator.comparingInt(Country::getArea)
                                .thenComparing(Country::getName));
        System.out.println("Min area: " + hasMinArea);

        // find country with longest name
        Country longestName =
                Collections.max(countryList,
                        Comparator.comparingInt(country -> country.getName().length()));
        System.out.println("Longest name: " + longestName);
    }

    public void sorting() {
        // old school, must create a whole class and object
        Comparator<Country> popComparator = new CountryPopulationComparator();

        // Sort a collection. Will actually sort the underlying list.
        countryList.sort(popComparator);

        writeFile(countryList, "populationSort.txt");

        // Use an anonymous inner class to sort by area
        countryList.sort(new Comparator<Country>() {
            @Override
            public int compare(Country c1, Country c2) {
                return Integer.compare(c1.getArea(), c2.getArea());
            }
        });

        writeFile(countryList, "areaSort.txt");

        // Use a lambda to sort by name because Comparator is a @FunctionalInterface
        countryList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

        writeFile(countryList, "nameSort.txt");

        // Use a method reference to Country's getCapital method to sort by capital
        countryList.sort(Comparator.comparing(Country::getCapital));

        writeFile(countryList, "capitalSort.txt");

        // Sort first by continent, then by population (low to high)
        countryList.sort(Comparator.comparing(Country::getContinent)
                .thenComparing(Country::getPopulation));

        writeFile(countryList, "continentPopulationSort.txt");

        // Sort in reverse dictionary order (natural order reversed)
        Arrays.sort(stringArray, Comparator.reverseOrder());
        System.out.println(String.join(", ", stringArray));

        // Sort by string length (longest to shortest)
        Arrays.sort(stringArray, Comparator.comparingInt(String::length).reversed());
        System.out.println(String.join(", ", stringArray));
    }

    private void writeFile(List<?> list, String fileName) {
        try (PrintWriter pw = new PrintWriter(fileName)) {
            // for each item in the list, pass it to the print writer's println method
            list.forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    private class CountryPopulationComparator implements Comparator<Country> {

        @Override
        public int compare(Country c1, Country c2) {
            return Integer.compare(c1.getPopulation(), c2.getPopulation());
        }
    }
}
