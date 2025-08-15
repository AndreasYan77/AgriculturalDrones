import java.util.ArrayList;
import java.util.Scanner;

public class AgriculturalDroneScorer {

    // Ten metrics calculation
    public static ArrayList<Double> calculateAllScores(
            int languageSupport,      // Number of supported languages
            int trainingDays,         // Training Days
            double maxTakeoffWeight,  // Maximum takeoff weight (kg)
            double droneMinTemp,      // Minimum operating temperature of drone(℃)
            double droneMaxTemp,      // Maximum operating temperature of drone(℃)
            int windTolerance,        // Maximum tolerance wind speed(m/s)
            int IPrating,     // IP rating(IP00-IP69)
            double dronePrice,        // Drone prices(RMB)
            double horizontalError,   // Horizontal hovering error(m)
            double verticalError,     // Vertical hovering error(m)
            double maxRadius          // Maximum flight distance(km)
    ) {
        ArrayList<Double> scores = new ArrayList<>();

        // Important Data (Beijing)
        double localMinTemp = -8; // Local minimum temperature(℃)
        double localMaxTemp = 31; // Local maximum temperature(℃)
        double maxWindVelocity = 4.0; //Local maximum wind velocity(m/s)
        double ruralIncome = 23199.0; // Per capita disposable income in rural areas
        double subsidyAmount = 14400.0; //Subsidy amount

        // 1. Language
        scores.add(Math.min(10.0, languageSupport));

        // 2. Training Duration
        scores.add(Math.max(10.0 - trainingDays, 0.0));

        // 3. Max Takeoff Weight
        scores.add(Math.min(10.0, maxTakeoffWeight / 15));

        // 4. Temperature Range
        double countTemp = 0.0;
        double range = droneMaxTemp - droneMinTemp;
        if(localMinTemp < droneMinTemp){
            countTemp = droneMinTemp - localMinTemp;
        }
        if(localMaxTemp > droneMaxTemp){
            countTemp += localMaxTemp - droneMaxTemp;
        }
        scores.add(10 - countTemp / range);

        // 5. Wind Tolerance
        double countWindDifference = 0.0;
        if(maxWindVelocity > windTolerance){
            countWindDifference = maxWindVelocity - windTolerance;
        }
        scores.add(10 - countWindDifference / windTolerance);

        // 6. IP Rating
        int dustRating = IPrating / 10;
        double dustScore = dustRating + 1;
        int waterproofRating = IPrating % 10;
        double waterproofScore;
        if (waterproofRating >= 8){
            waterproofScore = 5;
        }else if (waterproofRating == 7){
            waterproofScore = 4;
        } else if (waterproofRating >= 5) {
            waterproofScore = 3;
        } else if (waterproofRating >= 3){
            waterproofScore = 2;
        } else if (waterproofRating == 2) {
            waterproofScore = 1;
        } else {
            waterproofScore = 0;
        }
        double IPscore = dustScore - 1 + waterproofScore;
        scores.add(IPscore);

        // 7. & 8. Price & Subsidy
        double actualCost = Math.min(ruralIncome, dronePrice - subsidyAmount);
        scores.add(20 * (ruralIncome - actualCost) / ruralIncome);

        // 9. Hovering Accuracy
        scores.add(10 - 10 * horizontalError * verticalError);

        // 10. Maximum Radius
        scores.add(Math.max(10.0, maxRadius / 0.2));

        return scores;
    }

    // Calculate the total score
    public static double calculateTotalScore(ArrayList<Double> scores) {
        double sum = 0.0;
        for(int i = 0; i < scores.size(); i++){
            sum += scores.get(i);
            System.out.println( scores.get(i));
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Agricultural Drone Scoring System ===");
        System.out.println("Please enter the following parameters:");

        System.out.print("Number of supported languages: ");
        int languageSupport = scanner.nextInt();

        System.out.print("Training Days required: ");
        int trainingDays = scanner.nextInt();

        System.out.print("Maximum takeoff weight (kg): ");
        double maxTakeoffWeight = scanner.nextDouble();

        System.out.print("Minimum operating temperature (°C): ");
        double droneMinTemp = scanner.nextDouble();

        System.out.print("Maximum operating temperature (°C): ");
        double droneMaxTemp = scanner.nextDouble();

        System.out.print("Maximum tolerance wind speed (m/s): ");
        int windTolerance = scanner.nextInt();

        System.out.print("IP rating(only number): ");
        int IPrating = scanner.nextInt();

        System.out.print("Drone price (RMB): ");
        double dronePrice = scanner.nextDouble();

        System.out.print("Horizontal hovering error (m): ");
        double horizontalError = scanner.nextDouble();

        System.out.print("Vertical hovering error (m): ");
        double verticalError = scanner.nextDouble();

        System.out.print("Maximum flight radius (km): ");
        double maxRadius = scanner.nextDouble();

        ArrayList<Double> scores = calculateAllScores(
                languageSupport,
                trainingDays,
                maxTakeoffWeight,
                droneMinTemp,
                droneMaxTemp,
                windTolerance,
                IPrating,
                dronePrice,
                horizontalError,
                verticalError,
                maxRadius
        );

        System.out.println("Individual Scores:");
        double totalScore = calculateTotalScore(scores);
        System.out.println("------------------------------------------");
        System.out.println("The total score is: " + totalScore);

        scanner.close();

        //DJI T100 Agricultural Drone Specifications 3WWDZ-U75A
        /*
        languageSupport: 1
        trainingDays: ?
        maxTakeoffWeight: 149.9
        droneMinTemp: 0
        droneMaxTemp: 40
        windTolerance: 6
        IPrating: IP67
        dronePrice: 63999 - 66999
        horizontalError: 0.1
        verticalError: 0.1
        maxRadius: 2
         */
    }
}