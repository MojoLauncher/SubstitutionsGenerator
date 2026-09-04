package git.artdeell.substgen.data;

public class Artifact {
    public final String path;
    public final String sha1;
    public final long size;
    public final String url;

    public Artifact(String path, String sha1, long size, String url) {
        this.path = path;
        this.sha1 = sha1;
        this.size = size;
        this.url = url;
    }
}
