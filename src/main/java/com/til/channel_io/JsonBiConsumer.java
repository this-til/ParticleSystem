package com.til.channel_io;

import com.google.gson.JsonElement;
import com.til.particle_system.MainParticleSystem;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;

public class JsonBiConsumer implements BiConsumer<JsonElement, FriendlyByteBuf> {

    @Override
    public void accept(JsonElement jsonElement, FriendlyByteBuf friendlyByteBuf) {
        String s = MainParticleSystem.main.gson.toJson(jsonElement);
        friendlyByteBuf.writeInt(s.length());
        for (char c : s.toCharArray()) {
            friendlyByteBuf.writeChar(c);
        }
    }


}
