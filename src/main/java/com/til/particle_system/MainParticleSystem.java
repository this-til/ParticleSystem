package com.til.particle_system;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import com.til.json_read_write.JsonAnalysis;
import com.til.particle_system.element.ParticleSystem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.json.Json;

/***
 * @author til
 */
@Mod(MainParticleSystem.MOD_ID)
public class MainParticleSystem {

    public static final String MOD_ID = "particle_system";
    public static final String MOD_NAME = "ParticleSystem";
    public final Logger logger;
    public final JsonAnalysis jsonAnalysis;
    public final Gson gson;
    public static MainParticleSystem main;

    public MainParticleSystem() {
        main = this;
        this.gson = new Gson();
        this.logger = LogUtils.getLogger();
        this.jsonAnalysis = new JsonAnalysis();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
        {
            logger.debug("吃个狐狸！");
            logger.debug("rua个华乐！");
            logger.debug("来份志豪！");
        }

    }


    private void setup(final FMLCommonSetupEvent event) {
        jsonAnalysis.readIO.add(MOD_ID, ParticleSystem.class);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }
    }
}
