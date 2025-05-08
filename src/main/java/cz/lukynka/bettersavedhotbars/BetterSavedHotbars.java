package cz.lukynka.bettersavedhotbars;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.InputConstants;
import cz.lukynka.bettersavedhotbars.updater.UpdateManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;
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

        ClientTickEvents.START_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft minecraft) {
        if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_L)) {
        }
    }
}
