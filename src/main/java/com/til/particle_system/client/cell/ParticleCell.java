package com.til.particle_system.client.cell;

import com.til.math.Colour;
import com.til.math.Quaternion;
import com.til.math.V2;
import com.til.math.V3;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/***
 * 粒子
 * @author til
 */
@OnlyIn(Dist.CLIENT)
public class ParticleCell {
    /***
     * 粒子系统
     */
    public final ParticleSystemCell particleSystemCell;
    /***
     * 粒子在被创建时主粒子发射器的时间进度
     */
    public final double structureTime;

    /***
     * 粒子最大生命
     */
    public final int maxLife;
    /***
     * 粒子重力
     */
    public final double gravity;

    /***
     * 开始的移动方位
     * 由形状组件给出可更改
     */
    public V3 startMove;
    /***
     * 位移
     * 模表示速度
     */
    public V3 move;
    /***
     * 起始的颜色
     */
    public final Colour startColour;
    /***
     * 开始时大小
     */
    public final V3 startSize;
    /***
     * 粒子当前生命
     */
    public int life;
    /***
     * 表示粒子是死亡状态
     * 由粒子系统清除
     */
    public boolean isDeath;

    /***
     * 时间进度用0~1表示粒子生命
     */
    public double time;
    /***
     * 上一t的时间进度
     */
    public double oldTime;


    /***
     * 粒子相对于粒子系统的局部坐标系
     */
    public V3 pos;
    /***
     * 粒子相对于粒子系统的旋转
     */
    public Quaternion rotate;
    /***
     * 粒子相对于粒子系统的大小
     */
    public V3 size;
    /***
     * 粒子的颜色
     */
    public Colour colour;
    /***
     * 上t粒子的位置
     */
    public V3 oldPos;
    /***
     * 上t粒子的大小
     */
    public V3 oldSize;
    /***
     * 上t粒子的颜色
     */
    public Colour oldColor;
    /***
     * 上t粒子的旋转
     */
    public Quaternion oldRotate;

    public ParticleCell(ParticleSystemCell particleSystemCell, double time) {
        this.particleSystemCell = particleSystemCell;
        this.structureTime = time;
        MainElement mainElement = particleSystemCell.particleSystem.mainElement;
        this.startSize = mainElement.particleSize.as(time);
        this.startColour = mainElement.particleColour.as(time);

        this.gravity = mainElement.particleGravity.as(time).doubleValue();
        this.rotate = particleSystemCell.particleSystem.shapeElement.getStartRotate(this);
        this.startMove = particleSystemCell.particleSystem.shapeElement.getStartMove(this);
        this.pos = particleSystemCell.particleSystem.shapeElement.getStartPos(this);
        this.rotate = new Quaternion(mainElement.particleRotate.as(time));
        {
            StartSpeedLifeElement element = particleSystemCell.particleSystem.startSpeedLifeElement;
            int maxLife = mainElement.particleLife.as(time).intValue();
            if (element != null) {
                this.maxLife = maxLife * element.multiplyLife.as(element.speedRange.getProportion(move.magnitude())).intValue();
            } else {
                this.maxLife = maxLife;
            }
        }
        writeStart();
        writeOld();
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

    public void up() {
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
        MultiBufferSource multiBufferSource;
        pos = pos.add(startMove);
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

}
