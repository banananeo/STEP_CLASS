import java.time.*;

enum LeaveStatus {
    PENDING, APPROVED, REJECTED
}

abstract class Employee {
    private final String name;

    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int getMaximumLeaveDays();
}

class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String name) {
        super(name);
    }

    public int getMaximumLeaveDays() {
        return 30;
    }
}

class PartTimeEmployee extends Employee {
    public PartTimeEmployee(String name) {
        super(name);
    }

    public int getMaximumLeaveDays() {
        return 15;
    }
}

class LeaveRequest {
    private final Employee employee;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private LeaveStatus status = LeaveStatus.PENDING;

    public LeaveRequest(Employee employee, LocalDate startDate, LocalDate endDate) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void review(LeaveStatus newStatus) {
        if (status != LeaveStatus.PENDING) {
            System.out.println("Cannot change status: " + status + " request cannot revert to Pending.");
            return;
        }
        if (newStatus == LeaveStatus.PENDING) {
            System.out.println("Review must approve or reject the request.");
            return;
        }
        status = newStatus;
        System.out.println("Leave request for " + employee.getName() + " "
                + status.toString().toLowerCase() + ". Status: " + status.toString().substring(0, 1)
                + status.toString().substring(1).toLowerCase());
    }

    public void submit() {
        System.out.println("Leave request submitted by " + employee.getName() + " for "
                + startDate + " to " + endDate + ". Status: Pending.");
    }
}

public class Employee_Leave_Request_Management {
    public static void main(String[] args) {
        Employee john = new FullTimeEmployee("John Doe");
        LeaveRequest johnRequest = new LeaveRequest(john,
                LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 12));
        johnRequest.submit();
        johnRequest.review(LeaveStatus.APPROVED);

        Employee jane = new PartTimeEmployee("Jane Smith");
        LeaveRequest janeRequest = new LeaveRequest(jane,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5));
        janeRequest.submit();

        johnRequest.review(LeaveStatus.PENDING);
    }
}
