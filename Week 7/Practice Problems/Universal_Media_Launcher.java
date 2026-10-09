interface Playable {
    String play();
    String play(int fromSecond);
    String pause();
}

abstract class MediaFile {
    private static int counter = 1000;
    private final String fileId;

    public MediaFile() {
        fileId = "MF-" + (++counter);
    }

    public String getFileId() {
        return fileId;
    }

    public abstract String getFormatInfo();
}

class AudioFile extends MediaFile implements Playable {
    private final String title;

    public AudioFile(String title) {
        this.title = title;
    }

    @Override
    public String play() {
        return "Playing audio: " + title;
    }

    @Override
    public String play(int fromSecond) {
        int minutes = fromSecond / 60;
        int seconds = fromSecond % 60;
        return "Playing audio: " + title + " from " + minutes + ":" 
                + (seconds < 10 ? "0" : "") + seconds;
    }

    @Override
    public String pause() {
        return "Paused audio: " + title;
    }

    @Override
    public String getFormatInfo() {
        return "Audio file, ID: " + getFileId();
    }
}

class Podcast implements Playable {
    private final String showName;
    private final int episodeNumber;

    public Podcast(String showName, int episodeNumber) {
        this.showName = showName;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String play() {
        return "Streaming episode " + episodeNumber + " of " + showName;
    }

    @Override
    public String play(int fromSecond) {
        return "Streaming episode " + episodeNumber + " of " + showName
                + " from " + (fromSecond / 60) + ":"
                + ((fromSecond % 60 < 10) ? "0" : "") + (fromSecond % 60);
    }

    @Override
    public String pause() {
        return "Paused stream: " + showName + " episode " + episodeNumber;
    }
}

public class Universal_Media_Launcher {
    static void launchAll(Playable[] items) {
        for (Playable item : items) {
            System.out.println(item.play());
        }
    }

    public static void main(String[] args) {
        AudioFile a = new AudioFile("Morning Jazz");
        System.out.println(a.play());
        System.out.println(a.play(30));
        System.out.println(a.getFormatInfo());

        Podcast p = new Podcast("Tech Talk", 12);
        System.out.println(p.play());

        Playable ref = a; // Upcasting: AudioFile stored as Playable.
        System.out.println(ref.play());
        launchAll(new Playable[]{ref, p});

        // AudioFile IS-A MediaFile and CAN-DO Playable.
        // Podcast only needs the Playable capability, not MediaFile's fileId/format data.
    }
}
