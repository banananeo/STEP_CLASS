abstract class StaffMember {
    private double baseSalary;
    protected double bonusRate;

    public StaffMember(double baseSalary) {
        this(baseSalary, 0.10);
    }

    public StaffMember(double baseSalary, double bonusRate) {
        if (baseSalary < 0) {
            throw new IllegalArgumentException("Base salary cannot be negative.");
        }
        if (bonusRate < 0) {
            throw new IllegalArgumentException("Bonus rate cannot be negative.");
        }
        this.baseSalary = baseSalary;
        this.bonusRate = bonusRate;
    }

    public abstract double calculateBonus();

    public double getSalary() {
        return baseSalary;
    }

    public void setSalary(double baseSalary) {
        if (baseSalary >= 0) {
            this.baseSalary = baseSalary;
        }
        // A negative value is rejected; the existing salary remains unchanged.
    }
}

interface Auditable {
    String auditRecord();
}

class TeamLead extends StaffMember implements Auditable {
    private final int teamSize;

    public TeamLead(double baseSalary, int teamSize) {
        super(baseSalary);
        this.teamSize = teamSize;
    }

    public TeamLead(double baseSalary, double bonusRate, int teamSize) {
        super(baseSalary, bonusRate);
        this.teamSize = teamSize;
    }

    @Override
    public double calculateBonus() {
        return getSalary() * bonusRate;
    }

    @Override
    public String auditRecord() {
        return "TeamLead audit: " + teamSize + " team members, salary $" + getSalary();
    }
}

public class Quarterly_Bonus_Calculator {
    static String getAuditIfApplicable(StaffMember s) {
        if (s instanceof Auditable) {
            Auditable auditable = (Auditable) s;
            return auditable.auditRecord();
        }
        return "No audit required";
    }

    public static void main(String[] args) {
        TeamLead t = new TeamLead(60000, 5);
        System.out.println(t.calculateBonus());

        TeamLead t2 = new TeamLead(60000, 0.20, 5);
        System.out.println(t2.calculateBonus());

        t.setSalary(-5000);
        System.out.println("Salary after rejected update: " + t.getSalary());

        StaffMember ref = t; // Upcasting: TeamLead stored as StaffMember.
        System.out.println(getAuditIfApplicable(ref));
    }
}
