package git.artdeell.substgen.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class GitHubQuery {
    public static Map<String, String> queryReleaseHashes(String releaseBaseUrl) throws IOException, URISyntaxException {
        String hashList = Downloader.urlToString(new URI(GitHubRelease.getFileUrl(releaseBaseUrl, "hashes.sha1")).toURL());
        String[] hashes = hashList.split("\\s+");
        int hashCount = hashes.length / 2;
        HashMap<String, String> hashMap = new HashMap<>(hashCount);
        for(int i = 0; i < hashCount; i++) {
            String hash = hashes[i * 2];
            String file = hashes[i * 2 + 1];
            hashMap.put(file.trim(), hash.trim());
        }
        return hashMap;
    }

    public static Map<String, Long> queryArtifactSizes(String repository, String tag) throws IOException, URISyntaxException {
        GitHubRelease releaseInfo = JSONParser.jsonFromUrl(GitHubRelease.class,
                new URI("https://api.github.com/repos/"+repository+"/releases/tags/"+tag).toURL()
        );
        HashMap<String, Long> fileSizeMap = new HashMap<>(releaseInfo.assets.length);
        for(GitHubRelease.Info asset : releaseInfo.assets)  {
            fileSizeMap.put(asset.name, asset.size);
        }
        return fileSizeMap;
    }
}
