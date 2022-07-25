package net.smorb42.condensedmilk.fluid;


import net.minecraft.world.level.material.*;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smorb42.condensedmilk.CondensedMilk;


public class ModFluids {


    public static final DeferredRegister<Fluid> FLUIDS
            = DeferredRegister.create(ForgeRegistries.FLUIDS, CondensedMilk.MOD_ID);

    public static RegistryObject<CondensedLavaFluid.Flowing> CONDENSED_LAVA_FLOW = FLUIDS.register("condensed_lava_flow", CondensedLavaFluid.Flowing::new);
    public static RegistryObject<CondensedLavaFluid.Source> CONDENSED_LAVA = FLUIDS.register("condensed_lava", CondensedLavaFluid.Source::new);





    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}