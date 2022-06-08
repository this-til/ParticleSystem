package com.til.channel_io;

import com.google.gson.JsonElement;
import com.til.particle_system.MainParticleSystem;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.core.jackson.JsonConstants;

import java.util.function.Function;

public class JsonFunction implements Function<FriendlyByteBuf, JsonElement> {

    @Override
    public JsonElement apply(FriendlyByteBuf friendlyByteBuf) {
        int l = friendlyByteBuf.readInt();
        char[] chars = new char[l];
        for (int i = 0; i < l; i++) {
            chars[i] = friendlyByteBuf.readChar();
        }
        String s = String.valueOf(chars);
        return MainParticleSystem.main.jsonAnalysis.gson.toJsonTree(s);
    }
}
