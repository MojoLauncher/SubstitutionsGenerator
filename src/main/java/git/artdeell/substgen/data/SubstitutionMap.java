package git.artdeell.substgen.data;

import java.util.Map;

public class SubstitutionMap {
    public final Map<String, ClientManifest.Library> libraries;
    public final Map<String, String> artifactMapping;

    public SubstitutionMap(Map<String, ClientManifest.Library> libraries, Map<String, String> artifactMapping) {
        this.libraries = libraries;
        this.artifactMapping = artifactMapping;
    }
}
