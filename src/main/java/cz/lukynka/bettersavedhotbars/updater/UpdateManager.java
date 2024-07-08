package cz.lukynka.bettersavedhotbars.updater;

import com.google.gson.Gson;
import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
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


    // Creates request to github API to get the latest release
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

                // If the release tag from github doesn't match the current mod version, set updateAvailable to true
//                if (!BetterSavedHotbars.version.equals(res.getTagName())) {
                if (true) {
                    BetterSavedHotbars.updateTag = res.getTagName();
                    BetterSavedHotbars.updateAvailable = true;
                    System.err.println("UPDATE DING DING DING");

                    var desc = res.getBody().split("\n");
                    var updateLine = desc[0];
                    var split = updateLine.split(Pattern.quote("_For Minecraft "));
                    if(split.length == 1) {
                        System.out.println("Version string not found!");
                        return;
                    }

                    var mcVersion = split[1].split(Pattern.quote("_"))[0];

                    var currentVersion = SharedConstants.getCurrentVersion().getName();
                    System.out.println(mcVersion);
                    System.out.println(currentVersion);
                    if(currentVersion.equals(mcVersion)) {
                        BetterSavedHotbars.isUpdatedForThisVersion = true;
                    }

                    var desc = res.getBody().split("\n");
                    var updateLine = desc[0];
                    var split = updateLine.split(Pattern.quote("_For Minecraft "));
                    if(split.length == 1) {
                        System.err.println("Version string not found!");
                        return;
                    }

                    var mcVersion = split[1].split(Pattern.quote("_"))[0];

                    var currentVersion = SharedConstants.getCurrentVersion().getName();
                    if(currentVersion.equals(mcVersion)) {
                        BetterSavedHotbars.isUpdatedForThisVersion = true;
                    }

                    // Compare tag from github and current version as numbers, if current version
                    // is bigger than one from github, we are running dev version
                    var versionNum = Integer.parseInt(BetterSavedHotbars.version.replace(".", ""));
                    var newVersionNum = Integer.parseInt(res.getTagName().replace(".", ""));

                    if(versionNum > newVersionNum) {
                        BetterSavedHotbars.isDevVersion = true;
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to get Update!");
        }
    }
}