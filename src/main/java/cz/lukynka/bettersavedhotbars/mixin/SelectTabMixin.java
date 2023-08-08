package cz.lukynka.bettersavedhotbars.mixin;

import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import cz.lukynka.bettersavedhotbars.util.ChatUtils;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.inventory.Hotbar;
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


@Mixin(net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class)
public abstract class SelectTabMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow protected abstract void selectTab(CreativeModeTab creativeModeTab);

    @Shadow private float scrollOffs;

    public SelectTabMixin(CreativeModeInventoryScreen.ItemPickerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(at = @At("TAIL"), method = "selectTab", cancellable = true)
    private void selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {
        if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR && selectedTab.getIconItem().getItem() == Items.BOOKSHELF) {
            HotbarManager hotbarManager = Minecraft.getInstance().getHotbarManager();
            (this.menu).items.clear();
            for (int i = 0; i < 9; ++i) {
                Hotbar hotbar = hotbarManager.get(i);
                if (hotbar.isEmpty()) {
                    for (int j = 0; j < 9; ++j) {
                        (this.menu).items.add(ItemStack.EMPTY);
                    }
                    continue;
                }
                this.menu.items.addAll(hotbar);
            }
            this.scrollOffs = BetterSavedHotbars.lastScrollOffset;
            this.menu.scrollTo(BetterSavedHotbars.lastScrollOffset);
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "mouseScrolled")
    private void mouseScrolled(double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        if (selectedTab.getIconItem().getItem() == Items.BOOKSHELF) {
            BetterSavedHotbars.lastScrollOffset = this.scrollOffs;
            this.selectTab(selectedTab);
        }
    }

    @Shadow private static CreativeModeTab selectedTab;
}
