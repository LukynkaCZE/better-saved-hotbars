package cz.lukynka.bettersavedhotbars.mixin;

import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import net.minecraft.Util;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(TitleScreen.class)
public abstract class MainScreenMixin extends Screen {
    protected MainScreenMixin(Component component) { super(component); }
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (!BetterSavedHotbars.updateAvailable) return;

        var text = "&4⚠ &cBetter Saved Hotbars Update Available! &n&lClick to Update!&c &4⚠";
        if(BetterSavedHotbars.isDevVersion) text = "&4⚠ &cYou are running dev version of Better Saved Hotbars &4⚠";
        Component component = Component.literal(cz.lukynka.bettersavedhotbars.Util.translate(text));

        this.addRenderableWidget(new PlainTextButton(
                (this.width / 2 - 100) + ((200 - this.font.width(component)) / 2),
                (this.height / 4 + 48 + 24 * 2) + 58,
                this.font.width(component),
                10,
                component,
                (button) -> Util.getPlatform().openUri("https://modrinth.com/mod/better-saved-hotbars/version/" +BetterSavedHotbars.updateTag),
                this.font));
    }
}
