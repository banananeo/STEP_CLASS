import java.util.*;

interface ScoringRule {
    double calculate(double idea, double execution, double presentation);
}

class InnovationScoringRule implements ScoringRule {
    public double calculate(double idea, double execution, double presentation) {
        return idea * 0.5 + execution * 0.3 + presentation * 0.2;
    }
}

class OpenScoringRule implements ScoringRule {
    public double calculate(double idea, double execution, double presentation) {
        return (idea + execution + presentation) / 3.0;
    }
}

class Student {
    private final String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Project {
    private final String name;
    private final Team team;
    private Score score;

    public Project(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}

class Score {
    private final double idea;
    private final double execution;
    private final double presentation;

    public Score(double idea, double execution, double presentation) {
        if (!valid(idea) || !valid(execution) || !valid(presentation)) {
            throw new IllegalArgumentException("Each rating must be between 0 and 10.");
        }
        this.idea = idea;
        this.execution = execution;
        this.presentation = presentation;
    }

    private boolean valid(double value) {
        return value >= 0 && value <= 10;
    }

    public double calculate(ScoringRule rule) {
        return rule.calculate(idea, execution, presentation);
    }
}

class Team {
    private final String name;
    private final List<Student> members;
    private final ScoringRule scoringRule;
    private Project project;

    public Team(String name, List<Student> members, ScoringRule scoringRule) {
        this.name = name;
        this.members = new ArrayList<>(members);
        this.scoringRule = scoringRule;
    }

    public String getName() {
        return name;
    }

    public List<Student> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public ScoringRule getScoringRule() {
        return scoringRule;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        if (this.project != null) {
            throw new IllegalStateException("A team can submit only one project.");
        }
        this.project = project;
    }
}

class Hackathon {
    private enum State { OPEN, JUDGING, PUBLISHED }

    private State state = State.OPEN;
    private final List<Team> teams = new ArrayList<>();
    private final Set<Student> registeredStudents = new HashSet<>();
    private final List<Project> projects = new ArrayList<>();

    public boolean registerTeam(Team team) {
        if (state != State.OPEN) {
            System.out.println("Registration failed: Hackathon is not open.");
            return false;
        }
        int size = team.getMembers().size();
        if (size < 2 || size > 4) {
            System.out.println("Registration failed: A team must have 2 to 4 members.");
            return false;
        }
        for (Student student : team.getMembers()) {
            if (registeredStudents.contains(student)) {
                System.out.println("Registration failed: A student can belong to only one team per hackathon.");
                return false;
            }
        }
        teams.add(team);
        registeredStudents.addAll(team.getMembers());
        System.out.println("Team " + team.getName() + " registered (" + size + " members).");
        return true;
    }

    public Project submitProject(Team team, String projectName) {
        if (state == State.PUBLISHED || !teams.contains(team)) {
            System.out.println("Project submission failed.");
            return null;
        }
        try {
            Project project = new Project(projectName, team);
            team.setProject(project);
            projects.add(project);
            state = State.JUDGING;
            System.out.println("Project '" + projectName + "' submitted by " + team.getName() + ".");
            return project;
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void recordScore(Project project, Score score) {
        if (state == State.PUBLISHED) {
            System.out.println("Rescore rejected: Results have already been published.");
            return;
        }
        if (!projects.contains(project)) {
            System.out.println("Score rejected: Project is not registered.");
            return;
        }
        project.setScore(score);
        System.out.printf("Score recorded for '%s'.%n", project.getName());
        System.out.printf("Final score: %.2f%n", score.calculate(project.getTeam().getScoringRule()));
    }

    public void publishResults() {
        state = State.PUBLISHED;
        System.out.println("Results published.");
    }
}

public class Code_Sprint_Judging_Desk {
    public static void main(String[] args) {
        Student asha = new Student("Asha");
        Student ravi = new Student("Ravi");
        Student neha = new Student("Neha");
        Student kiran = new Student("Kiran");

        Hackathon hackathon = new Hackathon();
        Team byteBusters = new Team("ByteBusters",
                Arrays.asList(asha, ravi, neha), new InnovationScoringRule());
        hackathon.registerTeam(byteBusters);

        Team soloCoder = new Team("SoloCoder",
                Arrays.asList(kiran), new OpenScoringRule());
        hackathon.registerTeam(soloCoder);

        Project project = hackathon.submitProject(byteBusters, "SmartAttend");
        if (project != null) {
            hackathon.recordScore(project, new Score(8, 7, 9));
            hackathon.publishResults();
            hackathon.recordScore(project, new Score(10, 7, 9));
        }
    }
}
