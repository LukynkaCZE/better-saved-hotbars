package cz.lukynka.bettersavedhotbars;

import cz.lukynka.bettersavedhotbars.protocol.ModInstalledPacket;
import cz.lukynka.bettersavedhotbars.updater.UpdateManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BetterSavedHotbars implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("bettersavedhotbars");
    public static String VERSION = "";
    public static String UPDATE_TAG = "";
    public static boolean IS_DEV_VERSION = false;
    public static UpdateManager UPDATE_MANAGER = new UpdateManager();
    public static boolean UPDATE_AVAILABLE = false;
    public static boolean IS_UPDATE_FOR_THIS_VERSION = false;
    public static float LAST_SCROLL_OFFSET = 0;

    @Override
    public void onInitialize() {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("bettersavedhotbars");
        container.ifPresent(modContainer -> VERSION = modContainer.getMetadata().getVersion().getFriendlyString());

        PayloadTypeRegistry.playC2S().register(ModInstalledPacket.TYPE, ModInstalledPacket.STREAM_CODEC);

        ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> {
            sender.sendPacket(new ModInstalledPacket(VERSION));
        });

        try {
            UPDATE_MANAGER.runUpdateCheck();
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }
}
