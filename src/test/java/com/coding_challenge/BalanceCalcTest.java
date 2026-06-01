package com.coding_challenge;

import com.coding_challenge.calc.BalanceCalc;
import com.coding_challenge.data.BankRecord;
import org.junit.jupiter.api.Test;
import com.coding_challenge.exceptions.AccountNotFoundException;


import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class BalanceCalcTest {

    private final BalanceCalc calculator = new BalanceCalc();

    private List<BankRecord> sampleTransactions() {
        return List.of(
                new BankRecord("LT678909876", LocalDateTime.of(2026, 1, 15, 10, 0), "LT456789", "Groceries", -300.00, "EUR"),
                new BankRecord("LT678909876", LocalDateTime.of(2026, 2, 10, 10, 0), "LT987653", "Salary",    2000.00, "EUR"),
                new BankRecord("LT678909876", LocalDateTime.of(2026, 3, 5,  10, 0), "LT996627", "Rent",      -500.00, "EUR")
        );
    }

    @Test
    void calculateReturnsCorrectBalance() {
        var result = calculator.calculate(
                sampleTransactions(),
                "LT678909876",
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 23, 59)
        );
        assertEquals(1200.00, result.get("EUR"), 0.01);
    }

    @Test
    void calculateRespectsDateRange() {
        var result = calculator.calculate(
                sampleTransactions(),
                "LT678909876",
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 2, 1, 0, 0) // before the salary transaction
        );
        assertEquals(-300.00, result.get("EUR"), 0.001);
    }

    @Test
    void calculateThrowsForUnknownAccount() {
        assertThrows(AccountNotFoundException.class, () ->
                calculator.calculate(
                        sampleTransactions(),
                        "UNKNOWN",
                        null,
                        null
                )
        );
    }
}