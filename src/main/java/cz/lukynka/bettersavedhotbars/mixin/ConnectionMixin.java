package cz.lukynka.bettersavedhotbars.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

@Mixin(Connection.class)
public class ConnectionMixin {

    private final File packetFolder = new File("C:\\Users\\LukynkaCZE\\Desktop\\dump\\registry-dump\\");

    public ConnectionMixin() {
        packetFolder.delete();
        packetFolder.mkdirs();
    }

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Nullable private volatile PacketListener packetListener;

    @Inject(at = @At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V")
    private void stuff(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) throws IOException {
        if (packet.getClass() == ClientboundRegistryDataPacket.class) {
            ClientboundRegistryDataPacket registry = (ClientboundRegistryDataPacket) packet;
            LOGGER.error("");
            LOGGER.error("");
            LOGGER.error("NEW REGISTRY PACKET {} {}", registry.registry().location(), registry.registry().registry());

            File file = Paths.get(packetFolder.getPath(), registry.registry().location().toString().split(":")[1].replace("/", "_") + ".yard").toFile();
            file.createNewFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                for (RegistrySynchronization.PackedRegistryEntry entry : registry.entries()) {
                    String data = entry.data().isPresent() ? entry.data().get().getAsString() : "";

                    LOGGER.error("{} - {}", entry.id(), data);
                    writer.write(entry.id() + ";" + data);
                    writer.newLine();
                }
            } catch (IOException e) {
                LOGGER.error("Failed to write to file: {}", file.getPath(), e);
                throw e;
            }
        }
    }
}