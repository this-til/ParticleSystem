package com.til.particle_system.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import com.til.math.Colour;
import com.til.math.Quaternion;
import com.til.math.UV;
import com.til.math.V3;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.client.cell.IParticleSystemSupport;
import com.til.particle_system.client.cell.ParticleCell;
import com.til.particle_system.client.cell.ParticleSystemCell;
import com.til.particle_system.element.ParticleSystem;
import com.til.particle_system.element.main.RenderElement;
import com.til.util.List;
import com.til.util.Map;
import com.til.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class RenderTypeManage {

    public static final Map<Class<?>, IRenderType<?>> MAP = new Map<>();
    public static final List<ParticleSystemCell> NEED_RENDER = new List<>();

    public static <E extends RenderElement> void add(Class<E> type, IRenderType<E> iRenderType) {
        MAP.put(type, iRenderType);
    }

    public static <E extends RenderElement> IRenderType<E> get(Class<E> type) {
        return Util.forcedConversion(MAP.get(type));
    }

    public static final IRenderType<RenderElement.TextureRender> TEXTURE_RENDER_I_RENDER_TYPE = (render, particleCell, vertexConsumer, camera, time) -> {
        ParticleSystemCell particleSystemCell = particleCell.particleSystemCell;
        V3 particlePos = particleSystemCell.renderPos.add(V3.lerp(particleCell.pos, particleCell.oldPos, time)).reduce(camera.getPosition());
        Quaternion particleQuaternion = particleSystemCell.renderRotate.multiply(particleCell.rotate);
        V3 particleSize = particleSystemCell.renderSize.multiply(particleCell.size);
        Colour colour = particleCell.colour;
        V3 u1v1 = new V3(-1.0, -1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        V3 u1v0 = new V3(-1.0, 1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        V3 u0v0 = new V3(1.0, 1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        V3 u0v1 = new V3(1.0, -1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        UV uv = render.getUV(particleCell.time);
        float u0 = uv.u0;
        float u1 = uv.u1;
        float v0 = uv.v0;
        float v1 = uv.v1;
        float r = (float) colour.r;
        float g = (float) colour.b;
        float b = (float) colour.g;
        float a = (float) colour.a;
        int combined = 15 << 20 | 15 << 4;
        vertexConsumer.vertex(u1v1.x, u1v1.y, u1v1.z).uv(u1, v1).color(r, g, b, a).uv2(combined).endVertex();
        vertexConsumer.vertex(u1v0.x, u1v0.y, u1v0.z).uv(u1, v0).color(r, g, b, a).uv2(combined).endVertex();
        vertexConsumer.vertex(u0v0.x, u0v0.y, u0v0.z).uv(u0, v0).color(r, g, b, a).uv2(combined).endVertex();
        vertexConsumer.vertex(u0v1.x, u0v1.y, u0v1.z).uv(u0, v1).color(r, g, b, a).uv2(combined).endVertex();
    };

    public static final IRenderType<RenderElement.TextureOnlyZRotate> TEXTURE_ONLY_Z_RENDER_I_RENDER_TYPE = (render, particleCell, vertexConsumer, camera, time) -> {
        ParticleSystemCell particleSystemCell = particleCell.particleSystemCell;
        V3 particlePos = particleSystemCell.renderPos.add(V3.lerp(particleCell.pos, particleCell.oldPos, time)).reduce(camera.getPosition());
        V3 quaternionV3 = new V3(particleSystemCell.renderRotate.multiply(particleCell.rotate));
        V3 particleSize = particleSystemCell.renderSize.multiply(particleCell.size);
        Colour colour = particleCell.colour;
        Quaternion particleQuaternion = quaternionV3.z == 0 ? new Quaternion(camera.rotation()) : new Quaternion(camera.rotation()).multiply(V3.ZP.rotation(quaternionV3.z));
        V3 u1v1 = new V3(-1.0, -1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        V3 u1v0 = new V3(-1.0, 1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        V3 u0v0 = new V3(1.0, 1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        V3 u0v1 = new V3(1.0, -1.0, 0.0).transform(particleQuaternion).multiply(particleSize).add(particlePos);
        UV uv = render.getUV(particleCell.time);
        float u0 = uv.u0;
        float u1 = uv.u1;
        float v0 = uv.v0;
        float v1 = uv.v1;
        float r = (float) colour.r;
        float g = (float) colour.b;
        float b = (float) colour.g;
        float a = (float) colour.a;
        int combined = 15 << 20 | 15 << 4;
        vertexConsumer.vertex(u1v1.x, u1v1.y, u1v1.z).uv(u1, v1).color(r, g, b, a).uv2(combined).endVertex();
        vertexConsumer.vertex(u1v0.x, u1v0.y, u1v0.z).uv(u1, v0).color(r, g, b, a).uv2(combined).endVertex();
        vertexConsumer.vertex(u0v0.x, u0v0.y, u0v0.z).uv(u0, v0).color(r, g, b, a).uv2(combined).endVertex();
        vertexConsumer.vertex(u0v1.x, u0v1.y, u0v1.z).uv(u0, v1).color(r, g, b, a).uv2(combined).endVertex();
    };

    static {
        add(RenderElement.TextureRender.class, TEXTURE_RENDER_I_RENDER_TYPE);
        add(RenderElement.TextureOnlyZRotate.class, TEXTURE_ONLY_Z_RENDER_I_RENDER_TYPE);
    }

    public static final String textString =
            """
                    {
                          "mainElement": {
                            "maxLife": 100,
                            "loop": false,
                            "delay": 0,
                            "particleLife": 100,
                            "particleSpeed": 0,
                            "particleSize": {
                              "type": "no_separation",
                              "value": 10
                            },
                            "particleRotate": {
                              "x": 0,
                              "y": 0,
                              "z": 0
                            },
                            "particleColour": {
                              "r": 1,
                              "g": 1,
                              "b": 1,
                              "a": 1
                            },
                            "particleGravity": 0,
                            "worldCoordinate": "WORLD",
                            "bufferMode": "IGNORE",
                            "maxParticle": 5
                          },
                          "launchElement": {
                            "timeGenerate": 0,
                            "moveGenerate": 0,
                            "launchBursts": [
                              {
                                "needTime": 0,
                                "cycle": 1,
                                "amount": 1,
                                "intervalTime": 1,
                                "probability": 1
                              }
                            ]
                          },
                          "shapeElement": {
                            "type": "empty"
                          },
                          "renderElement": {
                            "type": "render_only_z",
                            "texture": "particle_system:textures/particle/small.png",
                            "timeUV": [
                              {
                                "time": 1,
                                "uv": {
                                   "u0": 1,
                                   "u1": 0,
                                   "v0": 1,
                                   "v1": 0
                                }
                              }
                            ]
                          }
                        }
                    """;

    public static ParticleSystem textParticleSystem;

    public static final ParticleRenderType NO_RENDER = new ParticleRenderType() {
        @Override
        public void begin(@NotNull BufferBuilder p_107436_, @NotNull TextureManager p_107437_) {

        }

        @Override
        public void end(@NotNull Tesselator p_107438_) {

        }

        @Override
        public String toString() {
            return MainParticleSystem.MOD_ID + ":" + "no_render";
        }
    };

}
