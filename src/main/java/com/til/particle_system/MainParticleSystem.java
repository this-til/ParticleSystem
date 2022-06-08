package com.til.particle_system;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.til.channel_io.JsonBiConsumer;
import com.til.channel_io.JsonFunction;
import com.til.channel_io.JsonRun;
import com.til.json_read_write.JsonAnalysis;
import com.til.math.*;
import com.til.particle_system.client.render.RenderTypeManage;
import com.til.particle_system.element.ParticleSystem;
import com.til.util.UseString;
import com.til.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

/***
 * @author til
 */
@Mod(MainParticleSystem.MOD_ID)
public class MainParticleSystem {

    public static final String MOD_ID = "particle_system";
    public static final String MOD_NAME = "ParticleSystem";
    public static MainParticleSystem main;

    public final Logger logger;
    public final JsonAnalysis jsonAnalysis;
    public final String protocolVersion = "1";
    public final SimpleChannel channel;

    public MainParticleSystem() {
        main = this;
        this.logger = LogUtils.getLogger();
        this.jsonAnalysis = new JsonAnalysis();
        {

            try {
                jsonAnalysis.get(V3.class);
                jsonAnalysis.get(V2.class);
                jsonAnalysis.get(Colour.class);
                jsonAnalysis.get(IValue.IValueNumber.class);
                jsonAnalysis.get(IValue.IValurV2.class);
                jsonAnalysis.get(IValue.IValurV2.class);
                jsonAnalysis.get(IValue.IValurColour.class);
                jsonAnalysis.get(ITime.ITimeNumber.class);
                jsonAnalysis.get(ITime.ITimeV2.class);
                jsonAnalysis.get(ITime.ITimeV3.class);
                jsonAnalysis.get(ITime.ITimeColour.class);
            } catch (Exception e) {
                logger.error("", e);
            }

        }
        this.channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MOD_ID, UseString.MAIN),
                () -> protocolVersion,
                protocolVersion::equals,
                protocolVersion::equals
        );
        {
            channel.registerMessage(1, JsonElement.class, new JsonBiConsumer(), new JsonFunction(), new JsonRun());
        }
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
        {
            jsonAnalysis.readIO.add(MOD_ID, ParticleSystem.class);
        }

        try {
            RenderTypeManage.textParticleSystem = jsonAnalysis.as(ParticleSystem.class, RenderTypeManage.textString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Test {
    }
}
