package com.til.particle_system.element.cell;

import com.til.json_read_write.util.math.Colour;
import com.til.json_read_write.util.math.Quaternion;
import com.til.json_read_write.util.math.V2;
import com.til.json_read_write.util.math.V3;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeColourElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeSpeedColourElement;
import com.til.particle_system.element.particle_life_time.move.LifeTimeForceElement;
import com.til.particle_system.element.particle_life_time.rotate.LifeTimeRotateElement;
import com.til.particle_system.element.particle_life_time.rotate.LifeTimeSpeedRotateElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSizeElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSpeedSizeElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedExtendElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedLimitElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedResistanceElement;

/***
 * 粒子
 * @author til
 */
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
        //this.startSpeed = mainElement.particleSpeed.as(time).doubleValue();
        this.startColour = mainElement.particleColour.as(time);
        this.maxLife = mainElement.particleLife.as(time).intValue();
        this.rotate = particleSystemCell.particleSystem.shapeElement.getStartRotate(this);
        this.startMove = particleSystemCell.particleSystem.shapeElement.getStartMove(this);
        this.pos = particleSystemCell.particleSystem.shapeElement.getStartPos(this);
        this.rotate = new Quaternion(mainElement.particleRotate.as(time));


        writeStart();
        writeOld();
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
            /*{
                LifeTimeSpeedExtendElement element = particleSystemCell.particleSystem.lifeTimeSpeedExtendElement;
                if (element != null) {
                    switch (element.extendType) {
                        case ADD -> startMove.add(element.extend.as(time));
                        case MULTIPLY -> startMove.multiply(element.extend.as(time));
                    }
                    move = startMove;
                }
            }*/
            {
                LifeTimeForceElement element = particleSystemCell.particleSystem.lifeTimeForceElement;
                if (element != null) {
                    startMove = startMove.add(element.force.as(time));
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
                    move.add(move.multiply(-resistance));
                }
            }
            {
                LifeTimeSpeedElement element = particleSystemCell.particleSystem.lifeTimeSpeedElement;
                if (element != null) {
                    move = move.multiply(element.speed.as(time));
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
