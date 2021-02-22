package edu.wctc;

import java.nio.CharBuffer;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class StreamsDemo {
    private final List<Country> countryList = new ArrayList<>();

    // highest population counties in Wisconsin
    private final String[] stringArray = {"Milwaukee", "Dane", "Waukesha", "Brown", "Racine"};

    // local FM radio stations
    private final double[] doubleArray = {95.7, 102.1, 89.7, 88.9, 100.7, 99.1, 94.5};

    // high temperatures this week
    private final int[] intArray = {35, 39, 43, 40, 36, 40, 42, 39};

    // vowels
    private final char[] charArray = {'a', 'e', 'i', 'o', 'u'};

    public StreamsDemo(List<Country> countryList) {
        this.countryList.addAll(countryList);

        arraysToLists();

        mapDemo();

        mathTerminals();

        findingMatchingObjects();

        reportByContinent();

        forEachDemo();
    }

    public void arraysToLists() {
        /* ARRAY OF STRINGS */

        List<String> stringList = Arrays.stream(stringArray)
                .collect(Collectors.toList()); // collect the stream into a list

        printList(stringList);

        /* ARRAY OF CHARS */

        List<Character> charList = CharBuffer.wrap(charArray).chars() // get IntStream
                .mapToObj(i -> (char) i) // cast each int to Character
                .collect(Collectors.toList()); // or use Google's Guava library...

        printList(charList);

        /* ARRAY OF INTS */

        List<Integer> intList = Arrays.stream(intArray) // could also use IntStream.of
                .boxed() // box primitive ints into wrapper class Integers
                .collect(Collectors.toList()); // collect into a list

        printList(intList);

        /* ARRAY OF DOUBLES */

        List<Double> doubleList = DoubleStream.of(doubleArray)
                .boxed() // box primitive doubles into wrapper class Doubles
                .sorted() // sort stream in natural order (low to high)
                .collect(Collectors.toList()); // collect into a a new list

        printList(doubleList);
    }

    public void forEachDemo() {
        // Already got a list with stuff in it?
        List<Double> existingList = new ArrayList<>();
        existingList.add(103.7);
        existingList.add(91.7);

        // Call add method of existingList for each Double in the stream
        DoubleStream.of(95.7, 102.1, 89.7, 88.9, 100.7, 99.1, 94.5).forEach(existingList::add);

        printList(existingList);

        // update Central America countries to be South America instead
        countryList.forEach(country -> {
            if (country.getContinent().equals("Central America"))
                country.setContinent("South America");
        });

        reportByContinent();

    }


    public void mapDemo() {
        List<Integer> timesTwo = IntStream.of(intArray)
                .map(i -> i * 2) // create new stream with int values doubled
                .boxed() // create Integers from ints
                .collect(Collectors.toList()); // add to new list

        printList(timesTwo);

        List<Integer> populations = countryList.stream()
                .map(Country::getPopulation)
                .collect(Collectors.toList());

        printList(populations);

        List<String> uppercaseCapitals = countryList.stream()
                .map(Country::getCapital)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        printList(uppercaseCapitals);
    }

    private void mathTerminals() {
        // Kind of pointless, should just use countryList.size()
        long numCountries = countryList.stream().count();

        // Lambda function for Predicate interface, takes in X and returns
        // true or false for some test of X
        Predicate<Country> startsWithM = (country -> country.getName().startsWith("M"));

        long numCountriesStartWithM = countryList.stream()
                .filter(startsWithM) // apply true/false test to each country
                .count(); // count the ones that were "true"

        System.out.printf("There are %d countries that start with M %n", numCountriesStartWithM);

        long populationOfCountriesThatStartWithM = countryList.stream()
                .filter(startsWithM) // apply true/false test to each country
                .mapToInt(Country::getPopulation) // map each M country to its population (an int)
                .sum(); // sum the stream of ints

        System.out.printf("%d people live in countries that start with M %n",
                populationOfCountriesThatStartWithM);

        // How many countries have population < 10,000?
        long numCountriesSmallPop = countryList.stream()
                .filter(country -> country.getPopulation() < 10000) // a Predicate lambda, but inline
                .count();

        System.out.printf("%d countries have population < 10,000 %n", numCountriesSmallPop);

        // Average population of countries in Europe
        OptionalDouble averageEuropeanPopulation = countryList.stream()
                .filter(country -> country.getContinent().equals("Europe")) // Predicate matching continent of "Europe"
                .mapToDouble(Country::getPopulation) // map population to double for average to include decimals
                .average();

        // If there were no European countries, there would be no average
        // Optionals save us from dealing with nulls when there is no result
        if (averageEuropeanPopulation.isPresent()) {
            System.out.printf("Average European population: %.2f %n", averageEuropeanPopulation.getAsDouble());
        } else {
            System.out.println("Average European population: N/A");
        }

        // Find maximum area of all countries in Asia where the country and its capital start with the same letter
        // If there are no matching countries, there is no max, so this also produces an Optional
        double maxArea = countryList.stream()
                .filter(country -> country.getContinent().equals("Asia"))
                .filter(country -> country.getName().charAt(0) == country.getCapital().charAt(0))
                .mapToDouble(Country::getArea)
                .max()
                .orElse(0.0); // returns a real value if present, or else 0 if no result
        System.out.printf("Largest area in Asia: %.2f %n", maxArea);
    }

    private void findingMatchingObjects() {
        // Find the first Country that starts with "X"
        Optional<Country> startsWithX = countryList.stream()
                .filter(country -> country.getName().startsWith("X"))
                .findFirst();

        if (startsWithX.isEmpty()) {
            System.out.println("No countries start with X!");
        } else {
            System.out.printf("%s starts with X %n", startsWithX.get());
        }

        // Find all countries that start with "A"
        List<Country> startWithA = countryList.stream()
                .filter(country -> country.getName().startsWith("A"))
                .collect(Collectors.toList());

        printList(startWithA);

        // True/false: are there any countries with zero population?
        boolean zeroPopulation = countryList.stream()
                .anyMatch(country -> country.getPopulation() == 0);
        System.out.printf("Any countries have no population? %b %n", zeroPopulation);

        // True/false: do all countries have area > 0?
        boolean zeroArea = countryList.stream()
                .allMatch(country -> country.getArea() > 0);
        System.out.printf("All countries have area > 0? %b %n", zeroArea);

        // Really? Which countries have zero area?!
        List<Country> zeroAreaCountries = countryList.stream()
                .filter(country -> country.getArea() <= 0)
                .collect(Collectors.toList());
        printList(zeroAreaCountries);
    }

    private void reportByContinent() {
        // Find all unique continents (no duplicates)
        List<String> continents = countryList.stream()
                .map(Country::getContinent)
                .distinct() // collapse duplicates
                .collect(Collectors.toList());

        for (String continent : continents) {
            // Get sum of population for each continent
            long totalPop = countryList.stream()
                    .filter(country -> country.getContinent().equals(continent)) // outside variables used in lambdas are effectively final
                    .mapToInt(Country::getPopulation)
                    .sum();
            System.out.printf("%-20s%,20d %n", continent, totalPop);
        }
    }

    private void printList(List<?> list) {
        String concatString =
                list.stream()
                        .map(String::valueOf) // change whatever's in list to a string
                        .collect(Collectors.joining(", ")); // join with comma delimiter
        System.out.println(concatString);
    }
}
