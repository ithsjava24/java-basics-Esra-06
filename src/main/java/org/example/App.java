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


    // Alternativ 1: Inmatning
    private static void inmatning(Scanner scanner) {
        System.out.println("Ange elpriser i öre för varje timme (00-23):");
        for (int i = 0; i < HOURS_IN_DAY; i++) {
            System.out.printf("Pris för %02d-%02d: ", i, (i + 1) % 24);
            elpriser[i] = scanner.nextInt();
        }
        scanner.nextLine(); // Tar bort rest efter nextInt()
        isDataEntered = true;
        System.out.println("Inmatning slutförd.");
    }


    // Alternativ 2: Min, Max och Medel
    private static void minMaxMedel() {
        int minPris = Integer.MAX_VALUE;
        int maxPris = Integer.MIN_VALUE;
        int minTimme = 0;
        int maxTimme = 0;
        int total = 0;


        for (int i = 0; i < HOURS_IN_DAY; i++) {
            if (elpriser[i] < minPris) {
                minPris = elpriser[i];
                minTimme = i;
            }
            if (elpriser[i] > maxPris) {
                maxPris = elpriser[i];
                maxTimme = i;
            }
            total += elpriser[i];
        }


        double medel = total / (double) HOURS_IN_DAY;


        // Formatera och skriv ut enligt svensk standard
        System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", minTimme, (minTimme + 1) % 24, minPris);
        System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", maxTimme, (maxTimme + 1) % 24, maxPris);
        System.out.printf("Medelpris: %s öre/kWh\n", formatter.format(medel).replace('.', ','));
    }


    // Alternativ 3: Sortera
    private static void sortera() {


        int[] sortedPriser = Arrays.copyOf(elpriser, elpriser.length);
        boolean[] printedHours = new boolean[HOURS_IN_DAY];
        Arrays.sort(sortedPriser);


        System.out.println("Timmar sorterade efter pris (dyrast till billigast):");




        // Traversera de sorterade priserna i omvänd ordning för att hantera det högsta priset först.
        for (int i = HOURS_IN_DAY - 1; i >= 0; i--) {
            int timme = hittaTimme(sortedPriser[i], printedHours);
            int nästaTimme = (timme + 1) % HOURS_IN_DAY;


            String ändTimme = (nästaTimme == 0) ? "24" : String.format("%02d", nästaTimme);
            System.out.printf("%02d-%s %d öre\n", timme,ändTimme, sortedPriser[i]);
        }
    }


    private static int hittaTimme(int pris, boolean[] använtTimme){
        for (int i = 0; i < HOURS_IN_DAY; i++) {
            if (elpriser[i] == pris && !använtTimme[i]) {
                använtTimme[i] = true;
                return i;
            }
        }
        return -1;
    }




    // Alternativ 4: Bästa Laddningstid (4h)
    private static void bastaLaddningstid() {
        int billigasteStart = 0;
        int billigastePris = Integer.MAX_VALUE;


        // Iterate to find the cheapest 4-hour period
        for (int i = 0; i <= HOURS_IN_DAY - 4; i++) {
            int totalPris = elpriser[i] + elpriser[i + 1] + elpriser[i + 2] + elpriser[i + 3];
            if (totalPris < billigastePris) {
                billigastePris = totalPris;
                billigasteStart = i;
            }
        }


        // Beräkna genomsnittspriset för 4-timmarsperioden.
        double medelPris = billigastePris / 4.0;


        // Formatera genomsnittspriset till en decimal.
        String formattedMedelPris = String.format("%.1f", medelPris).replace('.', ',');


        // Skriv ut resultatet i det format som förväntas av testfallet
        System.out.printf("Påbörja laddning klockan %02d\nMedelpris 4h: %s öre/kWh\n", billigasteStart, formattedMedelPris);
    }
}


