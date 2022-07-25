package net.smorb42.condensedmilk.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import java.util.List;
import java.util.function.Supplier;

public class CondensedLavaBucket extends BucketItem {
    public CondensedLavaBucket(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("Put near lava or water.").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
