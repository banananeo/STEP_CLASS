import java.util.*;

interface CreditPolicy {
    int getCreditLimit();
    String getTypeName();
}

class RegularPolicy implements CreditPolicy {
    public int getCreditLimit() {
        return 24;
    }

    public String getTypeName() {
        return "Regular";
    }
}

class HonorsPolicy implements CreditPolicy {
    public int getCreditLimit() {
        return 28;
    }

    public String getTypeName() {
        return "Honors";
    }
}

class ExchangePolicy implements CreditPolicy {
    public int getCreditLimit() {
        return 20;
    }

    public String getTypeName() {
        return "Exchange";
    }
}

class Student {
    private final String name;
    private final CreditPolicy policy;
    private int currentCredits;
    private final Set<Elective> enrolled = new HashSet<>();
    private final Set<Elective> waiting = new HashSet<>();

    public Student(String name, CreditPolicy policy, int currentCredits) {
        this.name = name;
        this.policy = policy;
        this.currentCredits = currentCredits;
    }

    public String getName() {
        return name;
    }

    public int getCurrentCredits() {
        return currentCredits;
    }

    public int getCreditLimit() {
        return policy.getCreditLimit();
    }

    public boolean canTake(Elective elective) {
        return currentCredits + elective.getCredits() <= getCreditLimit();
    }

    public void enroll(Elective elective) {
        currentCredits += elective.getCredits();
        enrolled.add(elective);
        waiting.remove(elective);
    }

    public void drop(Elective elective) {
        currentCredits -= elective.getCredits();
        enrolled.remove(elective);
    }

    public boolean isEnrolled(Elective elective) {
        return enrolled.contains(elective);
    }

    public boolean isWaiting(Elective elective) {
        return waiting.contains(elective);
    }

    public void addWaiting(Elective elective) {
        waiting.add(elective);
    }

    public void removeWaiting(Elective elective) {
        waiting.remove(elective);
    }

    public String getPolicyName() {
        return policy.getTypeName();
    }
}

class Elective {
    private final String name;
    private final int credits;
    private final int capacity;
    private final List<Student> enrolled = new ArrayList<>();
    private final Queue<Student> waitlist = new LinkedList<>();

    public Elective(String name, int credits, int capacity) {
        this.name = name;
        this.credits = credits;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull() {
        return enrolled.size() >= capacity;
    }

    public boolean contains(Student student) {
        return enrolled.contains(student);
    }

    public boolean isWaiting(Student student) {
        return waitlist.contains(student);
    }

    public void addEnrolled(Student student) {
        enrolled.add(student);
    }

    public void removeEnrolled(Student student) {
        enrolled.remove(student);
    }

    public void addToWaitlist(Student student) {
        waitlist.offer(student);
    }

    public Student pollWaitlist() {
        return waitlist.poll();
    }

    public boolean hasWaitlist() {
        return !waitlist.isEmpty();
    }
}

class EnrollmentService {
    public void enroll(Student student, Elective elective) {
        if (student.isEnrolled(elective) || student.isWaiting(elective)) {
            System.out.println("Enrollment failed: " + student.getName() + " is already enrolled or waitlisted.");
            return;
        }
        if (!student.canTake(elective)) {
            System.out.printf("Enrollment failed: %s would exceed the %s credit limit (%d/%d).%n",
                    student.getName(), student.getPolicyName(),
                    student.getCurrentCredits() + elective.getCredits(), student.getCreditLimit());
            return;
        }
        if (elective.isFull()) {
            elective.addToWaitlist(student);
            student.addWaiting(elective);
            System.out.println(elective.getName() + " is full. " + student.getName()
                    + " added to waitlist (position " + countWaitlistPosition(elective, student) + ").");
            return;
        }
        elective.addEnrolled(student);
        student.enroll(elective);
        System.out.printf("%s enrolled in %s (credits: %d/%d).%n", student.getName(),
                elective.getName(), student.getCurrentCredits(), student.getCreditLimit());
    }

    private int countWaitlistPosition(Elective elective, Student target) {
        return 1;
    }

    public void drop(Student student, Elective elective) {
        if (!student.isEnrolled(elective) || !elective.contains(student)) {
            System.out.println(student.getName() + " is not enrolled in " + elective.getName() + ".");
            return;
        }
        elective.removeEnrolled(student);
        student.drop(elective);
        System.out.printf("%s dropped %s (credits: %d/%d).%n", student.getName(),
                elective.getName(), student.getCurrentCredits(), student.getCreditLimit());
        promote(elective);
    }

    private void promote(Elective elective) {
        while (!elective.isFull() && elective.hasWaitlist()) {
            Student candidate = elective.pollWaitlist();
            candidate.removeWaiting(elective);
            if (candidate.canTake(elective)) {
                elective.addEnrolled(candidate);
                candidate.enroll(elective);
                System.out.printf("%s promoted from waitlist and enrolled in %s (credits: %d/%d).%n",
                        candidate.getName(), elective.getName(), candidate.getCurrentCredits(),
                        candidate.getCreditLimit());
                return;
            }
            System.out.println("Promotion skipped: " + candidate.getName() + " exceeds credit limit.");
        }
    }
}

public class Elective_Seat_Rush {
    public static void main(String[] args) {
        Elective elective = new Elective("Cloud Computing", 4, 2);
        EnrollmentService service = new EnrollmentService();

        Student asha = new Student("Asha", new RegularPolicy(), 20);
        Student ravi = new Student("Ravi", new HonorsPolicy(), 22);
        Student neha = new Student("Neha", new ExchangePolicy(), 12);
        Student kiran = new Student("Kiran", new RegularPolicy(), 22);

        service.enroll(asha, elective);
        service.enroll(ravi, elective);
        service.enroll(neha, elective);
        service.enroll(kiran, elective);
        service.drop(asha, elective);
    }
}
