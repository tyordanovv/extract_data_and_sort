package utils;

import entities.Data;
import entities.Ratings;
import entities.WeekColumns;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class IOHandler {
    private final static String logOutputFilePath = "src/main/resources/output/logFile.txt";
    private final static String sortedListOutputFilePath = "src/main/resources/output/musicSorted2022.csv";

    private final Data outputData = new Data();
    private HashMap<String, Ratings> ratingMap = new HashMap<>();
    private Double rating;


    public void fetchData(String inputFilePath) {
        try {
            FileReader fr = new FileReader(inputFilePath);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(fr);
            for (CSVRecord record : records) {
                if (isAppropriate(record)){
                    addInterpretToRatingMap(record);
                }
            }
            writeData(logOutputFilePath);
            createOutputFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOutputFile() {
        List<Map.Entry<String, Ratings>> sortedList = createSortedListOfMap();
        createOutputData(sortedList);
        writeData(sortedListOutputFilePath);
    }


    private void writeData(String filepath) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));

            if (filepath.endsWith(".txt")){
                bufferedWriter.write(outputData.getLogFileData());
                bufferedWriter.close();
            }
            else if (filepath.endsWith(".csv")){
                bufferedWriter.write(outputData.getSortedOutputData());
                bufferedWriter.close();
            }
        }catch (IOException e){
            System.out.println(e);;
        }
    }

    private void createOutputData(List<Map.Entry<String, Ratings>> list) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (Map.Entry<String, Ratings> map : list) {
            outputData
                    .setSortedOutputData(
                            map.getKey() + "; " +
                    map.getValue().getNumOfRatings() + "; " +
                    decimalFormat.format(map.getValue().getAverageRating()) + "; " +
                    map.getValue().getRatings() + "\n");
        }
    }

    private List<Map.Entry<String, Ratings>> createSortedListOfMap() {
        List<Map.Entry<String, Ratings>> list = new ArrayList<>(ratingMap.entrySet());

        list.sort(Map.Entry.comparingByValue(
                Comparator
                        .comparingInt(Ratings::getNumOfRatings)
                        .thenComparingDouble(Ratings::getAverageRating).reversed()));
        return list;
    }

    private void addInterpretToRatingMap(CSVRecord record) {
        String[] interprets = record.get("Interpret").split("\\s*,\\s*");

        for (String interpret : interprets){
            if (!ratingMap.containsKey(interpret)){
                Ratings newRating = new Ratings();
                newRating.addRating(rating);
                ratingMap.put(interpret, newRating);
            } else {
                Ratings existingRating = ratingMap.get(interpret);
                existingRating.addRating(rating);
            }
        }
    }

    private boolean isAppropriate(CSVRecord record) {
        return (hasCorrectWeekInput(record) && hasCorrectRatingInput(record) && hasCorrectSize(record));
    }

    private boolean hasCorrectSize(CSVRecord record) {
        if (record.size() != 6){
            outputData.setLogFileData(
                    "Malformed input @ line: " + record.getRecordNumber() + " -- ignoring line\n"
            );
            return false;
        }else return true;
    }

    private boolean hasCorrectRatingInput(CSVRecord record) {

        try {
            NumberFormat nf_in = NumberFormat.getNumberInstance(Locale.GERMANY);
            rating = nf_in.parse(record.get("Bewertung")).doubleValue();
        }catch (Exception e) {
            outputData.setLogFileData(
                    "Parse exception for Bewertung @ line: " +
                    record.getRecordNumber() + " -- ignoring\n" +
                    e.getClass().getSimpleName() + "\n"
            );
            return false;
        }
        return true;
    }

    private boolean hasCorrectWeekInput(CSVRecord record) {
        int column = 0;
        try {
            for (int i = 0; i < 3; i++){
                column = i;
                if ((record.get(i).isEmpty()) && (i == 1)) continue;
                Integer integer = Integer.parseInt(record.get(i));
            }
        }catch (Exception e) {
            outputData.setLogFileData(
                    "Number parsing error for " + WeekColumns.values()[column] + "@ line: " +
                    record.getRecordNumber() + " -- ignoring\n" +
                    e.getClass().getSimpleName() + "\n"
            );
            return false;
        }
        return true;
    }
}
