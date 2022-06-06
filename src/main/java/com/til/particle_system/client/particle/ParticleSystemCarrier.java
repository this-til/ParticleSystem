package com.til.particle_system.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.til.math.Colour;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.client.cell.IParticleSystemSupport;
import com.til.particle_system.client.cell.ParticleSystemCell;
import com.til.particle_system.client.render.RenderTypeManage;
import com.til.particle_system.element.ParticleSystem;
import com.til.particle_system.element.main.RenderElement;
import com.til.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

/***
 * 粒子系统的载体
 */
@OnlyIn(Dist.CLIENT)
public class ParticleSystemCarrier extends Particle implements IParticleSystemSupport {

    public final ParticleSystemCell particleSystemCell;

    public V3 pos;
    public Quaternion rotate;
    public V3 size;
    public V3 oldPos;
    public V3 oldSize;
    public Quaternion oldRotate;

    protected ParticleSystemCarrier(ClientLevel clientLevel, ParticleSystem particleSystem, V3 pos, Quaternion rotate, V3 size) {
        super(clientLevel, 0, 0, 0);
        this.pos = this.oldPos = pos;
        this.rotate = this.oldRotate = rotate;
        this.size = this.oldSize = size;
        this.particleSystemCell = new ParticleSystemCell(particleSystem, this);
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float time) {
        RenderElement element = particleSystemCell.particleSystem.renderElement;
        RenderTypeManage.getInstance().MAP.get(element.getClass()).render(Util.forcedConversion(element), particleSystemCell, vertexConsumer, camera, time);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        particleSystemCell.up();
    }

    @Override
    public int getLifetime() {
        return particleSystemCell.maxParticle;
    }

    @Override
    public boolean isAlive() {
        return !particleSystemCell.isDeath;
    }

    @Override
    public V3 getPos() {
        return pos;
    }

    @Override
    public V3 getOldPos() {
        return oldPos;
    }

    @Override
    public Quaternion getRotate() {
        return rotate;
    }

    @Override
    public Quaternion getOldRotate() {
        return oldRotate;
    }

    @Override
    public V3 getSize() {
        return size;
    }

    @Override
    public V3 getOldSize() {
        return oldSize;
    }

}
