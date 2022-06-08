package com.til.particle_system.client.cell;


import com.mojang.blaze3d.vertex.VertexConsumer;
import com.til.math.V2;
import com.til.particle_system.client.render.RenderTypeManage;
import com.til.particle_system.element.main.LaunchElement;
import com.til.util.Extension;
import com.til.util.List;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.ParticleSystem;
import com.til.util.Map;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

/***
 * 一个粒子系统的元素
 * @author til
 */
@OnlyIn(Dist.CLIENT)
public class ParticleSystemCell extends Particle {

    /***
     * 粒子系统模板
     */
    public final ParticleSystem particleSystem;

    /***
     * 粒子系统的支持
     */
    public final IParticleSystemSupport iParticleSystemSupport;

    /***
     * 子粒子
     */
    public final List<ParticleCell> particleCells;

    public final Map<LaunchElement.LaunchBurst, Extension.Data2<Integer, Integer>> launchBurstMap;

    public final Random rand = new Random();

    /***
     * 最大粒子数
     */
    public final int maxParticle;

    /***
     * 最大生命
     */
    public final int maxLife;
    /***
     * life和maxLife的比值
     */
    public double time;

    /***
     * 开启循环
     */
    public final boolean isLoop;

    /***
     * 当前生命
     */
    public int life;

    /***
     * 延迟启动
     */
    public int delay;

    /***
     * 系统死亡
     */
    public boolean isDeath;
    /***
     * 发射粒子数量
     */
    public double launchParticleAmount;

    /***
     * 渲染位置
     */
    public V3 renderPos;

    /***
     * 渲染旋转
     */
    public Quaternion renderRotate;

    /***
     * 渲染大小
     */
    public V3 renderSize;

    /***
     * 粒子范围
     */
    public AABB aabb;

    public ParticleSystemCell(ParticleSystem particleSystem, IParticleSystemSupport iParticleSystemSupport) {
        super(iParticleSystemSupport.getClient(), 0, 0, 0);
        this.particleSystem = particleSystem;
        this.iParticleSystemSupport = iParticleSystemSupport;
        maxParticle = particleSystem.mainElement.maxParticle;
        particleCells = new List<>(Math.min(maxParticle, 32));
        launchBurstMap = new Map<>();
        particleSystem.launchElement.launchBursts.forEach(e -> launchBurstMap.put(e, new Extension.Data2<>(e.needTime.intValue(), e.cycle.intValue())));
        maxLife = particleSystem.mainElement.maxLife;
        isLoop = particleSystem.mainElement.loop;
        renderPos = new V3();
        renderRotate = new Quaternion();
        renderSize = new V3();
        refreshAABB();
    }


    /***
     * 毎t刷新
     */
    @Override
    public void tick() {
        V3 move = iParticleSystemSupport.getPos().reduce(iParticleSystemSupport.getOldPos());
        {
            time = V2.getProportionStatic(0, maxLife, life);
            if (delay-- > 0) {
                return;
            }
            if (!particleSystem.mainElement.bufferMode.equals(MainElement.ParticleBufferMode.SUSPEND) && particleCells.size() <= maxParticle) {
                life++;
            }
            if (life > maxLife) {
                if (isLoop) {
                    life = 0;
                    delay = particleSystem.mainElement.delay.as().intValue();
                } else {
                    setDeath();
                }
            }
        }
        // 粒子的发射
        {
            launchParticleAmount += particleSystem.launchElement.timeGenerate.as(time).doubleValue();
            launchParticleAmount += particleSystem.launchElement.moveGenerate.as(time).doubleValue() * move.magnitude();
            for (; launchParticleAmount > 0; launchParticleAmount--) {
                launch();
            }
            launchBurstMap.forEach((k, v) -> {
                if (v.d2 <= 0) {
                    return;
                }
                v.d1--;
                if (v.d1 <= 0) {
                    v.d2--;
                    v.d1 = k.intervalTime.as(time).intValue();
                    if (rand.nextDouble() < k.probability.as().doubleValue()) {
                        int a = k.amount.as(time).intValue();
                        for (int i = 0; i < a; i++) {
                            launch();
                        }
                    }
                }
            });
        }
        // 粒子的刷新
        {
            particleCells.remove(e -> e.isDeath);
        }
        //系统的移动
        {
            if (particleSystem.mainElement.worldCoordinate.equals(MainElement.ParticleCoordinate.WORLD)) {
                if (!move.isEmpty()) {
                    particleCells.forEach(cell -> {
                        cell.pos = cell.pos.reduce(move);
                        cell.oldPos = cell.pos;
                    });
                }
                Quaternion quaternion = iParticleSystemSupport.getRotate().multiply(iParticleSystemSupport.getOldRotate().inverse());
                if (!quaternion.isEmpty()) {
                    particleCells.forEach(cell -> {
                        cell.rotate = cell.rotate.multiply(quaternion);
                        cell.oldRotate = cell.rotate;
                    });
                }
            }
        }
        //刷新aabb
        {
            refreshAABB();
        }
    }


    /***
     * 发射粒子
     */
    public void launch() {
        if (particleCells.size() <= maxParticle) {
            ParticleCell particleCell = new ParticleCell(this);
            Minecraft.getInstance().particleEngine.add(particleCell);
            particleCells.add(particleCell);
        } else {
            if (particleSystem.mainElement.bufferMode == MainElement.ParticleBufferMode.KILL) {
                particleCells.remove(particleCells.get(0));
                launch();
            }
        }
    }

    /***
     * 设置死亡
     * 无论粒子是不是循环都杀死
     */
    public void setDeath() {
        isDeath = true;
        for (ParticleCell particleCell : particleCells) {
            particleCell.isDeath = true;
        }
    }

    public void refreshAABB() {
        V3 mPos = iParticleSystemSupport.getPos();
        V3 mSize = iParticleSystemSupport.getSize();
        V3 max = mPos.add(mSize);
        V3 min = mPos.reduce(mSize);
        aabb = new AABB(max.x, max.y, max.z, min.x, min.y, min.z);
    }

    @Override
    public void remove() {
        setDeath();
    }

    @Override
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
    protected void setAlpha(float p_107272_) {
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
    @Deprecated
    public void setColor(float p_107254_, float p_107255_, float p_107256_) {
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
    @Deprecated
    protected void setLocationFromBoundingbox() {
    }

    @Override
    @Deprecated
    public void setBoundingBox(@NotNull AABB p_107260_) {
    }

    @Override
    @Deprecated
    public int getLifetime() {
        return 0;
    }

    @Override
    public @NotNull Optional<ParticleGroup> getParticleGroup() {
        return Optional.empty();
    }

    @Override
    @Deprecated
    protected int getLightColor(float p_107249_) {
        return 0;
    }

    @Override
    @Deprecated
    public void move(double p_107246_, double p_107247_, double p_107248_) {
    }

    @Override
    public @NotNull AABB getBoundingBox() {
        return aabb;
    }

    @Override
    @Deprecated
    public @NotNull Particle scale(float p_107270_) {
        return this;
    }

    @Override
    public boolean shouldCull() {
        return true;
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float time) {
        renderPos = V3.lerp(iParticleSystemSupport.getPos(), iParticleSystemSupport.getOldPos(), time);
        renderRotate = Quaternion.lerp(iParticleSystemSupport.getRotate(), iParticleSystemSupport.getOldRotate(), time);
        renderSize = V3.lerp(iParticleSystemSupport.getSize(), iParticleSystemSupport.getOldSize(), time);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return RenderTypeManage.NO_RENDER;
    }
}
