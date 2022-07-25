package net.smorb42.condensedmilk;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.smorb42.condensedmilk.block.ModBlocks;
import net.smorb42.condensedmilk.fluid.ModFluids;
import net.smorb42.condensedmilk.item.ModItems;
import net.smorb42.condensedmilk.network.ModMessages;
import org.slf4j.Logger;


@Mod(CondensedMilk.MOD_ID)
public class CondensedMilk
{
    public static final String MOD_ID = "condensedmilk";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public CondensedMilk()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);

        ModFluids.register(eventBus);
        //ModBlockEntities.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModFluids.CONDENSED_LAVA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.CONDENSED_LAVA_FLOW.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CONDENSED_LAVA.get(), RenderType.cutout());

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();

    }
}
