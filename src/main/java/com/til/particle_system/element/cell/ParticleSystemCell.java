package com.til.particle_system.element.cell;


import com.til.json_read_write.util.List;
import com.til.json_read_write.util.math.Quaternion;
import com.til.json_read_write.util.math.V3;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.element.IElement;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.ParticleSystem;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeColourElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeSpeedColourElement;
import com.til.particle_system.util.ParticleSystemUtil;

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
     * 开启循环
     */
    public final boolean loop;

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

    public ParticleSystemCell(ParticleSystem particleSystem, V3 pos, Quaternion rotate, V3 size) {
        this.particleSystem = particleSystem;
        this.pos = pos;
        this.rotate = rotate;
        this.size = size;
        maxParticle = particleSystem.mainElement.maxParticle;
        particleCells = new List<>(maxParticle);
        maxLife = particleSystem.mainElement.maxLife;
        loop = particleSystem.mainElement.loop;
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
