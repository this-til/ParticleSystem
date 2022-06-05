package com.til.channel_io;

import com.google.gson.JsonElement;
import com.til.channel_io.run.JsonRunBasics;
import com.til.particle_system.MainParticleSystem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class JsonRun implements BiConsumer<JsonElement, Supplier<NetworkEvent.Context>> {
    @Override
    public void accept(JsonElement jsonElement, Supplier<NetworkEvent.Context> contextSupplier) {
        JsonRunBasics jsonRunBasics = null;
        try {
            jsonRunBasics = MainParticleSystem.main.jsonAnalysis.as(JsonRunBasics.class, jsonElement);
            jsonRunBasics.supplier = contextSupplier;
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRunBasics finalJsonRunBasics = jsonRunBasics;
        contextSupplier.get().enqueueWork(() -> {
            if (finalJsonRunBasics != null) {
                finalJsonRunBasics.run();
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
