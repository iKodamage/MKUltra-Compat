package com.chaosbuffalo.mkultrax;

import com.chaosbuffalo.mkultrax.init.MKXBlockRegistry;
import com.chaosbuffalo.mkultrax.init.MKXItemRegistry;
import com.chaosbuffalo.mkultrax.init.MKXSpawnRegistry;
import com.chaosbuffalo.mkultrax.init.MKXTileRegistry;
import com.chaosbuffalo.mkultrax.integrations.*;
import com.chaosbuffalo.mkultrax.utils.IntegrationUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Mod(modid = MKUltraX.MODID, name = MKUltraX.NAME, version = MKUltraX.VERSION,
        dependencies="required-after:mkultra@[0.97,);after:thebetweenlands;after:iceandfire;" +
                "required-after:targeting_api")
public class MKUltraX
{
    public static final String MODID = "mkultrax";
    public static final String NAME = "MK Ultra Compat";
    public static final String VERSION = "@VERSION@";

    public static IceAndFireIntegration iceAndFire;
    public static SpartanWeaponryIntegration spartanWeaponry;
    public static LootableBodiesIntegration lootableBodiesIntegration;

    public static final ArrayList<IIntegration> integrations = new ArrayList<>();

    static {
        if (IntegrationUtils.isIceAndFirePresent()){
            integrations.add(iceAndFire = new IceAndFireIntegration());
        }
        if (IntegrationUtils.isSpartanWeaponryPresent()){
            integrations.add(spartanWeaponry = new SpartanWeaponryIntegration());
        }
        if (IntegrationUtils.isLootableBodiesPresent()){
            integrations.add(lootableBodiesIntegration = new LootableBodiesIntegration());
        }
    }

    public static Logger LOG;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOG = event.getModLog();
        MKXItemRegistry.initItems();
        MKXBlockRegistry.initBlocks();
        MKXTileRegistry.registerTileEntities();
        MKXSpawnRegistry.initCustomSetters();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        for (IIntegration integration : integrations){
            if (integration.isLoaded()){
                integration.mod_init();
            }
        }
    }
}
