package com.til.particle_system.client.cell;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.til.math.Colour;
import com.til.math.Quaternion;
import com.til.math.V2;
import com.til.math.V3;
import com.til.particle_system.client.render.IRenderType;
import com.til.particle_system.client.render.RenderTypeManage;
import com.til.particle_system.element.control.StartSpeedLifeElement;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeColourElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeSpeedColourElement;
import com.til.particle_system.element.particle_life_time.move.LifeTimeForceElement;
import com.til.particle_system.element.particle_life_time.move.LifeTimeTrackElement;
import com.til.particle_system.element.particle_life_time.rotate.LifeTimeRotateElement;
import com.til.particle_system.element.particle_life_time.rotate.LifeTimeSpeedRotateElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSizeElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSpeedSizeElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedExtendElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedLimitElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedResistanceElement;
import com.til.util.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

/***
 * ??????
 * @author til
 */
@OnlyIn(Dist.CLIENT)
public class ParticleCell extends Particle implements IParticleSystemSupport {
    /***
     * ????????????
     */
    public final ParticleSystemCell particleSystemCell;

    /***
     * ??????????????????
     */
    public final IRenderType<?> iRenderType;

    /***
     * ??????????????????????????????????????????????????????
     */
    public final double structureTime;

    /***
     * ??????????????????
     */
    public final int maxLife;
    /***
     * ????????????
     */
    public final double gravity;

    /***
     * ?????????????????????
     * ??????????????????????????????
     */
    public V3 startMove;
    /***
     * ??????
     * ???????????????
     */
    public V3 move;
    /***
     * ???????????????
     */
    public final Colour startColour;
    /***
     * ???????????????
     */
    public final V3 startSize;
    /***
     * ??????????????????
     */
    public int life;
    /***
     * ???????????????????????????
     * ?????????????????????
     */
    public boolean isDeath;

    /***
     * ???????????????0~1??????????????????
     */
    public double time;
    /***
     * ??????t???????????????
     */
    public double oldTime;


    /***
     * ?????????????????????????????????????????????
     */
    public V3 pos;
    /***
     * ????????????????????????????????????
     */
    public Quaternion rotate;
    /***
     * ????????????????????????????????????
     */
    public V3 size;
    /***
     * ???????????????
     */
    public Colour colour;
    /***
     * ???t???????????????
     */
    public V3 oldPos;
    /***
     * ???t???????????????
     */
    public V3 oldSize;
    /***
     * ???t???????????????
     */
    public Colour oldColor;
    /***
     * ???t???????????????
     */
    public Quaternion oldRotate;

    /***
     * ????????????
     */
    public V3 renderPos;

    /***
     * ????????????
     */
    public Quaternion renderRotate;

    /***
     * ????????????
     */
    public V3 renderSize;

    /***
     * ????????????????????????
     */
    public AABB aabb;

    public ParticleCell(ParticleSystemCell particleSystemCell) {
        super(particleSystemCell.iParticleSystemSupport.getClient(), 0, 0, 0);
        this.particleSystemCell = particleSystemCell;
        this.time = particleSystemCell.time;
        this.structureTime = particleSystemCell.time;
        MainElement mainElement = particleSystemCell.particleSystem.mainElement;
        this.startSize = mainElement.particleSize.as(structureTime);
        this.startColour = mainElement.particleColour.as(structureTime);
        this.gravity = mainElement.particleGravity.as(structureTime).doubleValue();
        this.rotate = particleSystemCell.particleSystem.shapeElement.getStartRotate(this);
        this.startMove = particleSystemCell.particleSystem.shapeElement.getStartMove(this);
        this.pos = particleSystemCell.particleSystem.shapeElement.getStartPos(this);
        this.rotate = new Quaternion(mainElement.particleRotate.as(structureTime));
        this.iRenderType = RenderTypeManage.get(particleSystemCell.particleSystem.renderElement.getClass());
        {
            StartSpeedLifeElement element = particleSystemCell.particleSystem.startSpeedLifeElement;
            int maxLife = mainElement.particleLife.as(structureTime).intValue();
            if (element != null) {
                this.maxLife = maxLife * element.multiplyLife.as(element.speedRange.getProportion(move.magnitude())).intValue();
            } else {
                this.maxLife = maxLife;
            }
        }
        writeStart();
        writeOld();
        refreshAABB();
        {
            LifeTimeSpeedExtendElement element = particleSystemCell.particleSystem.lifeTimeSpeedExtendElement;
            if (element != null && element.extendType.equals(LifeTimeSpeedExtendElement.ExtendType.START)) {
                V3 move = particleSystemCell.iParticleSystemSupport.getPos().reduce(particleSystemCell.iParticleSystemSupport.getOldPos());
                if (!move.isEmpty()) {
                    startMove.add(move.multiply(element.extend.as()));
                }
            }
        }
    }

