package cz.lukynka.bettersavedhotbars.updater;

import com.google.gson.Gson;
import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import net.minecraft.SharedConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

// This is blatantly stolen from Island Utils
 // https://github.com/AsoDesu/IslandUtils/tree/main/src/main/java/net/asodev/islandutils/util/updater/
 // thanks aso :P

public class UpdateManager {

    HttpClient client;
    Gson gson;

    public UpdateManager() {
        client = HttpClient.newBuilder().build();
        gson = new Gson();
    }


    // Creates request to GitHub API to get the latest release
    private CompletableFuture<GithubRelease> checkForUpdates() throws Exception {
        CompletableFuture<GithubRelease> f = new CompletableFuture<>();

        URI updatorURI = new URI("https://api.github.com/repos/LukynkaCZE/better-saved-hotbars/releases/latest");
        HttpRequest req = HttpRequest.newBuilder(updatorURI).GET().build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            f.complete(gson.fromJson(res.body(), GithubRelease.class));
        });

        return f;
    }

    public void runUpdateCheck() {
        try {
            checkForUpdates().thenAccept(res -> {
                if (res == null) return;

                // If the release tag from GitHub doesn't match the current mod version, set updateAvailable to true
                if (!BetterSavedHotbars.VERSION.equals(res.getTagName())) {
                    BetterSavedHotbars.UPDATE_TAG = res.getTagName();
                    BetterSavedHotbars.UPDATE_AVAILABLE = true;
                    System.err.println("UPDATE DING DING DING");

                    var desc = res.getBody().split("\n");
                    var updateLine = desc[0];
                    var split = updateLine.split(Pattern.quote("_For Minecraft "));
                    if(split.length == 1) {
                        System.out.println("Version string not found!");
                        return;
                    }

                    var mcVersion = split[1].split(Pattern.quote("_"))[0];

                    var currentVersion = SharedConstants.getCurrentVersion().name();
                    System.out.println(mcVersion);
                    System.out.println(currentVersion);
                    if(currentVersion.equals(mcVersion)) {
                        BetterSavedHotbars.IS_UPDATE_FOR_THIS_VERSION = true;
                    }

                    // Compare tag from GitHub and current version as numbers, if the current version
                    // is bigger than one from GitHub, we are running a dev version
                    var versionNum = Integer.parseInt(BetterSavedHotbars.VERSION.replace(".", ""));
                    var newVersionNum = Integer.parseInt(res.getTagName().replace(".", ""));

                    if(versionNum > newVersionNum) {
                        BetterSavedHotbars.IS_DEV_VERSION = true;
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to get Update!");
        }
    }
}