package git.artdeell.substgen.lwjgl;

import git.artdeell.substgen.data.ClientManifest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public class LWJGL2LibraryGenerator extends LWJGLLibraryGenerator {
    private static final ClassifierDescription[] DESCRIPTIONS = new ClassifierDescription[] {
            new ClassifierDescription("android-arm64", "natives-an-arm64", "native-arm64-v8a"),
            new ClassifierDescription("android-arm",   "natives-an-arm32", "native-armeabi-v7a"),
            new ClassifierDescription("android-x86_64", "natives-an-x86_64", "native-x86"),
            new ClassifierDescription("android-x86",  "natives-an-x86", "native-x86_64")
    };

    public LWJGL2LibraryGenerator(String version, String repo, String tag) throws IOException, URISyntaxException {
        super(DESCRIPTIONS, version, repo, tag);
    }

    public List<ClientManifest.Library> generateLibraries() {
        return generate(Set.of(
                "lwjgl",
                "lwjgl_util",
                "lwjgl-platform"
        ));
    }

    @Override
    protected String getModuleFileName(String moduleName, String nativesName) {
        if(nativesName == null) return moduleName+".jar";
        else if(moduleName.equals("lwjgl-platform")) return "lwjgl-"+nativesName+".jar";
        else return "missingno.jar";
    }

    @Override
    protected String getLibraryName(String module) {
        return "org.lwjgl.lwjgl:"+module+":"+version;
    }

    @Override
    protected void insertNativeSettings(ClientManifest.Library library) {
        library.extract = new Extract();
    }

    public static class Extract {
        public List<String> exclude = List.of("META-INF/");
    }
}
