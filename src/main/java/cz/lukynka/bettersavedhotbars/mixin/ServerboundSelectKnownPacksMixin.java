package cz.lukynka.bettersavedhotbars.mixin;

import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;
import net.minecraft.server.packs.repository.KnownPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerboundSelectKnownPacks.class)
public class ServerboundSelectKnownPacksMixin {

    @Inject(at = @At("HEAD"), method = "knownPacks", cancellable = true)
    private void aa(CallbackInfoReturnable<List<KnownPack>> cir) {
        cir.setReturnValue(List.of());
    }

}
