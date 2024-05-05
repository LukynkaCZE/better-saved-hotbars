package cz.lukynka.bettersavedhotbars.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.Cipher;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V")
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        LOGGER.info(packet.getClass().getName());
    }

    @Inject(at = @At("HEAD"), method = "setEncryptionKey")
    private void setEncryptionKey(Cipher cipher, Cipher cipher2, CallbackInfo ci) {
        LOGGER.info("Set Encryption Key");
    }

}
