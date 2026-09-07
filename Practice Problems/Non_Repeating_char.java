package Week1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Problem4 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a word or sentence: ");
        String text = sc.nextLine();

        char result = findFirstNonRepeatingChar(text);

        if (result != '\0') {
            System.out.println("First Non-Repeating Character: '" + result + "'");
        } else {
            System.out.println("No Non-Repeating Character Found");
        }

        sc.close();
    }

    static char findFirstNonRepeatingChar(String text) {
        Map<Character, Integer> frequency = new HashMap<>();

        // Count frequency of every character
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
        }

        // Scan left to right for the first character with frequency exactly 1
        for (char c : text.toCharArray()) {
            if (frequency.get(c) == 1) {
                return c;
            }
        }

        return '\0'; // sentinel value meaning "not found"
    }
}
