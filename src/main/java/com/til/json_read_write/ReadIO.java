package com.til.json_read_write;

import com.google.gson.JsonElement;
import com.til.particle_system.MainParticleSystem;
import com.til.util.Util;
import com.til.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/***
 * 对文件的读写操作
 * @author til
 */
public class ReadIO {

    public final JsonAnalysis jsonAnalysis;
    public Map<Class<?>, Map<String, JsonReloadListenerCell<?>>> resources = new Map<>();


    public ReadIO(JsonAnalysis jsonAnalysis) {
        this.jsonAnalysis = jsonAnalysis;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEvent(AddReloadListenerEvent event) {
        resources.forEach((k, v) -> v.forEach((_k, _v) -> event.addListener(_v)));
    }

    /***
     * 新添加一个json资源包读取
     * @param p 目录
     * @param type 反射类型
     */
    public <E> JsonReloadListenerCell<E> add(String p, Class<E> type) {
        JsonReloadListenerCell<E> jsonReloadListenerCell = new JsonReloadListenerCell<>(this, type, p, jsonAnalysis);
        resources.get(type, Map::new).put(p, jsonReloadListenerCell);
        return jsonReloadListenerCell;
    }

    /***
     * 获取资源
     * @throws NullPointerException 没有注册资源时调用产生
     */
    public <E> JsonReloadListenerCell<E> get(String p, Class<E> type) throws NullPointerException {
        return Util.forcedConversion(resources.get(type).get(p));
    }

    public static class JsonReloadListenerCell<E> extends SimpleJsonResourceReloadListener {

        public final ReadIO readIO;
        public final JsonAnalysis jsonAnalysis;
        public final Map<ResourceLocation, E> map;
        public final Class<E> type;

        public JsonReloadListenerCell(ReadIO readIO, Class<E> type, String p, JsonAnalysis jsonAnalysis) {
            super(MainParticleSystem.main.jsonAnalysis.gson, p);
            this.type = type;
            this.readIO = readIO;
            this.map = new Map<>();
            this.jsonAnalysis = jsonAnalysis;
        }

        @Override
        protected void apply(java.util.@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager p_10794_, @NotNull ProfilerFiller p_10795_) {
            resourceLocationJsonElementMap.forEach((k, v) -> {
                try {
                    map.put(k, jsonAnalysis.as(type, v));
                } catch (Exception e) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("在解析目录为[{0}]，类型为[{1}]，的Json字段[{2}]时出现问题：", k, type, v), e);
                }
            });
        }
    }


}
