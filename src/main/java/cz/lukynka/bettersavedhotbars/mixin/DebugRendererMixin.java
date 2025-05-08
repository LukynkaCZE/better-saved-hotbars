package cz.lukynka.bettersavedhotbars.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

    @Shadow
    @Final
    public DebugRenderer.SimpleDebugRenderer heightMapRenderer;

    @Inject(at = @At("HEAD"), method = "render")
    public void render(PoseStack poseStack, Frustum frustum, MultiBufferSource.BufferSource bufferSource, double d, double e, double f, CallbackInfo ci) {
        heightMapRenderer.render(poseStack, bufferSource, d, e, f);
    }
}
