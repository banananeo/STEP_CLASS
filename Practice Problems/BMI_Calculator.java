package Week1;

import java.util.Random;

public class Problem3 {

    public static void main(String[] args) {
        int teamSize = 10;
        double[] heights = new double[teamSize];
        double[] weights = new double[teamSize];

        Random random = new Random();

        // Generate random heights (1.50m - 1.95m) and weights (50kg - 100kg) for a fast
        // live demo
        for (int i = 0; i < teamSize; i++) {
            heights[i] = 1.50 + (random.nextDouble() * 0.45);
            weights[i] = 50 + (random.nextDouble() * 50);
        }

        printWellnessReport(heights, weights);
    }

    static double calculateBmi(double height, double weight) {
        return weight / (height * height);
    }

    static String getBmiStatus(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    static void printWellnessReport(double[] heights, double[] weights) {
        System.out.printf("%-10s %-12s %-12s %-8s %-12s%n",
                "Person", "Height (m)", "Weight (kg)", "BMI", "Status");

        for (int i = 0; i < heights.length; i++) {
            double bmi = calculateBmi(heights[i], weights[i]);
            String status = getBmiStatus(bmi);

            System.out.printf("%-10s %-12.2f %-12.2f %-8.2f %-12s%n",
                    "Person " + (i + 1), heights[i], weights[i], bmi, status);
        }
    }
}
