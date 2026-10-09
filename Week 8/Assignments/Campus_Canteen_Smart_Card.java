import java.util.*;

interface PricingPlan {
    double calculatePrice(double originalPrice);
    String getPlanName();
}

class DayScholarPlan implements PricingPlan {
    public double calculatePrice(double originalPrice) {
        return originalPrice;
    }

    public String getPlanName() {
        return "Day Scholar";
    }
}

class HostellerPlan implements PricingPlan {
    public double calculatePrice(double originalPrice) {
        return originalPrice * 0.90;
    }

    public String getPlanName() {
        return "Hosteller";
    }
}

class StaffPlan implements PricingPlan {
    public double calculatePrice(double originalPrice) {
        return originalPrice * 0.80;
    }

    public String getPlanName() {
        return "Staff";
    }
}

class Transaction {
    private final double amount;
    private final String description;

    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}

class Purchase {
    private final String itemName;
    private final double chargedAmount;
    private boolean refunded;

    public Purchase(String itemName, double chargedAmount) {
        this.itemName = itemName;
        this.chargedAmount = chargedAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public double getChargedAmount() {
        return chargedAmount;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void markRefunded() {
        refunded = true;
    }
}

class SmartCard {
    private final String cardId;
    private final PricingPlan plan;
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<Purchase> purchases = new ArrayList<>();
    private double balance;
    private boolean blocked;

    public SmartCard(String cardId, PricingPlan plan) {
        this.cardId = cardId;
        this.plan = plan;
    }

    public double getBalance() {
        return balance;
    }

    public void topUp(double amount) {
        if (blocked) {
            System.out.println("Top-up rejected: card is blocked.");
            return;
        }
        if (amount < 100) {
            System.out.println("Top-up rejected: minimum top-up is ₹100.00.");
            return;
        }
        if (balance + amount > 5000) {
            System.out.println("Top-up rejected: maximum balance is ₹5000.00.");
            return;
        }
        record(amount, "Top-up");
        System.out.printf("%s topped up with ₹%.2f. Balance: ₹%.2f.%n", cardId, amount, balance);
    }

    public Purchase purchase(String itemName, double originalPrice) {
        if (blocked) {
            System.out.println("Purchase failed: card is blocked.");
            return null;
        }
        double charged = Math.round(plan.calculatePrice(originalPrice) * 100.0) / 100.0;
        if (balance < charged) {
            System.out.printf("Purchase failed: Insufficient balance (required ₹%.2f, available ₹%.2f).%n",
                    charged, balance);
            return null;
        }
        record(-charged, "Purchase: " + itemName);
        Purchase purchase = new Purchase(itemName, charged);
        purchases.add(purchase);
        System.out.printf("%s purchased for ₹%.2f. Balance: ₹%.2f.%n", itemName, charged, balance);
        return purchase;
    }

    public void refund(Purchase purchase) {
        if (purchase == null) {
            System.out.println("Refund rejected: purchase not found.");
            return;
        }
        if (purchase.isRefunded()) {
            System.out.println("Refund rejected: " + purchase.getItemName() + " has already been refunded.");
            return;
        }
        record(purchase.getChargedAmount(), "Refund: " + purchase.getItemName());
        purchase.markRefunded();
        System.out.printf("Refund of ₹%.2f for %s processed. Balance: ₹%.2f.%n",
                purchase.getChargedAmount(), purchase.getItemName(), balance);
    }

    public void block() {
        blocked = true;
    }

    public void unblock() {
        blocked = false;
    }

    public void miniStatement() {
        StringBuilder statement = new StringBuilder();
        for (Transaction transaction : transactions) {
            if (statement.length() > 0) {
                statement.append(", ");
            }
            statement.append(String.format("%+.2f", transaction.getAmount()));
        }
        System.out.printf("Mini-statement for %s: %s = ₹%.2f.%n", cardId, statement, balance);
    }

    private void record(double amount, String description) {
        transactions.add(new Transaction(amount, description));
        balance += amount;
    }
}

public class Campus_Canteen_Smart_Card {
    public static void main(String[] args) {
        SmartCard card = new SmartCard("C-2045", new HostellerPlan());
        card.topUp(500);
        Purchase thali = card.purchase("Veg Thali", 120);
        card.purchase("Cold Coffee", 60);
        card.purchase("Items", 400);
        card.refund(thali);
        card.refund(thali);
        card.miniStatement();
    }
}
