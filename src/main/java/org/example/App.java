package org.example;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class App {

    private static final int HOURS_IN_DAY = 24;
    private static int[] elpriser = new int[HOURS_IN_DAY];
    private static boolean isDataEntered = false;

    // Använd svensk standard för decimalformat
    private static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("sv-SE"));
    private static final DecimalFormat formatter = new DecimalFormat("#0.00", symbols);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean runProgram = true;

        while (runProgram) {
            String menu = """
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                e. Avsluta
                """;
            System.out.println(menu);


            // Tar emot användarens val
            String val = scanner.nextLine();

            switch (val.toLowerCase()) {
                case "1":
                    inmatning(scanner);
                    break;
                case "2":
                    if (isDataEntered) {
                        minMaxMedel();
                    } else {
                        System.out.println("Du måste mata in elpriser först!");
                    }
                    break;
                case "3":
                    if (isDataEntered) {
                        sortera();
                    } else {
                        System.out.println("Du måste mata in elpriser först!");
                    }
                    break;
                case "4":
                    if (isDataEntered) {
                        bastaLaddningstid();
                    } else {
                        System.out.println("Du måste mata in elpriser först!");
                    }
                    break;
                case "e":
                case "E":
                    System.out.println("Avslutar programmet...");
                    runProgram = false;
                    break;
                default:
                    System.out.println("Felaktigt val, försök igen.");
                    break;
            }

            System.out.println(); // Skriver ut en tom rad för bättre läsbarhet
        }

        scanner.close();
    }
}

