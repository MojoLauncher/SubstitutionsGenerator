package git.artdeell.substgen.lwjgl;

import git.artdeell.substgen.util.GitHubQuery;
import git.artdeell.substgen.util.GitHubRelease;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class VersionInfo extends HashMap<String, VersionInfo.File> {
    public final String downloadUrl;

    protected VersionInfo(String downloadUrl, int fileCount) {
        super(fileCount);
        this.downloadUrl = downloadUrl;
    }

    public static class File {
        public final String sha1;
        public final long size;

        public File(String sha1, long size) {
            this.sha1 = sha1;
            this.size = size;
        }
    }

    public static VersionInfo load(String repo, String tag) throws IOException, URISyntaxException {
        String releaseFilesUrl = GitHubRelease.generateFileFormat(repo, tag);

        Map<String, Long> sizes = GitHubQuery.queryArtifactSizes(repo, tag);
        assert sizes.containsKey("hashes.sha1");
        Map<String, String> hashes = GitHubQuery.queryReleaseHashes(releaseFilesUrl);

        VersionInfo versionInfo = new VersionInfo(releaseFilesUrl, sizes.size());

        for(Map.Entry<String, Long> entries : sizes.entrySet()) {
            String fileName = entries.getKey();
            if(fileName.equals("hashes.sha1")) continue;
            String fileHash = hashes.get(fileName);
            long fileSize = entries.getValue();
            if(fileName == null || fileHash == null)
                throw new IllegalStateException("Missing hash for "+fileName);
            versionInfo.put(entries.getKey(), new VersionInfo.File(fileHash, fileSize));
        }
        return versionInfo;
    }
}
