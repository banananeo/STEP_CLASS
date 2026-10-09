interface Exportable {
    String exportData();
}

class ExportCounter {
    private static int totalExports = 0;

    public static void increment() {
        totalExports++;
    }

    public static int getTotalExports() {
        return totalExports;
    }
}

class ReportGenerator implements Exportable {
    private final String reportName;

    public ReportGenerator(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String exportData() {
        ExportCounter.increment();
        return "Exported report: " + reportName;
    }
}

class UserProfile implements Exportable {
    private final String username;

    public UserProfile(String username) {
        this.username = username;
    }

    @Override
    public String exportData() {
        ExportCounter.increment();
        return "Exported profile: " + username;
    }
}

public class One_Click_Data_Export {
    static int getTotalExports() {
        return ExportCounter.getTotalExports();
    }

    static void exportAll(Exportable[] items) {
        for (Exportable item : items) {
            System.out.println(item.exportData());
        }
    }

    public static void main(String[] args) {
        ReportGenerator r = new ReportGenerator("Sales Q1");
        UserProfile u = new UserProfile("jane_doe");

        System.out.println(r.exportData());
        System.out.println(u.exportData());

        Exportable ref = r;
        exportAll(new Exportable[]{ref, u});
        System.out.println(getTotalExports());
    }
}
