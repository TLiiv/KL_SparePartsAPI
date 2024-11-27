package tliiv.dev.SpareParts;

import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//apache commons csv
//https://www.baeldung.com/jackson  parseimiseks  jsonisse

@Service
public class SparePartsService {
    private final List<SpareParts> sparePartsList = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("/csv/LE.txt"), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withDelimiter('\t')
                     .withIgnoreSurroundingSpaces()
                     .withIgnoreEmptyLines()
                     .withTrim())) {

            for (CSVRecord record : csvParser) {
                // Safe parsing for numeric fields (check if empty and use default value)
                String seriesNumber = record.get(0);
                int stock1 = parseIntOrDefault(record.get(2), 0);
                int stock2 = parseIntOrDefault(record.get(3), 0);
                int stock3 = parseIntOrDefault(record.get(4), 0);
                int stock4 = parseIntOrDefault(record.get(5), 0);
                int stock5 = parseIntOrDefault(record.get(6), 0);
                int stock6 = parseIntOrDefault(record.get(7), 0);
                double price = parseDoubleOrDefault(record.get(8), 0.0);
                double priceWithTax = parseDoubleOrDefault(record.get(10), 0.0);

                // Map each row to a SpareParts object
                SpareParts sparePart = new SpareParts(
                        seriesNumber,
                        record.get(1),
                        stock1, stock2, stock3, stock4, stock5, stock6,
                        price, record.get(9), priceWithTax
                );
                sparePartsList.add(sparePart);
            }
            System.out.println("CSV data successfully loaded into memory.");
        } catch (Exception e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
            e.printStackTrace();
        }

    }


    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public List<SpareParts> getSparePartsList() {
        return sparePartsList;
    }

    public List<SpareParts> getPaginatedSpareParts(int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, sparePartsList.size());

        // If the requested page is out of bounds, return an empty list
        if (fromIndex > sparePartsList.size()) {
            return new ArrayList<>();
        }

        return sparePartsList.subList(fromIndex, toIndex);
    }

    public List<SpareParts> getFilteredSpareParts(String productName, String seriesNumber) {
        return sparePartsList.stream()
                .filter(sparePart ->
                        (productName == null || productName.trim().isEmpty() || sparePart.getProductName().toLowerCase().contains(productName.toLowerCase())) &&

                                (seriesNumber == null || seriesNumber.trim().isEmpty() || sparePart.getSeriesNumber().toLowerCase().contains(seriesNumber.toLowerCase()))
                )
                .collect(Collectors.toList());
    }



    public List<SpareParts> getPaginatedAndFilteredSpareParts(int page, int size, String productName, String seriesNumber) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, sparePartsList.size());

        // Filter spare parts based on name or series number
        List<SpareParts> filteredList = sparePartsList.stream()
                .filter(sparePart -> {
                    boolean matchesProductName = (productName == null || sparePart.getProductName().toLowerCase().contains(productName.toLowerCase()));
                    boolean matchesSeriesNumber = (seriesNumber == null || sparePart.getSeriesNumber().toLowerCase().contains(seriesNumber.toLowerCase()));

                    // Add logging to see the filtering process
                    System.out.println("Filtering - Product Name: " + sparePart.getProductName() + ", Series Number: " + sparePart.getSeriesNumber());
                    System.out.println("Matches Product Name: " + matchesProductName + ", Matches Series Number: " + matchesSeriesNumber);

                    return matchesProductName && matchesSeriesNumber;
                })
                .collect(Collectors.toList());

        // If the requested page is out of bounds after filtering, return an empty list
        if (fromIndex >= filteredList.size()) {
            return new ArrayList<>();
        }

        // Return the paginated result
        return filteredList.subList(fromIndex, toIndex);
    }

}

