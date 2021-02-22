package edu.wctc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Country> readCsvIntoList() {
        List<Country> countryList = new ArrayList<>();

        // Read CSV into object list

        try (Scanner fileInput = new Scanner(new File("countries.txt"))) {
            while (fileInput.hasNext()) {
                String line = fileInput.nextLine();
                String[] columns = line.split(";");

                Country country = new Country();
                countryList.add(country);

                // name, continent, capital, population, area
                country.setName(columns[0]);
                country.setContinent(columns[1]);
                country.setCapital(columns[2]);
                country.setPopulation(Integer.parseInt(columns[3]));
                country.setArea(Integer.parseInt(columns[4]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return countryList;
    }


    public static void main(String[] args) {
        System.out.println("PROGRAM START");

        List<Country> countryList = readCsvIntoList();

        new FunctionalDemo(countryList);

        new StreamsDemo(countryList);

        System.out.println("PROGRAM END");
    }

}
