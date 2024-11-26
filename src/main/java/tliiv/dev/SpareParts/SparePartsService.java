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

//apache commons csv
//https://www.baeldung.com/jackson  parseimiseks  jsonisse

@Service
public class SparePartsService {
    private final List<SpareParts> sparePartsList = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("/csv/LE.txt"), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withDelimiter('\t') // Set tab as delimiter
                     .withIgnoreSurroundingSpaces() // Ignore spaces around fields
                     .withIgnoreEmptyLines() // Skip empty lines
                     .withTrim())) { // Trim leading and trailing whitespace

            for (CSVRecord record : csvParser) {
                // Safe parsing for numeric fields (check if empty and use default value)
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
                        record.get(0),
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


}

