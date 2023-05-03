package cz.lukynka.bettersavedhotbars.util;


import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ChatUtils {

    //I stole this from island utils ok
    public static final String prefix = "&8[&bBetterSavedHotbars&8]";
    public static String translate(String s) {
        return s.replaceAll("&", "§");
    }

    public static void send(String s) {
        send(Component.literal(translate(prefix + " " + s)));
    }

    public static void debug(String s, Object... args) {
        debug(String.format(s, args));
    }

    public static void debug(String s) {
        send(Component.literal(translate("&7[DEBUG] " + s)));
    }

    public static void debug(Component component) {
        send(Component.literal(translate("&7[DEBUG] ")).append(component));
    }

    public static void send(Component component) {
        Minecraft.getInstance().getChatListener().handleSystemMessage(component, false);
    }

}
