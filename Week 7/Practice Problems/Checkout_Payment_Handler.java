abstract class PaymentMethod {
    private static int counter = 1000;
    private final String transactionId;

    public PaymentMethod() {
        transactionId = "TXN-" + (++counter);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public abstract String processPayment(double amount);

    public String processPayment(double amount, String note) {
        return processPayment(amount) + " (" + note + ")";
    }
}

class CreditCardPayment extends PaymentMethod {
    private final String cardNumberLastFour;

    public CreditCardPayment(String cardNumberLastFour) {
        this.cardNumberLastFour = cardNumberLastFour;
    }

    @Override
    public String processPayment(double amount) {
        return "Charged $" + amount + " to card ending " + cardNumberLastFour
                + " - Txn " + getTransactionId();
    }
}

class CashPayment extends PaymentMethod {
    public CashPayment() {
        super();
    }

    @Override
    public String processPayment(double amount) {
        return "Received $" + amount + " in cash - Txn " + getTransactionId();
    }
}

public class Checkout_Payment_Handler {
    static void printConfirmation(PaymentMethod payment, double amount) {
        System.out.println(payment.processPayment(amount));
    }

    static void test() {
        CreditCardPayment cc = new CreditCardPayment("4471");
        PaymentMethod ref = cc;
        System.out.println(cc.processPayment(250.0));
        System.out.println(cc.processPayment(250.0, "Birthday gift"));
        printConfirmation(ref, 250.0);

        CashPayment cash = new CashPayment();
        System.out.println(cash.processPayment(40.0));

    }

    public static void main(String[] args) {
        test();
    }
}
