package git.artdeell.substgen;

import git.artdeell.substgen.data.*;
import git.artdeell.substgen.lwjgl.LWJGL2LibraryGenerator;
import git.artdeell.substgen.lwjgl.LWJGL3LibraryGenerator;
import git.artdeell.substgen.util.Downloader;
import git.artdeell.substgen.util.JSONParser;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    private static final File versionsDir = new File("./allversions/");

    private static final Map<String, ClientManifest.Library> substitutions = new HashMap<>();
    private static final Map<String, String> versionReplacements = new HashMap<>();
    private static final Map<String, Set<String>> lwjgl3WantedModules = new HashMap<>();

    private static final String lwjgl2Version = "2.9.4-mojo";
    private static final String repoLwjgl2 = "MojoLauncher/lwjgl2-glfw";
    private static final String tagLwjgl2 = "v7m";

    private static final String tagFormat = "v%s-r6";
    private static final String repo = "MojoLauncher/unilwjgl3-builder";

    private static final String[] legacyLwjgl3Versions = new String[] {
            "3.1.2",
            "3.1.6",
            "3.2.1",
            "3.2.2"
    };

    private static final String oldestLwjgl3Version = "3.2.3";

    public static void main(String[] args) throws Throwable {
        if(!versionsDir.exists() && !versionsDir.mkdirs()) throw new IOException("Failed to mkdirs");

        System.out.println("Downloading version manifest...");
        VersionManifest versionManifest = JSONParser.jsonFromUrl(VersionManifest.class, new URI("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json").toURL());

        System.out.println("Preparing versions...");
        for(int i = 0; i < versionManifest.versions.length; i++) {
            VersionManifest.Download download = versionManifest.versions[i];

            ClientManifest clientManifest = getFor(download);
            for(ClientManifest.Library library : clientManifest.libraries) {
                String libraryName = library.name;
                if(!(libraryName.startsWith("org.lwjgl") || libraryName.contains("jinput-platform"))) continue;
                if(library.rules != null && !(MoJsonRule.ruleSetCheck(library.rules).equals("allow"))) continue;
                SubstitutionTarget target = new SubstitutionTarget(library);
                processSubstitution(target);
            }
            System.out.println(i+"/"+versionManifest.versions.length);
        }



        addLwjglModules();

        JSONParser.jsonToFile(new SubstitutionMap(substitutions, versionReplacements), new File("substitutions.json"));
    }

    private static void processSubstitution(SubstitutionTarget target) {
        if(target.provider.equals("org.lwjgl")) {
            String version = target.version;
            if(isLegacyLwjgl3(version)) {
                versionReplacements.put(target.fullName, target.fullName.replace(target.version, oldestLwjgl3Version));
                version = oldestLwjgl3Version;
            }
            if(target.fullName.contains(":natives-")) {
                disableLibrary(target);
            } else {
                addLwjgl3Module(target.module, version);
            }
        } else if(target.provider.equals("org.lwjgl.lwjgl")) {
            versionReplacements.put(target.fullName, target.fullName.replace(target.version, lwjgl2Version));
        }
    }

    private static void addLwjgl3Module(String module, String version) {
        lwjgl3WantedModules.computeIfAbsent(version, k -> new HashSet<>()).add(module);
    }

    private static void disableLibrary(SubstitutionTarget target) {
        System.out.println("Disable: "+target.fullName);
        substitutions.put(target.fullName, new ClientManifest.SkippedLibrary());
    }

    private static boolean isLegacyLwjgl3(String version) {
        for(String legacy : legacyLwjgl3Versions) {
            if(legacy.equals(version)) return true;
        }
        return false;
    }

    private static void addLwjglModules() throws IOException, URISyntaxException {
        LWJGL2LibraryGenerator generator2 = new LWJGL2LibraryGenerator(lwjgl2Version, repoLwjgl2, tagLwjgl2);
        for(ClientManifest.Library library : generator2.generateLibraries()) {
            System.out.println("Added module: "+library.name);
            substitutions.put(library.name, library);
        }

        for(Map.Entry<String, Set<String>> version : lwjgl3WantedModules.entrySet()) {
            LWJGL3LibraryGenerator generator3 = new LWJGL3LibraryGenerator(version.getKey(), repo, tagFormat);
            for(ClientManifest.Library library : generator3.generateModules(version.getValue())) {
                System.out.println("Added module: "+library.name);
                substitutions.put(library.name, library);
            }
        }
    }

    private static ClientManifest getFor(VersionManifest.Download download) throws IOException, URISyntaxException {
        File release = new File(versionsDir, download.id);
        if(!release.exists()) {
            Downloader.urlToFile(release, new URI(download.url).toURL());
        }
        return JSONParser.jsonFromFile(ClientManifest.class, release);
    }

}
