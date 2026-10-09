import java.time.*;
import java.util.*;

abstract class Room {
    private final String name;
    private final double pricePerNight;
    private final List<Reservation> reservations = new ArrayList<>();

    public Room(String name, double pricePerNight) {
        this.name = name;
        this.pricePerNight = pricePerNight;
    }

    public String getName() {
        return name;
    }

    public double calculatePrice(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end) * pricePerNight;
    }

    public boolean isAvailable(LocalDate start, LocalDate end) {
        for (Reservation r : reservations) {
            if (r.isActive() && start.isBefore(r.getEndDate()) && end.isAfter(r.getStartDate())) {
                return false;
            }
        }
        return true;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}

class StandardRoom extends Room {
    public StandardRoom(String name) {
        super(name, 150.0);
    }
}

class DeluxeRoom extends Room {
    public DeluxeRoom(String name) {
        super(name, 200.0);
    }
}

class Reservation {
    private final Room room;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate cancellationDeadline;
    private boolean active = true;

    public Reservation(Room room, LocalDate startDate, LocalDate endDate, LocalDate cancellationDeadline) {
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cancellationDeadline = cancellationDeadline;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel(LocalDate today) {
        if (!active) {
            System.out.println("Reservation is already inactive.");
        } else if (!today.isBefore(cancellationDeadline)) {
            System.out.println("Cancellation failed: deadline has passed.");
        } else {
            active = false;
            System.out.println("Reservation for " + room.getName() + " cancelled successfully.");
        }
    }
}

class BookingManager {
    public Reservation book(Room room, LocalDate start, LocalDate end, LocalDate cancellationDeadline) {
        if (!start.isBefore(end)) {
            System.out.println("Booking failed: invalid date range.");
            return null;
        }
        if (!room.isAvailable(start, end)) {
            System.out.println("Booking failed: " + room.getName() + " is not available for "
                    + start + " to " + end + ".");
            return null;
        }
        Reservation reservation = new Reservation(room, start, end, cancellationDeadline);
        room.addReservation(reservation);
        System.out.println(room.getName() + " booked from " + start + " to " + end
                + ". Total price: $" + String.format("%.2f", room.calculatePrice(start, end)));
        return reservation;
    }
}

public class Hotel_Booking_Cancellation_System {
    public static void main(String[] args) {
        Room deluxe = new DeluxeRoom("Deluxe Room 101");
        Room standard = new StandardRoom("Standard Room 205");
        BookingManager manager = new BookingManager();

        LocalDate start1 = LocalDate.of(2024, 12, 1);
        LocalDate end1 = LocalDate.of(2024, 12, 5);
        LocalDate deadline = LocalDate.of(2024, 11, 25);
        Reservation r1 = manager.book(deluxe, start1, end1, deadline);

        manager.book(standard, LocalDate.of(2024, 12, 3),
                LocalDate.of(2024, 12, 7), LocalDate.of(2024, 11, 27));

        manager.book(deluxe, LocalDate.of(2024, 12, 3),
                LocalDate.of(2024, 12, 7), LocalDate.of(2024, 11, 27));

        if (r1 != null) {
            r1.cancel(LocalDate.of(2024, 11, 20));
        }
    }
}
