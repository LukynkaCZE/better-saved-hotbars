package cz.lukynka.bettersavedhotbars.mixin;

import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import net.minecraft.Util;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MainScreenMixin extends Screen {

    @Unique
    public int fadeTime = 0;

    @Unique
    public int fadeAfterTime = 0;

    @Unique
    public long startTime = Long.MAX_VALUE;

    @Unique
    public PlainTextButton button;

    @Unique
    public float opacity = 1f;

    @Unique
    public boolean isFading = false;

    @Unique
    public boolean hasFaded = false;

    protected MainScreenMixin(Component component) { super(component); }
    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (!BetterSavedHotbars.updateAvailable) return;

        var text = "";
        if(BetterSavedHotbars.isUpdatedForThisVersion) {
            text = "&2⚠ &aBetter Saved Hotbars Update Available! &n&lClick to Update!&a &2⚠";
            fadeAfterTime = 15;
            fadeTime = 5;
        } else {
            text = "&7&oBetter Saved Hotbars Update for different version available...";
            fadeAfterTime = 5;
            fadeTime = 2;
        }

        startTime = System.currentTimeMillis();

        if(BetterSavedHotbars.isDevVersion) text = "&4⚠ &cYou are running dev version of Better Saved Hotbars &4⚠";
        Component component = Component.literal(cz.lukynka.bettersavedhotbars.Util.translate(text));

        button = new PlainTextButton(
                (this.width / 2 - 100) + ((200 - this.font.width(component)) / 2),
                (this.height / 4 + 48 + 24 * 2) + 58,
                this.font.width(component),
                10,
                component,
                (clickable) -> Util.getPlatform().openUri("https://modrinth.com/mod/better-saved-hotbars/version/" +BetterSavedHotbars.updateTag),
                this.font);

        this.addRenderableWidget(button);
    }

    @Override
    public void tick() {

        if((System.currentTimeMillis() - startTime) >= fadeAfterTime * 1000L) {
            isFading = true;
        }

        if(hasFaded) {
            opacity = 0.001f;
            super.tick();
            button.visible = false;
            return;
        }

        if(isFading) {
            var step = 1.0f / (fadeTime / 0.05f);
            opacity -= step;

            if(opacity > 1f) {
                opacity = 1f;
            }

            if (opacity <= 0.015f) {
                opacity = 0.01f;
                isFading = false;
                hasFaded = true;
                button.visible = false;
            }
            button.setAlpha(opacity);
        } else {
            button.setAlpha(1f);
        }

        super.tick();
    }
}