    @Override
    public void tick() {
        life++;
        if (life >= maxLife) {
            isDeath = true;
        }
        if (isDeath) {
            return;
        }
        time = V2.getProportionStatic(0, maxLife, life);
        writeOld();
        writeStart();
        //speed
        {

            {
                LifeTimeForceElement element = particleSystemCell.particleSystem.lifeTimeForceElement;
                if (element != null) {
                    startMove = startMove.add(element.force.as(time));
                    move = startMove;
                }
            }
            {
                LifeTimeSpeedExtendElement element = particleSystemCell.particleSystem.lifeTimeSpeedExtendElement;
                if (element != null && element.extendType.equals(LifeTimeSpeedExtendElement.ExtendType.ALWAYS)) {
                    V3 move = particleSystemCell.iParticleSystemSupport.getPos().reduce(particleSystemCell.iParticleSystemSupport.getOldPos());
                    if (!move.isEmpty()) {
                        startMove = startMove.add(move.multiply(element.extend.as()));
                        this.move = startMove;
                    }
                }
            }
            {
                LifeTimeSpeedResistanceElement element = particleSystemCell.particleSystem.lifeTimeSpeedResistanceElement;
                if (element != null) {
                    double resistance = element.resistance.as(time).doubleValue();
                    if (element.multiplySize) {
                        resistance *= size.magnitude();
                    }
                    if (element.multiplySpeed) {
                        resistance *= move.magnitude();
                    }
                    resistance = Math.min(1, Math.max(0, resistance));
                    move = move.add(move.multiply(-resistance));
                }
            }
            {
                LifeTimeSpeedElement element = particleSystemCell.particleSystem.lifeTimeSpeedElement;
                if (element != null) {
                    move = move.multiply(element.speed.as(time));
                }
            }
            {
                LifeTimeTrackElement element = particleSystemCell.particleSystem.lifeTimeTrackElement;
                if (element != null) {
                    V3 track = element.track.as(time);
                    double eTime = element.time.as(time).doubleValue();
                    double _time = time * eTime;
                    double _oldTime = oldTime * eTime;
                    if (track.x != 0) {
                        //y,z
                        V3 _oldPos = new V3(0, Math.sin(_oldTime) * track.x, Math.cos(_oldTime) * track.x);
                        V3 _pos = new V3(0, Math.sin(_time) * track.x, Math.cos(_time) * track.x);
                        V3 _move = _pos.reduce(_oldPos);
                        move = move.add(_move);
                    }
                    if (track.y != 0) {
                        //x,z
                        V3 _oldPos = new V3(Math.sin(_oldTime) * track.y, 0, Math.cos(_oldTime) * track.y);
                        V3 _pos = new V3(Math.sin(_time) * track.y, 0, Math.cos(_time) * track.y);
                        V3 _move = _pos.reduce(_oldPos);
                        move = move.add(_move);
                    }
                    if (track.z != 0) {
                        //x,y
                        V3 _oldPos = new V3(Math.sin(_oldTime) * track.x, Math.cos(_oldTime) * track.x, 0);
                        V3 _pos = new V3(Math.sin(_time) * track.x, Math.cos(_time) * track.x, 0);
                        V3 _move = _pos.reduce(_oldPos);
                        move = move.add(_move);
                    }
                }
            }
            {
                LifeTimeSpeedLimitElement element = particleSystemCell.particleSystem.lifeTimeSpeedLimitElement;
                if (element != null) {
                    V3 threshold = element.threshold.as(time);
                    V3 speedThreshold = element.speedThreshold.as(time);
                    double nx = startMove.x;
                    double ny = startMove.y;
                    double nz = startMove.z;
                    if (nx > threshold.x) {
                        nx -= (nx - threshold.x) * Math.min(1, Math.max(0, speedThreshold.x));
                    }
                    if (ny > threshold.y) {
                        ny -= (ny - threshold.y) * Math.min(1, Math.max(0, speedThreshold.y));
                    }
                    if (nz > threshold.z) {
                        nz -= (nz - threshold.z) * Math.min(1, Math.max(0, speedThreshold.z));
                    }
                    startMove = new V3(nx, ny, nz);
                    move = startMove;
                }
            }
        }
        //rotate
        {
            {
                LifeTimeRotateElement element = particleSystemCell.particleSystem.lifeTimeRotateElement;
                if (element != null) {
                    rotate = rotate.multiply(new Quaternion(element.angularVelocity.as(time)));
                }

            }
            {

                LifeTimeSpeedRotateElement element = particleSystemCell.particleSystem.lifeTimeSpeedRotateElement;
                if (element != null) {
                    rotate = rotate.multiply(new Quaternion(element.angularVelocity.as(element.speedRange.getProportion(move.magnitude()))));
                }
            }
        }
        //size
        {
            {
                LifeTimeSizeElement element = particleSystemCell.particleSystem.lifeTimeSizeElement;
                if (element != null) {
                    size = size.multiply(element.size.as(time));
                }
            }
            {

                LifeTimeSpeedSizeElement element = particleSystemCell.particleSystem.lifeTimeSpeedSizeElement;
                if (element != null) {
                    size = size.multiply(element.size.as(element.speedRange.getProportion(move.magnitude())));
                }
            }
        }
        //colour
        {
            {
                LifeTimeColourElement element = particleSystemCell.particleSystem.lifeTimeColourElement;
                if (element != null) {
                    colour = colour.multiply(element.colour.as(time));
                }
            }
            {
                LifeTimeSpeedColourElement element = particleSystemCell.particleSystem.lifeTimeSpeedColourElement;
                if (element != null) {
                    colour = colour.multiply(element.colour.as(element.speedRange.getProportion(move.magnitude())));
                }
            }
        }
        if (!startMove.isEmpty()) {
            pos = pos.add(startMove);
            refreshAABB();
        }
    }

