package cz.lukynka.bettersavedhotbars.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Inventory.class)
public interface InventoryAccessor {

    @Accessor("equipment")
    public EntityEquipment getEquipment();

    @Accessor("items")
    public NonNullList<ItemStack> getItems();
}
