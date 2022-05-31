package com.til.particle_system;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.logging.LogUtils;
import com.til.particle_system.element.*;
import com.til.particle_system.event.EventMod;
import com.til.particle_system.register.Analysis;
import com.til.particle_system.register.IFromJsonElement;
import com.til.particle_system.register.RegisterManage;
import com.til.particle_system.util.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
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

/***
 * @author til
 */
@Mod(MainParticleSystem.MOD_ID)
public class MainParticleSystem {

    public static final String MOD_ID = "particle_system";
    public static final String MOD_NAME = "ParticleSystem";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MainParticleSystem main;

    public final RegisterManage registerManage = new RegisterManage();

    public MainParticleSystem() {
        main = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
    }


    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("开始注册标签解析类型");
        MinecraftForge.EVENT_BUS.start();

        {
            Analysis<Number> analysis = registerManage.get(Number.class);
            analysis.put(new IFromJsonElement<>() {
                @Override
                public JsonElement from(Number number) {
                    return new JsonPrimitive(number);
                }

                @Override
                public Number as(JsonElement jsonElement) {
                    return jsonElement.getAsNumber();
                }

                @Override
                public Class<Number> getType() {
                    return Number.class;
                }

                @Override
                public String getTypeName() {
                    return UseString.DEFAULT;
                }
            });
        }
        {
            Analysis<V2> analysis = registerManage.get(V2.class);
            analysis.put(new IFromJsonElement<>() {
                @Override
                public JsonElement from(V2 vec2) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add(UseString.X, new JsonPrimitive(vec2.x));
                    jsonObject.add(UseString.Y, new JsonPrimitive(vec2.y));
                    return jsonObject;
                }

                @Override
                public V2 as(JsonElement jsonElement) {
                    return new V2(jsonElement.getAsJsonObject().get(UseString.X).getAsDouble(),
                            jsonElement.getAsJsonObject().get(UseString.Y).getAsDouble());
                }

                @Override
                public Class<V2> getType() {
                    return V2.class;
                }

                @Override
                public String getTypeName() {
                    return UseString.DEFAULT;
                }
            });
        }
        {
            Analysis<V3> analysis = registerManage.get(V3.class);
            analysis.put(new IFromJsonElement<>() {
                @Override
                public JsonElement from(V3 vec2) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add(UseString.X, new JsonPrimitive(vec2.x));
                    jsonObject.add(UseString.Y, new JsonPrimitive(vec2.y));
                    jsonObject.add(UseString.Z, new JsonPrimitive(vec2.z));
                    return jsonObject;
                }

                @Override
                public V3 as(JsonElement jsonElement) {
                    return new V3(jsonElement.getAsJsonObject().get(UseString.X).getAsDouble(),
                            jsonElement.getAsJsonObject().get(UseString.Y).getAsDouble(),
                            jsonElement.getAsJsonObject().get(UseString.Z).getAsDouble());
                }

                @Override
                public Class<V3> getType() {
                    return V3.class;
                }

                @Override
                public String getTypeName() {
                    return UseString.DEFAULT;
                }
            });
        }
        {
            Analysis<IValue.IValueNumber> analysis = registerManage.get(IValue.IValueNumber.class);
            analysis.put(new IFromJsonElement<IValue.IValueNumber.NumberFinal>() {
                @Override
                public JsonElement from(IValue.IValueNumber.NumberFinal numberFinal) {
                    return new JsonPrimitive(numberFinal.value);
                }

                @Override
                public IValue.IValueNumber.NumberFinal as(JsonElement jsonElement) {
                    IValue.IValueNumber.NumberFinal numberFinal = new IValue.IValueNumber.NumberFinal();
                    numberFinal.value = jsonElement.getAsNumber();
                    return numberFinal;
                }

                @Override
                public Class<IValue.IValueNumber.NumberFinal> getType() {
                    return IValue.IValueNumber.NumberFinal.class;
                }

                @Override
                public String getTypeName() {
                    return UseString.DEFAULT;
                }
            });
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(IValue.IValueNumber.NumberRandom.class, UseString.RANDOM));
        }
        {
            Analysis<Boolean> analysis = registerManage.get(Boolean.class);
            analysis.put(new IFromJsonElement<>() {
                @Override
                public JsonElement from(Boolean aBoolean) {
                    return new JsonPrimitive(aBoolean);
                }

                @Override
                public Boolean as(JsonElement jsonElement) {
                    return jsonElement.getAsBoolean();
                }

                @Override
                public Class<Boolean> getType() {
                    return Boolean.class;
                }

                @Override
                public String getTypeName() {
                    return UseString.DEFAULT;
                }
            });
        }
        {
            Analysis<ITime.ITimeNumber> analysis = registerManage.get(ITime.ITimeNumber.class);
        }
        {
            Analysis<ITime.ITimeV3> analysis = registerManage.get(ITime.ITimeV3.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(ITime.ITimeV3.DefaultTimeV3.class));
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(ITime.ITimeV3.NoSeparationTimeV3.class, UseString.NO_SEPARATION));
        }
        {
            Analysis<ITime.ITimeColour> analysis = registerManage.get(ITime.ITimeColour.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(ITime.ITimeColour.DefaultTimeColour.class));
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(ITime.ITimeColour.NoSeparationTimeColour.class, UseString.NO_SEPARATION));
        }
        {
            Analysis<ITime.ITimeV2> analysis = registerManage.get(ITime.ITimeV2.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(ITime.ITimeV2.DefaultTimeV2.class));
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(ITime.ITimeV2.NoSeparationTimeV2.class, UseString.NO_SEPARATION));
        }
        {
            Analysis<MainElement> analysis = registerManage.get(MainElement.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(MainElement.class, UseString.DEFAULT));
            {
                Analysis<MainElement.ParticleBufferMode> particleBufferModeAnalysis = registerManage.get(MainElement.ParticleBufferMode.class);
                particleBufferModeAnalysis.put(new IFromJsonElement.EnumFromJsonElement<>(MainElement.ParticleBufferMode.class));
            }
            {
                Analysis<MainElement.ParticleCoordinate> particleCoordinateAnalysis = registerManage.get(MainElement.ParticleCoordinate.class);
                particleCoordinateAnalysis.put(new IFromJsonElement.EnumFromJsonElement<>(MainElement.ParticleCoordinate.class));
            }
        }
        {
            Analysis<LaunchElement> analysis = registerManage.get(LaunchElement.class);
            {
                analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LaunchElement.class));
                Analysis<LaunchElement.LaunchBurst> launchBurstAnalysis = registerManage.get(LaunchElement.LaunchBurst.class);
                launchBurstAnalysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LaunchElement.LaunchBurst.class));
                {
                    Analysis<LaunchElement.LaunchBurst.LaunchBurstList> launchBurstLis = registerManage.get(LaunchElement.LaunchBurst.LaunchBurstList.class);
                    launchBurstLis.put(new IFromJsonElement.ListFromJsonElement<>(LaunchElement.LaunchBurst.LaunchBurstList.class, LaunchElement.LaunchBurst.class));
                }
            }
        }
        {
            Analysis<LifeTimeColourElement> analysis = registerManage.get(LifeTimeColourElement.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LifeTimeColourElement.class));
        }
        {
            Analysis<LifeTimeSpeedColourElement> analysis = registerManage.get(LifeTimeSpeedColourElement.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LifeTimeSpeedColourElement.class));
        }
        {
            Analysis<LifeTimeSpeedElement> analysis = registerManage.get(LifeTimeSpeedElement.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LifeTimeSpeedElement.class));
        }
        {
            Analysis<LifeTimeSizeElement> analysis = registerManage.get(LifeTimeSizeElement.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LifeTimeSizeElement.class));
        }
        {
            Analysis<LifeTimeSpeedSizeElement> analysis = registerManage.get(LifeTimeSpeedSizeElement.class);
            analysis.put(new IFromJsonElement.CurrencyFromJsonElement<>(LifeTimeSpeedSizeElement.class));
        }

        MinecraftForge.EVENT_BUS.post(new EventMod.Init.Register.Type());

        MinecraftForge.EVENT_BUS.post(new EventMod.Init.Register.Field());
        LOGGER.info("开始注册标签解析元素");

        {

        }

        MinecraftForge.EVENT_BUS.post(new EventMod.Init.Register.Element());
        //LOGGER.info("开始扫描XML文档");
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    //@Mod.EventBusSubscriber(modid = MainParticleSystem.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
