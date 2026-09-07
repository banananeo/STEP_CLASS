package Week1;

import java.util.Scanner;

public class Problem5 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();

        String reversedName = reverseCustomerName(customerName);

        System.out.println("Original Name: " + customerName);
        System.out.println("Reversed Name: " + reversedName);

        sc.close();
    }

    static String reverseCustomerName(String customerName) {
        char[] chars = customerName.toCharArray();
        char[] reversedChars = new char[chars.length];

        for (int i = 0; i < chars.length; i++) {
            reversedChars[i] = chars[chars.length - 1 - i];
        }

        return new String(reversedChars);
    }
}
