package com.til.particle_system.element.cell;


import com.til.math.V2;
import com.til.particle_system.element.main.LaunchElement;
import com.til.util.Extension;
import com.til.util.List;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.ParticleSystem;
import com.til.util.Map;

import java.util.Random;

/***
 * 一个粒子系统的元素
 * @author til
 */
public class ParticleSystemCell {

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

    public ParticleSystemCell(ParticleSystem particleSystem, IParticleSystemSupport iParticleSystemSupport) {
        this.particleSystem = particleSystem;
        this.iParticleSystemSupport = iParticleSystemSupport;
        maxParticle = particleSystem.mainElement.maxParticle;
        particleCells = new List<>(Math.min(maxParticle, 32));
        launchBurstMap = new Map<>();
        particleSystem.launchElement.launchBursts.forEach(e -> launchBurstMap.put(e, new Extension.Data2<>(e.needTime.intValue(), e.cycle.intValue())));
        maxLife = particleSystem.mainElement.maxLife;
        isLoop = particleSystem.mainElement.loop;
    }


    /***
     * 毎t刷新
     */
    public void up() {
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
                if (v.d2 < 0) {
                    return;
                }
                v.d1--;
                if (v.d1 <= 0) {
                    v.d2++;
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
            particleCells.remove(e -> {
                e.up();
                return e.isDeath;
            });
        }
        //系统的移动
        {
            if (particleSystem.mainElement.worldCoordinate.equals(MainElement.ParticleCoordinate.WORLD)) {
                if (!move.isEmpty()) {
                    particleCells.forEach(cell -> cell.pos.reduce(move));
                }
                Quaternion quaternion = iParticleSystemSupport.getRotate().multiply(iParticleSystemSupport.getOldRotate().inverse());
                if (!quaternion.isEmpty()) {
                    particleCells.forEach(cell -> cell.rotate.multiply(quaternion));
                }
            }
        }
    }


    /***
     * 发射粒子
     */
    public void launch() {
        if (particleCells.size() <= maxParticle) {
            particleCells.add(new ParticleCell(this, time));
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
    }

}
