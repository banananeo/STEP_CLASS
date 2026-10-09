import java.util.*;

abstract class Vehicle {
    private final String name;
    private boolean available = true;

    public Vehicle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double calculateCharge(int days);
}

class StandardCar extends Vehicle {
    public StandardCar(String name) {
        super(name);
    }

    public double calculateCharge(int days) {
        return days * 50.0;
    }
}

class LuxuryCar extends Vehicle {
    public LuxuryCar(String name) {
        super(name);
    }

    public double calculateCharge(int days) {
        return days * 100.0;
    }
}

class Rental {
    private final Vehicle vehicle;
    private final int days;
    private boolean returned;

    public Rental(Vehicle vehicle, int days) {
        this.vehicle = vehicle;
        this.days = days;
    }

    public double getTotalCharge() {
        return vehicle.calculateCharge(days);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isReturned() {
        return returned;
    }

    public void markReturned() {
        returned = true;
        vehicle.setAvailable(true);
    }
}

class RentalService {
    private final List<Rental> rentals = new ArrayList<>();

    public Rental rent(Vehicle vehicle, int days) {
        if (!vehicle.isAvailable()) {
            System.out.println(vehicle.getName() + " cannot be rented: currently unavailable.");
            return null;
        }
        if (days <= 0) {
            System.out.println("Rental duration must be positive.");
            return null;
        }
        vehicle.setAvailable(false);
        Rental rental = new Rental(vehicle, days);
        rentals.add(rental);
        System.out.printf("%s rented for %d days. Total charge: $%.2f%n",
                vehicle.getName(), days, rental.getTotalCharge());
        return rental;
    }

    public void returnVehicle(Rental rental) {
        if (rental != null && !rental.isReturned()) {
            rental.markReturned();
            System.out.println(rental.getVehicle().getName() + " returned. Now available.");
        }
    }
}

public class Vehicle_Rental_System {
    public static void main(String[] args) {
        Vehicle luxury = new LuxuryCar("Luxury Car A");
        Vehicle standard = new StandardCar("Standard Car B");
        RentalService service = new RentalService();

        Rental r1 = service.rent(luxury, 3);
        service.rent(standard, 5);
        service.returnVehicle(r1);
    }
}
