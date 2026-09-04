package git.artdeell.substgen.lwjgl;

import git.artdeell.substgen.data.ClientManifest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public class LWJGL3LibraryGenerator extends LWJGLLibraryGenerator {
    private static final ClassifierDescription[] DESCRIPTIONS = new ClassifierDescription[] {
            new ClassifierDescription("android-arm64", "natives-an-arm64", "natives-linux-arm64"),
            new ClassifierDescription("android-arm",   "natives-an-arm32", "natives-linux-arm32"),
            new ClassifierDescription("android-x86_64", "natives-an-x86_64", "natives-linux"),
            new ClassifierDescription("android-x86",  "natives-an-x86", "natives-linux-x86")
    };
    /**
     * @param repository the repo name in owner/name format
     * @param tag the tag, formatted with version in 1st argument
     */
    public LWJGL3LibraryGenerator(String version, String repository, String tag) throws IOException, URISyntaxException {
        super(DESCRIPTIONS, version, repository, tag);
    }

    public List<ClientManifest.Library> generateModules(Set<String> modules) {
        return generate(modules);
    }

    @Override
    protected String getModuleFileName(String module, String natives) {
        if(natives != null) return module + "-" + natives + "-" + version + ".jar";
        else return module + "-" + version + ".jar";
    }

    @Override
    protected String getLibraryName(String module) {
        return "org.lwjgl:"+module+":"+version;
    }

    @Override
    protected void insertNativeSettings(ClientManifest.Library library) {

    }
}
