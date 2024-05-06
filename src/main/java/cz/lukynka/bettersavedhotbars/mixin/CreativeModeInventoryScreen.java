package cz.lukynka.bettersavedhotbars.mixin;

import cz.lukynka.bettersavedhotbars.BetterSavedHotbars;
import cz.lukynka.bettersavedhotbars.HotbarInfo;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreen {
    @Shadow private static CreativeModeTab selectedTab;
    @Shadow private float scrollOffs;

    @Shadow protected abstract void selectTab(CreativeModeTab creativeModeTab);

    @Inject(at = @At("HEAD"), method = "slotClicked", cancellable = true)
    private void slotClicked(@Nullable Slot slot, int i, int j, ClickType clickType, CallbackInfo ci) {
        if (selectedTab.getType() != CreativeModeTab.Type.HOTBAR) return;

        if (slot == null) return;

        HotbarManager hotbarManager = Minecraft.getInstance().getHotbarManager();
        Player player = Minecraft.getInstance().player;
        assert player != null;
        ItemStack item = player.inventoryMenu.getCarried().copy();
        RegistryAccess registryAccess = player.level().registryAccess();

        if (i >= 45) return;
        HotbarInfo newHotbarInfo = getHotbarWithIndex(slot);

        if (item.getItem() == Items.AIR) {
            if (clickType == ClickType.CLONE) {
                ItemStack slotItem = slot.getItem();
                slot.set(new ItemStack(Items.AIR, 0));
                var hotbar = hotbarManager.get(newHotbarInfo.row());
                hotbar.storeFrom(fakeInventoryWithModifiedHotbar(hotbar.load(registryAccess), newHotbarInfo.slot(), item), registryAccess);
                hotbarManager.save();
                Minecraft.getInstance().player.inventoryMenu.setCarried(slotItem);
                ci.cancel();
            }
            return;
        }
        slot.set(item);
        var hotbar = hotbarManager.get(newHotbarInfo.row());
        hotbar.storeFrom(fakeInventoryWithModifiedHotbar(hotbar.load(registryAccess), newHotbarInfo.slot(), item), registryAccess);

        hotbarManager.save();
        Minecraft.getInstance().player.inventoryMenu.setCarried(ItemStack.EMPTY);
        BetterSavedHotbars.lastScrollOffset = scrollOffs;
        this.selectTab(selectedTab);
        ci.cancel();
    }


    @Unique
    private Inventory fakeInventoryWithModifiedHotbar(List<ItemStack> existingItems, Integer slot, ItemStack itemStack) {
        assert Minecraft.getInstance().player != null;
        var player = Minecraft.getInstance().player;
        var fakeInventory = new Inventory(player);
        var i = 0;
        fakeInventory.items.clear();
        for(ItemStack item : existingItems) {
            fakeInventory.setItem(i, existingItems.get(i));
            i++;
        }
        fakeInventory.setItem(slot, itemStack);

        return fakeInventory;
    }

    @Unique
    private HotbarInfo getHotbarWithIndex(Slot slot) {
        int scrollPage = Math.round(4 * scrollOffs);

        int slotRow = (slot.getContainerSlot() / 9) + scrollPage;
        int slotNumber = (((slot.x - 9) / 9) / 2);

        assert Minecraft.getInstance().player != null;
        return new HotbarInfo(slotNumber, slotRow);
    }
}