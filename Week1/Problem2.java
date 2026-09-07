package Week1;

import java.util.Scanner;

public class Problem2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a word or phrase: ");
        String text = sc.nextLine();

        boolean iterativeResult = isPalindromeIterative(text);
        boolean recursiveResult = isPalindromeRecursive(text);
        boolean arrayReversalResult = isPalindromeArrayReversal(text);

        System.out.println("Iterative: " + (iterativeResult ? "Palindrome" : "Not Palindrome")
                + " | Recursive: " + (recursiveResult ? "Palindrome" : "Not Palindrome")
                + " | Array Reversal: " + (arrayReversalResult ? "Palindrome" : "Not Palindrome"));

        sc.close();
    }

    // Approach 1: Iterative comparison from both ends
    static boolean isPalindromeIterative(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        int left = 0, right = cleaned.length() - 1;

        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // Approach 2: Recursive comparison
    static boolean isPalindromeRecursive(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        return checkRecursive(cleaned, 0, cleaned.length() - 1);
    }

    static boolean checkRecursive(String s, int left, int right) {
        if (left >= right) {
            return true;
        }
        if (s.charAt(left) != s.charAt(right)) {
            return false;
        }
        return checkRecursive(s, left + 1, right - 1);
    }

    // Approach 3: Array reversal
    static boolean isPalindromeArrayReversal(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        char[] original = cleaned.toCharArray();
        char[] reversed = new char[original.length];

        for (int i = 0; i < original.length; i++) {
            reversed[i] = original[original.length - 1 - i];
        }

        return new String(original).equals(new String(reversed));
    }
}
