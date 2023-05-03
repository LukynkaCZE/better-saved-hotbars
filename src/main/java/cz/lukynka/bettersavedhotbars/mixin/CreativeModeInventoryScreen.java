package cz.lukynka.bettersavedhotbars.mixin;

import cz.lukynka.bettersavedhotbars.HotbarInfo;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreen {
    @Shadow private static CreativeModeTab selectedTab;

    @Inject(at = @At("HEAD"), method = "slotClicked", cancellable = true)
    private void slotClicked(Slot slot, int i, int j, ClickType clickType, CallbackInfo ci) {
        if(selectedTab.getType() != CreativeModeTab.Type.HOTBAR) return;

        if(slot == null) return;

        HotbarManager hotbarManager = Minecraft.getInstance().getHotbarManager();
        Player player = Minecraft.getInstance().player;
        assert player != null;
        ItemStack item = player.inventoryMenu.getCarried().copy();

        if(i >= 45) return;
        HotbarInfo newHotbarInfo = getHotbarWithIndex(slot);

        if(item.getItem() == Items.AIR) {
            if(clickType == ClickType.CLONE) {
                ItemStack slotItem = slot.getItem();
                slot.set(new ItemStack(Items.AIR, 0));
                hotbarManager.get(newHotbarInfo.getRow()).set(newHotbarInfo.getSlot(), ItemStack.EMPTY);
                hotbarManager.save();
                Minecraft.getInstance().player.inventoryMenu.setCarried(slotItem);
                ci.cancel();
            }
            return;
        }
        slot.set(item);
        hotbarManager.get(newHotbarInfo.getRow()).set(newHotbarInfo.getSlot(), item);
        hotbarManager.save();
        Minecraft.getInstance().player.inventoryMenu.setCarried(ItemStack.EMPTY);
        ci.cancel();
    }

    private HotbarInfo getHotbarWithIndex(Slot slot) {
        Integer rHotbar = slot.getContainerSlot() / 9;
        Integer rSlot = ((slot.x - 9) / 9) / 2;
        return new HotbarInfo(rSlot, rHotbar);
    }
}