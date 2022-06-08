package com.til.particle_system.client;

import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.client.cell.IParticleSystemSupport;
import com.til.particle_system.client.cell.ParticleSystemCell;
import com.til.particle_system.client.render.RenderTypeManage;
import com.til.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/***
 * 一个客户端的事件类
 * @author til
 */
@Mod.EventBusSubscriber(modid = MainParticleSystem.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ClientManage {

    public static Capability<IParticleSystemSupport> PARTICLE_SYSTEM_SUPPORT_CAPABILITY;


    //@SubscribeEvent
    public static void onEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().level.isClientSide && event.getEntity() instanceof LocalPlayer) {
            Optional<IParticleSystemSupport> optional = event.getEntity().getCapability(PARTICLE_SYSTEM_SUPPORT_CAPABILITY, null).resolve();
            optional.ifPresent(iParticleSystemSupport -> Minecraft.getInstance().particleEngine.add(new ParticleSystemCell(RenderTypeManage.textParticleSystem, iParticleSystemSupport)));
        }
    }

    @SubscribeEvent
    public static void onEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().level.isClientSide && event.getEntity() instanceof LocalPlayer) {
            Optional<IParticleSystemSupport> optional = event.getEntity().getCapability(PARTICLE_SYSTEM_SUPPORT_CAPABILITY, null).resolve();
            optional.ifPresent(iParticleSystemSupport -> Minecraft.getInstance().particleEngine.add(new ParticleSystemCell(RenderTypeManage.textParticleSystem, iParticleSystemSupport)));
        }
    }

    @SubscribeEvent
    public static void onEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().level.isClientSide) {
            Entity entity = event.getObject();
            IParticleSystemSupport iParticleSystemSupport = new IParticleSystemSupport() {
                @Override
                public V3 getPos() {
                    return new V3(entity.position());
                }

                @Override
                public V3 getOldPos() {
                    return new V3(entity.xOld, entity.yOld, entity.zOld);
                }

                @Override
                public Quaternion getRotate() {
                    return new Quaternion();
                }

                @Override
                public Quaternion getOldRotate() {
                    return new Quaternion();
                }

                @Override
                public V3 getSize() {
                    return new V3(1, 1, 1);
                }

                @Override
                public V3 getOldSize() {
                    return new V3(1, 1, 1);
                }

                @Override
                public ClientLevel getClient() {
                    return (ClientLevel) entity.level;
                }
            };
            LazyOptional<IParticleSystemSupport> lazyOptional = LazyOptional.of(() -> iParticleSystemSupport);
            event.addCapability(new ResourceLocation(MainParticleSystem.MOD_ID, "particle_system_support"), new ICapabilityProvider() {
                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap.equals(PARTICLE_SYSTEM_SUPPORT_CAPABILITY)) {
                        return Util.forcedConversion(lazyOptional);
                    }
                    return LazyOptional.empty();
                }
            });
        }
    }

    @Mod.EventBusSubscriber(modid = MainParticleSystem.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    @OnlyIn(Dist.CLIENT)
    public static class ClientManageInMod {

        @SubscribeEvent
        public static void onSetupEvent(RegisterCapabilitiesEvent event) {
            event.register(IParticleSystemSupport.class);
            PARTICLE_SYSTEM_SUPPORT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
            });
        }

    }

}
