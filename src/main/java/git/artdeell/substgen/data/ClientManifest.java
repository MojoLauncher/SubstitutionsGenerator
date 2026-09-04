package git.artdeell.substgen.data;

import java.util.HashMap;
import java.util.Map;

public class ClientManifest {
    public Library[] libraries;
    public static class Library {
        public String name;
        public MoJsonRule[] rules;
        public Map<String, String> natives;
        public Download downloads;
        public Object extract;
    }


    public static class SkippedLibrary extends Library {
        public boolean skip = true;
    }

    public static class Download {
        public Artifact artifact;
        public ClassifierMap classifiers;
    }

    public static class ClassifierMap extends HashMap<String, Artifact> {
        public ClassifierMap() {
            super();
        }

        public ClassifierMap(int count) {
            super(count);
        }
    }
}
