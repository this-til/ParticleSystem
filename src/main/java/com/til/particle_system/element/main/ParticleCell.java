package com.til.particle_system.element.main;

import com.til.particle_system.element.MainElement;
import com.til.particle_system.util.Colour;
import com.til.particle_system.util.Quaternion;
import com.til.particle_system.util.V2;
import com.til.particle_system.util.V3;

/***
 * 粒子
 * @author til
 */
public class ParticleCell {

    public final ParticleSystemCell particleSystemCell;
    public final double structureTime;
    public int maxLife;
    public int life;
    public boolean isDeath;

    public double time;

    public V3 pos;
    public Quaternion rotate;

    public Quaternion startRotateMove;
    public V3 startSize;
    public V3 startMove;
    public Colour startColor;

    public Quaternion rotateMove;
    public V3 size;
    public V3 move;
    public Colour color;

    public V3 oldPos;
    public V3 oldSize;
    public Colour oldColor;
    public Quaternion oldRotate;

    public ParticleCell(ParticleSystemCell particleSystemCell, double time) {
        this.particleSystemCell = particleSystemCell;
        this.structureTime = time;
        MainElement mainElement = particleSystemCell.particleSystem.get(MainElement.class);
        this.maxLife = mainElement.particleLife.as(time).intValue();
        this.startSize = mainElement.particleSize.as(time);
        this.startRotateMove = new Quaternion(mainElement.particleRotate.as(time));
        this.startMove = mainElement.particleSpeed.as(time);
        this.startColor = mainElement.particleColour.as(time);
        this.pos = new V3();
        this.rotateMove = new Quaternion();
        writeStart();
        writeOld();

    }

    public void up() {
        life++;
        if (life >= maxLife) {
            isDeath = true;
            return;
        }
        writeOld();
        writeStart();
        time = V2.getProportionStatic(0, maxLife, life);
    }

    public void upEnd() {
        pos = pos.add(move);
        rotate = rotate.multiply(rotateMove);
    }

    public void writeOld() {
        oldPos = pos;
        oldSize = size;
        oldColor = color;
        oldRotate = rotate;
    }

    public void writeStart() {
        rotateMove = startRotateMove;
        size = startSize;
        move = startMove;
        color = startColor;
    }


}
