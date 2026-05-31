package com.coding_challenge.io;

import com.coding_challenge.data.BankRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

public class FileImporter{

     public List<BankRecord> readCsv(InputStream inputStream) throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .skip(1) // skip header row
                    .filter(line -> !line.isBlank())
                    .map(this::toBankRecord)
                    .toList();
        }
    }

    private BankRecord toBankRecord(String line) {
        var cols = line.split(",");
        return new BankRecord(
                cols[0].strip(), // accountNr
                LocalDateTime.parse(cols[1].strip()), // date
                cols[2].strip(),// beneficiaryAcc
                cols[3].strip(),// comment
                Double.parseDouble(cols[4].strip()), // amount
                cols[5].strip() // currency
        );
    }

}