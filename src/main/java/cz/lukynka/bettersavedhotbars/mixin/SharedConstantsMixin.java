package cz.lukynka.bettersavedhotbars.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {

    @Mutable
    @Shadow
    @Final
    public static boolean DEBUG_RENDER;

    @Inject(at = @At("HEAD"), method = "<clinit>")
    private static void init(CallbackInfo ci) {
        DEBUG_RENDER = true;
    }

}
