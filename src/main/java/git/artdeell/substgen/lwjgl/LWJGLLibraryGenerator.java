package git.artdeell.substgen.lwjgl;

import git.artdeell.substgen.data.Artifact;
import git.artdeell.substgen.data.ClientManifest;
import git.artdeell.substgen.util.GitHubRelease;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public abstract class LWJGLLibraryGenerator {
    private final ClassifierDescription[] classifierDescriptions;
    private final VersionInfo versionInfo;
    protected final String version;

    /**
     * @param repository the repo name in owner/name format
     * @param tag the tag, formatted with version in 1st argument
     */
    public LWJGLLibraryGenerator(ClassifierDescription[] descriptions, String version, String repository, String tag) throws IOException, URISyntaxException {
        this.classifierDescriptions = descriptions;
        this.version = version;
        tag = String.format(Locale.US, tag, version);
        this.versionInfo = VersionInfo.load(repository, tag);
    }

    private ClientManifest.Library generateLibrary(String module) {
        ClientManifest.Library library = new ClientManifest.Library();
        library.name = getLibraryName(module);
        library.downloads = new ClientManifest.Download();
        library.downloads.artifact = generateArtifact(module, null);

        ClientManifest.ClassifierMap classMap = new ClientManifest.ClassifierMap(classifierDescriptions.length);
        HashMap<String, String> nativeMap = new HashMap<>(classifierDescriptions.length);
        for(ClassifierDescription description : classifierDescriptions) {
            Artifact classArtifact = generateArtifact(module, description.nativeName());
            if(classArtifact == null) continue;
            String className = description.classifierName();
            classMap.put(className, classArtifact);
            nativeMap.put(description.platform(), className);
        }

        if(classMap.isEmpty()) return library;

        library.downloads.classifiers = classMap;
        library.natives = nativeMap;

        insertNativeSettings(library);

        return library;
    }


    private Artifact generateArtifact(String module, String natives) {
        String modulefile = getModuleFileName(module, natives);

        VersionInfo.File file = versionInfo.get(modulefile);
        if(file == null) {
            return null;
        }

        String downloadUrl = GitHubRelease.getFileUrl(versionInfo.downloadUrl, modulefile);
        String path = "org/lwjgl/"+module+"/"+ version +"/"+modulefile;

        return new Artifact(path, file.sha1, file.size, downloadUrl);
    }

    protected List<ClientManifest.Library> generate(Set<String> modules) {
        ArrayList<ClientManifest.Library> libraries = new ArrayList<>(modules.size());
        for(String module : modules) {
            libraries.add(generateLibrary(module));
        }
        return libraries;
    }

    protected abstract String getModuleFileName(String moduleName, String nativesName);
    protected abstract String getLibraryName(String module);
    protected abstract void insertNativeSettings(ClientManifest.Library library);
}
