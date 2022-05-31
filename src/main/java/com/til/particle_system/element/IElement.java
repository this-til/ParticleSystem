package com.til.particle_system.element;

import com.til.particle_system.element.main.ParticleCell;
import com.til.particle_system.element.main.ParticleSystemCell;

/***
 * @author til
 */
public interface IElement {

    interface IParticleElement extends IElement {
        void up(ParticleCell particleCell);
    }

}
