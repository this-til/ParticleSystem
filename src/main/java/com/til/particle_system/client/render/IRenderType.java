package com.til.particle_system.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.client.cell.ParticleCell;
import com.til.particle_system.client.cell.ParticleSystemCell;
import com.til.particle_system.element.main.RenderElement;
import com.til.util.Map;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public interface IRenderType<E extends RenderElement> {

    void render(E e, ParticleCell particleSystemCell, VertexConsumer vertexConsumer, Camera camera, float time);


    default ParticleRenderType getRenderType(E e) {
        ResourceLocation resourceLocation = e.getTexture();
        if (resourceLocation == null) {
            return NULL_TEXTURE;
        }
        if (PARTICLE_RENDER_OF_TEXTURE.containsKey(resourceLocation)) {
            return PARTICLE_RENDER_OF_TEXTURE.get(resourceLocation);
        } else {
            ParticleRenderType particleRenderType = new ParticleRenderType() {
                @Override
                public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                    Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

                    RenderSystem.setShaderTexture(0, resourceLocation);
                    AbstractTexture tex = textureManager.getTexture(resourceLocation);
                    tex.setBlurMipmap(true, false);
                    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                }

                @Override
                public void end(@NotNull Tesselator tesselator) {
                    tesselator.end();
                    @SuppressWarnings("deprecation") AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES);
                    tex.restoreLastBlurMipmap();
                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                }

                @Override
                public String toString() {
                    return resourceLocation.toString();
                }

            };
            PARTICLE_RENDER_OF_TEXTURE.put(resourceLocation, particleRenderType);
            return particleRenderType;
        }
    }

    Map<ResourceLocation, ParticleRenderType> PARTICLE_RENDER_OF_TEXTURE = new Map<>();
    ParticleRenderType NULL_TEXTURE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder bufferBuilder, @NotNull TextureManager textureManager) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(@NotNull Tesselator tesselator) {
            tesselator.end();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return MainParticleSystem.MOD_ID + ":" + "null_texture";
        }
    };
}
