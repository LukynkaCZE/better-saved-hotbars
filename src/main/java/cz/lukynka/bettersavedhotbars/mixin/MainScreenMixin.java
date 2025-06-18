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
        if (!BetterSavedHotbars.UPDATE_AVAILABLE) return;

        var text = "";
        if(BetterSavedHotbars.IS_UPDATE_FOR_THIS_VERSION) {
            text = "&7&oBetter Saved Hotbars update available";
            fadeAfterTime = 10;
            fadeTime = 5;
        }

        startTime = System.currentTimeMillis();

        Component component = Component.literal(cz.lukynka.bettersavedhotbars.Util.translate(text));

        button = new PlainTextButton(
                (this.width / 2 - 100) + ((200 - this.font.width(component)) / 2),
                (this.height / 4 + 48 + 24 * 2) + 58,
                this.font.width(component),
                10,
                component,
                (clickable) -> Util.getPlatform().openUri("https://modrinth.com/mod/better-saved-hotbars/version/" +BetterSavedHotbars.UPDATE_TAG),
                this.font);

        this.addRenderableWidget(button);
    }

    @Override
    public void tick() {

        if(button == null) return;

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