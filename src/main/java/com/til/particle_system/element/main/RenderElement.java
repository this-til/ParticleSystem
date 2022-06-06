package com.til.particle_system.element.main;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.particle_system.client.cell.ParticleSystemCell;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/***
 * 粒子的渲染
 * @author til
 */
@BaseClass(sonClass = {RenderElement.EmptyRenderElement.class, RenderElement.EmptyRenderElement.class})
@DefaultNew(newExample = RenderElement.EmptyRenderElement.class)
public abstract class RenderElement {


    @SonClass(name = EmptyRenderElement.NAME)
    public static class EmptyRenderElement extends RenderElement {
        public static final String NAME = "empty";


    }

    @SonClass(name = TextureRender.NAME)
    public static class TextureRender extends RenderElement {

        @JsonField
        public float u0;

        @JsonField
        public float u1;

        @JsonField
        public float v0;

        @JsonField
        public float v1;

        /***
         * 贴图路径
         */
        @JsonField
        public String texture;

        public static final String NAME = "render";

    }

}
