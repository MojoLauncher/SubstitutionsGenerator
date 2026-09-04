package git.artdeell.substgen.data;

import java.util.Objects;

public class SubstitutionTarget {
    public final boolean hasNative;
    public final String fullName;
    public final String provider;
    public final String module;
    public final String version;


    public SubstitutionTarget(ClientManifest.Library library) {
        hasNative = library.natives != null;
        fullName = library.name;
        String[] name_split = library.name.split(":");
        provider = name_split[0].trim();
        module = name_split[1].trim();
        version = name_split[2].trim();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubstitutionTarget that)) return false;
        return hasNative == that.hasNative && Objects.equals(that.fullName, fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasNative, fullName);
    }

    @Override
    public String toString() {
        return "{"+provider+" "+module + " " + version +" VN="+hasNative+"}";
    }
}
