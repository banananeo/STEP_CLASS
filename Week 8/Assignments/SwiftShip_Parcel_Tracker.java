import java.util.*;

interface ShippingType {
    double calculateCharge(double weight);
}

class StandardShipping implements ShippingType {
    public double calculateCharge(double weight) {
        return 40 + 10 * weight;
    }
}

class ExpressShipping implements ShippingType {
    public double calculateCharge(double weight) {
        return 80 + 15 * weight;
    }
}

class FragileShipping implements ShippingType {
    public double calculateCharge(double weight) {
        return new StandardShipping().calculateCharge(weight) + 50;
    }
}

interface NotificationChannel {
    void notifyStatus(String parcelId, String status);
}

class SmsChannel implements NotificationChannel {
    public void notifyStatus(String parcelId, String status) {
        System.out.println("[SMS] " + parcelId + " is now " + status + ".");
    }
}

class EmailChannel implements NotificationChannel {
    public void notifyStatus(String parcelId, String status) {
        System.out.println("[Email] " + parcelId + " is now " + status + ".");
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
}

class Parcel {
    private static final List<String> STATUSES = Arrays.asList(
            "BOOKED", "PICKED_UP", "IN_TRANSIT", "OUT_FOR_DELIVERY", "DELIVERED");
    private final String id;
    private final double weight;
    private final ShippingType shippingType;
    private final List<NotificationChannel> channels;
    private String status = "BOOKED";
    private boolean cancelled;

    public Parcel(String id, double weight, ShippingType shippingType,
                  List<NotificationChannel> channels) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive.");
        }
        this.id = id;
        this.weight = weight;
        this.shippingType = shippingType;
        this.channels = new ArrayList<>(channels);
    }

    public double getCharge() {
        return shippingType.calculateCharge(weight);
    }

    public String getStatus() {
        return status;
    }

    public void changeStatus(String nextStatus) {
        int currentIndex = STATUSES.indexOf(status);
        int nextIndex = STATUSES.indexOf(nextStatus);
        if (cancelled || nextIndex != currentIndex + 1) {
            System.out.println("Invalid transition: " + status + " → " + nextStatus + " is not allowed.");
            return;
        }
        status = nextStatus;
        for (NotificationChannel channel : channels) {
            channel.notifyStatus(id, status);
        }
    }

    public void cancel() {
        if (!"BOOKED".equals(status) || cancelled) {
            System.out.println("Cancellation failed: " + id + " can be cancelled only while BOOKED.");
            return;
        }
        cancelled = true;
        System.out.println(id + " cancelled.");
    }
}

class ParcelService {
    public Parcel book(String id, double weight, ShippingType type,
                       List<NotificationChannel> channels) {
        Parcel parcel = new Parcel(id, weight, type, channels);
        System.out.printf("Parcel %s booked (%s, %.0f kg).%n", id, type.getClass().getSimpleName().replace("Shipping", ""), weight);
        System.out.printf("Charge: ₹%.2f%n", parcel.getCharge());
        for (NotificationChannel channel : channels) {
            channel.notifyStatus(id, parcel.getStatus());
        }
        return parcel;
    }
}

public class SwiftShip_Parcel_Tracker {
    public static void main(String[] args) {
        ParcelService service = new ParcelService();
        Parcel parcel = service.book("P101", 2, new ExpressShipping(),
                Arrays.asList(new SmsChannel(), new EmailChannel()));
        parcel.changeStatus("PICKED_UP");
        parcel.cancel();
        parcel.changeStatus("IN_TRANSIT");
        parcel.changeStatus("DELIVERED");
    }
}
