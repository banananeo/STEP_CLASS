import java.util.*;

interface IPaymentMethod {
    boolean pay(double amount);
    String getMethodName();
}

class CreditCardPayment implements IPaymentMethod {
    public boolean pay(double amount) {
        return true;
    }

    public String getMethodName() {
        return "Credit Card";
    }
}

class DigitalWalletPayment implements IPaymentMethod {
    public boolean pay(double amount) {
        return false;
    }

    public String getMethodName() {
        return "Digital Wallet";
    }
}

class FoodItem {
    private final String name;
    private final double price;

    public FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class LineItem {
    private final FoodItem item;
    private final int quantity;

    public LineItem(FoodItem item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.item = item;
        this.quantity = quantity;
    }

    public double getTotal() {
        return item.getPrice() * quantity;
    }

    public String getName() {
        return item.getName();
    }

    public int getQuantity() {
        return quantity;
    }
}

class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void notifyCustomer(String message) {
        System.out.println("Notification: " + message);
    }
}

class FoodOrder {
    private static int counter = 122;
    private final int orderId = ++counter;
    private final Customer customer;
    private final List<LineItem> items = new ArrayList<>();
    private String status = "Pending Payment";

    public FoodOrder(Customer customer) {
        this.customer = customer;
        System.out.println("Order created.");
    }

    public void addItem(FoodItem item, int quantity) {
        LineItem lineItem = new LineItem(item, quantity);
        items.add(lineItem);
        System.out.println("Added " + item.getName() + " (Qty " + quantity + ")");
    }

    public boolean placeOrder(IPaymentMethod paymentMethod) {
        if (items.isEmpty()) {
            System.out.println("Cannot place order: Order must contain at least one item.");
            return false;
        }

        System.out.println("Order placed successfully.");
        boolean success = paymentMethod.pay(getTotal());
        if (success) {
            status = "Paid";
            System.out.println("Payment via " + paymentMethod.getMethodName() + " successful.");
            System.out.println("Order status: " + status);
            customer.notifyCustomer("Order #" + orderId + " placed and paid.");
        } else {
            status = "Pending Payment";
            System.out.println("Payment via " + paymentMethod.getMethodName() + " failed.");
            System.out.println("Order status: " + status);
            customer.notifyCustomer("Order #" + orderId + " placed, awaiting payment.");
        }
        return success;
    }

    private double getTotal() {
        double total = 0;
        for (LineItem item : items) {
            total += item.getTotal();
        }
        return total;
    }
}

public class Food_Order_Flexible_Payment_System {
    public static void main(String[] args) {
        Customer customer = new Customer("Alex");
        FoodOrder emptyOrder = new FoodOrder(customer);
        emptyOrder.placeOrder(new CreditCardPayment());

        FoodOrder order1 = new FoodOrder(customer);
        order1.addItem(new FoodItem("Pizza", 12.0), 2);
        order1.addItem(new FoodItem("Soda", 2.0), 1);
        order1.placeOrder(new CreditCardPayment());

        FoodOrder order2 = new FoodOrder(customer);
        order2.addItem(new FoodItem("Burger", 8.0), 1);
        order2.placeOrder(new DigitalWalletPayment());
    }
}
