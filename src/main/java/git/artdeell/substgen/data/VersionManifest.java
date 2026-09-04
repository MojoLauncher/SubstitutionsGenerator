package git.artdeell.substgen.data;

public class VersionManifest {
    public Download[] versions;
    public static class Download {
        public String type;
        public String url;
        public String id;
    }
}
