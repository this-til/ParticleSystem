package com.til.particle_system.element.main;


import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.element.IElement;
import com.til.particle_system.element.MainElement;
import com.til.particle_system.util.List;
import com.til.particle_system.util.Quaternion;
import com.til.particle_system.util.Util;
import com.til.particle_system.util.V3;

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
        MainElement mainElement = particleSystem.get(MainElement.class);
        maxParticle = mainElement.maxParticle.intValue();
        particleCells = new List<>(maxParticle);
        maxLife = mainElement.maxLife.intValue();
        loop = mainElement.loop;
    }

    /***
     * 新的循环
     */
    public void newLoop() {
        life = 0;
        delay = particleSystem.get(MainElement.class).delay.as().intValue();
    }

    /***
     * 毎t刷新
     */
    public void up() {
        MainElement mainElement = particleSystem.get(MainElement.class);
        life++;
        if (life > maxLife){

        }
        MainParticleSystem.main.registerManage.INTERCEPT.get(IElement.IParticleElement.class).forEach(c -> {
            if (particleSystem.map.containsKey(c)) {
                particleCells.forEach(e -> {
                    particleSystem.get(Util.<Class<IElement.IParticleElement>>forcedVonversion(c)).up(e);
                });
            }
        });
        List<ParticleCell> endParticleCell = null;
        for (ParticleCell particleCell : particleCells) {
            if (particleCell.isDeath) {
                if (endParticleCell == null) {
                    endParticleCell = new List<>();
                }
                endParticleCell.add(particleCell);
            }
        }
        if (endParticleCell != null) {
            particleCells.remove(endParticleCell);
        }

    }

    /***
     * 移动粒子系统
     * @param m 移动向量
     */
    public void move(V3 m) {
        pos = pos.add(m);
        MainElement mainElement = particleSystem.get(MainElement.class);
        particleCells.forEach(cell -> mainElement.worldCoordinate.move(this, cell, m));
    }

    /***
     * 旋转粒子系统
     * @param r 旋转向量
     */
    public void rotate(Quaternion r) {
        rotate = rotate.multiply(r);
        MainElement mainElement = particleSystem.get(MainElement.class);
        particleCells.forEach(cell -> mainElement.worldCoordinate.rotate(this, cell, r));
    }

    public void setSize(V3 size) {
        this.size = size;
    }

}
