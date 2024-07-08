package cz.lukynka.bettersavedhotbars;

import cz.lukynka.bettersavedhotbars.updater.UpdateManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BetterSavedHotbars implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("bettersavedhotbars");

    public static String version = "";

    public static String updateTag = "";

    public static boolean isDevVersion = false;

    public static UpdateManager updateManager = new UpdateManager();

    public static boolean updateAvailable = false;

    public static boolean isUpdatedForThisVersion = false;

    public static float lastScrollOffset = 0;

    public static boolean isUpdatedForThisVersion = false;

    @Override
    public void onInitialize() {
        LOGGER.info("Hi there");

        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("bettersavedhotbars");
        container.ifPresent(modContainer -> version = modContainer.getMetadata().getVersion().getFriendlyString());

        try {
            updateManager.runUpdateCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
