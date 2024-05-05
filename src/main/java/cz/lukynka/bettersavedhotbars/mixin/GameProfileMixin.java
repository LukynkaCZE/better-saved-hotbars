package cz.lukynka.bettersavedhotbars.mixin;

import com.mojang.authlib.GameProfile;
import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundGameProfilePacket.class)
public class GameProfileMixin {

    @Shadow @Final private GameProfile gameProfile;

    @Inject(at = @At("HEAD"), method = "handle(Lnet/minecraft/network/protocol/login/ClientLoginPacketListener;)V")
    private void handle(ClientLoginPacketListener clientLoginPacketListener, CallbackInfo ci) {
        BetterSavedHotbars.LOGGER.info(gameProfile.toString());
    }

}
