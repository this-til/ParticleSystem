package com.til.particle_system.element.cell;


import com.til.math.V2;
import com.til.util.List;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.ParticleSystem;

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
     * 子粒子
     */
    public final List<ParticleCell> particleCells;

    /***
     * 最大粒子数
     */
    public final int maxParticle;

    /***
     * 最大生命
     */
    public final int maxLife;
    /***
     * 相对于maxLife和life的时间
     */
    public double time;

    /***
     * 开启循环
     */
    public final boolean isLoop;

    /***
     * 当前粒子系统的坐标
     */
    public V3 pos;

    /***
     * 粒子系统的旋转
     */
    public Quaternion rotate;

    /***
     * 粒子系统的大小
     */
    public V3 size;

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

    public ParticleSystemCell(ParticleSystem particleSystem, V3 pos, Quaternion rotate, V3 size) {
        this.particleSystem = particleSystem;
        this.pos = pos;
        this.rotate = rotate;
        this.size = size;
        maxParticle = particleSystem.mainElement.maxParticle;
        particleCells = new List<>(maxParticle);
        maxLife = particleSystem.mainElement.maxLife;
        isLoop = particleSystem.mainElement.loop;
    }

    /***
     * 新的循环
     */
    public void newLoop() {
        life = 0;
        delay = particleSystem.mainElement.delay.as().intValue();
    }

    /***
     * 毎t刷新
     */
    public void up() {
        {
            time = V2.getProportionStatic(0, maxLife, life);
            if (delay-- > 0) {
                return;
            }
            life++;
            if (life > maxLife) {
                if (isLoop) {
                    newLoop();
                } else {
                    setDeath();
                }
            }
        }
        {
            launchParticleAmount += particleSystem.launchElement.timeGenerate.as(time).doubleValue();
            for (; launchParticleAmount > 0; launchParticleAmount--) {
                launch();
            }
        }
        upParticle();
    }


    /***
     * 刷新粒子
     */
    protected void upParticle() {
        particleCells.remove(e -> {
            e.up();
            return e.isDeath;
        });
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

    /***
     * 移动粒子系统
     * @param m 移动向量
     */
    public void move(V3 m) {
        pos = pos.add(m);
        MainElement mainElement = particleSystem.mainElement;
        particleCells.forEach(cell -> mainElement.worldCoordinate.move(this, cell, m));
    }

    /***
     * 旋转粒子系统
     * @param r 旋转向量
     */
    public void rotate(Quaternion r) {
        rotate = rotate.multiply(r);
        MainElement mainElement = particleSystem.mainElement;
        particleCells.forEach(cell -> mainElement.worldCoordinate.rotate(this, cell, r));
    }

    /***
     * 设置粒子系统大小
     * @param size 大小
     */
    public void setSize(V3 size) {
        this.size = size;
    }

}
