package com.coding_challenge.io;

import com.coding_challenge.data.BankRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;
import com.coding_challenge.exceptions.InvalidCsvFormatException;
import java.time.format.DateTimeParseException;

// CSV parsing structure and date format handling developed with Claude
public class FileImporter{

     public List<BankRecord> readCsv(InputStream inputStream) throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .skip(1) // skip header row
                    .filter(line -> !line.isBlank()) //empty lines not parsed as transactions
                    .map(this::toBankRecord) //converts row into BankRecord obj
                    .toList();
        }
    }

    // formates dates and allows optional seconds
    private static final DateTimeFormatter FORMATTER = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]");

    private BankRecord toBankRecord(String line) {
        var cols = line.split(",", -1); //exprects comma separated csv, without quoted data

        //checks if row has correct columns
        if(cols.length < 6){
            throw new InvalidCsvFormatException("Invalid row in the file, expected 6 comma-separated columns but there are"+ cols.length + ": " + line);
        }
        // validates required fields
        if (cols[0].strip().isBlank()) {
            throw new InvalidCsvFormatException("account number is empty in row: " + line);
        }
        if (cols[2].strip().isBlank()) {
            throw new InvalidCsvFormatException("beneficiary account is empty in row: " + line);
        }
        if (cols[5].strip().isBlank()) {
            throw new InvalidCsvFormatException("currency is empty in row: " + line);
        }
        if (!cols[5].strip().matches("[A-Z]{3}")) {
            throw new InvalidCsvFormatException("Invalid currency format in row: " + line + ". Expected 3 uppercase letters like EUR");
        }

     
        try{ 
            // Convert the validated CSV columns into a BankRecord
            return new BankRecord(
            cols[0].strip(), // accountNr
            LocalDateTime.parse(cols[1].strip(), FORMATTER), // date
            cols[2].strip(),// beneficiaryAcc
            cols[3].strip(),// comment
            Double.parseDouble(cols[4].strip()), // amount
            cols[5].strip() // currency
        );
        } catch (DateTimeParseException e) {
            throw new InvalidCsvFormatException(
                "Invalid date format in row: " + line + ". Expected: yyyy-MM-dd HH:mm:ss"
            );
        } catch (NumberFormatException e) {
            throw new InvalidCsvFormatException(
                "Invalid amount in row: " + line
            );
        }
    }

        
       
    

}