    public void writeOld() {
        oldPos = pos;
        oldSize = size;
        oldColor = colour;
        oldRotate = rotate;
    }

    public void writeStart() {
        size = startSize;
        move = startMove;
        colour = startColour;
    }

    public void refreshAABB() {
        V3 mPos = particleSystemCell.iParticleSystemSupport.getPos().add(pos);
        V3 mSize = particleSystemCell.iParticleSystemSupport.getSize().multiply(size);
        V3 max = mPos.add(mSize);
        V3 min = mPos.reduce(mSize);
        aabb = new AABB(max.x, max.y, max.z, min.x, min.y, min.z);
    }

    @Override
    public V3 getPos() {
        IParticleSystemSupport support = particleSystemCell.iParticleSystemSupport;
        return support.getPos().add(pos.multiply(support.getSize()).transform(support.getRotate()));
    }

    @Override
    public V3 getOldPos() {
        IParticleSystemSupport support = particleSystemCell.iParticleSystemSupport;
        return support.getOldPos().add(oldPos.multiply(support.getSize()).transform(support.getOldRotate()));
    }

    @Override
    public Quaternion getRotate() {
        return particleSystemCell.iParticleSystemSupport.getRotate().multiply(rotate);
    }

    @Override
    public Quaternion getOldRotate() {
        return particleSystemCell.iParticleSystemSupport.getOldRotate().multiply(oldRotate);
    }

    @Override
    public V3 getSize() {
        return particleSystemCell.iParticleSystemSupport.getSize().multiply(size);
    }

    @Override
    public V3 getOldSize() {
        return particleSystemCell.iParticleSystemSupport.getOldSize().multiply(oldSize);
    }

    @Override
    public V3 getRenderPos(float time) {
        return renderPos;
    }

    @Override
    public Quaternion getRenderRotate(float time) {
        return renderRotate;
    }

    @Override
    public V3 getRenderSize(float time) {
        return renderSize;
    }

    @Override
    public ClientLevel getClient() {
        return particleSystemCell.iParticleSystemSupport.getClient();
    }

    @Override
    @Deprecated
    public boolean isAlive() {
        return !isDeath;
    }

    @Override
    @Deprecated
    public @NotNull Particle setPower(float p_107269_) {
        return this;
    }

    @Override
    @Deprecated
    public void setColor(float r, float b, float g) {
    }

    @Override
    @Deprecated
    protected void setAlpha(float a) {
    }

    @Override
    @Deprecated
    public void setParticleSpeed(double p_172261_, double p_172262_, double p_172263_) {
    }

    @Override
    @Deprecated
    public void setLifetime(int p_107258_) {
    }

    @Override
    public @NotNull Particle scale(float p_107683_) {
        return this;
    }

    @Override
    @Deprecated
    protected int getLightColor(float p_107249_) {
        return 0;
    }

    @Override
    @Deprecated
    protected void setSize(float p_107251_, float p_107252_) {
    }

    @Override
    @Deprecated
    public void setPos(double p_107265_, double p_107266_, double p_107267_) {
    }

    @Override
    public void remove() {
        this.isDeath = true;
    }

    @Override
    public boolean shouldCull() {
        return true;
    }

    @Override
    @Deprecated
    protected void setLocationFromBoundingbox() {
    }

    @Override
    @Deprecated
    public void setBoundingBox(@NotNull AABB p_107260_) {
    }

    @Override
    public @NotNull AABB getBoundingBox() {
        return aabb;
    }

    @Override
    public @NotNull Optional<ParticleGroup> getParticleGroup() {
        return Optional.empty();
    }

    @Override
    public void move(double p_107246_, double p_107247_, double p_107248_) {
        super.move(p_107246_, p_107247_, p_107248_);
    }

    @Override
    public int getLifetime() {
        return maxLife;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return iRenderType.getRenderType(Util.forcedConversion(particleSystemCell.particleSystem.renderElement));
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float time) {
        renderPos = particleSystemCell.renderPos.add(V3.lerp(pos, oldPos, time).multiply(particleSystemCell.renderSize).transform(particleSystemCell.renderRotate));
        renderSize = particleSystemCell.renderSize.multiply(V3.lerp(size, oldSize, time));
        renderRotate = particleSystemCell.renderRotate.multiply(Quaternion.lerp(rotate, oldRotate, time));
        iRenderType.render((Util.forcedConversion(particleSystemCell.particleSystem.renderElement)),
                this, vertexConsumer, camera, time);
    }


}
