package cz.lukynka.bettersavedhotbars.mixin;

import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class)
public abstract class SelectTabMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    public SelectTabMixin(CreativeModeInventoryScreen.ItemPickerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(at = @At("TAIL"), method = "selectTab", cancellable = true)
    private void selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {

        if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR) {
            HotbarManager hotbarManager = Minecraft.getInstance().getHotbarManager();
            (this.menu).items.clear();
            for (int i = 0; i < 5; ++i) {
                Hotbar hotbar = hotbarManager.get(i);
                if (hotbar.isEmpty()) {
                    for (int j = 0; j < 9; ++j) {
                        (this.menu).items.add(ItemStack.EMPTY);
                    }
                    continue;
                }
                this.menu.items.addAll(hotbar);
            }
            this.menu.scrollTo(0.0f);
            ci.cancel();
        }
    }

    @Shadow private static CreativeModeTab selectedTab;
}
