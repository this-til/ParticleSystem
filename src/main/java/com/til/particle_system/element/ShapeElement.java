package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.main.ParticleSystemCell;

/**
 * 粒子系统的发射形状
 *
 * @author til
 */
public abstract class ShapeElement implements IElement {

    abstract void init(ParticleSystemCell particleSystemCell, ParticleCell particleCell);

}
