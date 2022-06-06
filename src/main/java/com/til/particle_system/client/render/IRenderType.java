package com.til.particle_system.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.til.json_read_write.annotation.JsonField;
import com.til.math.Colour;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.client.cell.IParticleSystemSupport;
import com.til.particle_system.client.cell.ParticleCell;
import com.til.particle_system.client.cell.ParticleSystemCell;
import com.til.particle_system.element.main.RenderElement;
import com.til.util.Map;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
public interface IRenderType<E extends RenderElement> {

    void render(E e, ParticleSystemCell particleSystemCell, VertexConsumer vertexConsumer, Camera camera, float time);


}
