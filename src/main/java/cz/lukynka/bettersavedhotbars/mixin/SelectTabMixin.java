package cz.lukynka.bettersavedhotbars.mixin;

import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class SelectTabMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    @Shadow private static CreativeModeTab selectedTab;
    @Shadow private float scrollOffs;

    public SelectTabMixin(CreativeModeInventoryScreen.ItemPickerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Shadow
    protected abstract void selectTab(CreativeModeTab creativeModeTab);

    @Inject(at = @At("TAIL"), method = "selectTab", cancellable = true)
    private void selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {
        if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR && selectedTab.getIconItem().getItem() == Items.BOOKSHELF) {
            HotbarManager hotbarManager = Minecraft.getInstance().getHotbarManager();
            RegistryAccess registryAccess = Minecraft.getInstance().player.level().registryAccess();
            (this.menu).items.clear();
            for (int i = 0; i < 9; ++i) {
                Hotbar hotbar = hotbarManager.get(i);
                if (hotbar.isEmpty()) {
                    for (int j = 0; j < 9; ++j) {
                        (this.menu).items.add(ItemStack.EMPTY);
                    }
                    continue;
                }
                this.menu.items.addAll(hotbar.load(registryAccess));
            }
            this.scrollOffs = BetterSavedHotbars.lastScrollOffset;
            this.menu.scrollTo(BetterSavedHotbars.lastScrollOffset);
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "mouseScrolled")
    private void mouseScrolled(double d, double e, double f, double g, CallbackInfoReturnable<Boolean> cir) {
        if (selectedTab.getIconItem().getItem() == Items.BOOKSHELF) {
            BetterSavedHotbars.lastScrollOffset = this.scrollOffs;
            this.selectTab(selectedTab);
        }
    }
}

