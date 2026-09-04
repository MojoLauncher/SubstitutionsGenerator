package git.artdeell.substgen.util;

import java.util.Locale;

public class GitHubRelease {
    public static String releaseUrl = "https://github.com/%s/releases/download/%s/%%s";

    public Info[] assets;
    public static class Info {
        public String name;
        public long size;
    }

    public static String generateFileFormat(String repo, String tag) {
        return String.format(Locale.US, releaseUrl, repo, tag);
    }

    public static String getFileUrl(String releaseBaseUrl, String fileName) {
        return String.format(Locale.US, releaseBaseUrl, fileName);
    }

}
