package com.til.particle_system.element;


import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.particle_system.element.main.LaunchElement;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.main.ShapeElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeColourElement;
import com.til.particle_system.element.particle_life_time.colour.LifeTimeSpeedColourElement;
import com.til.particle_system.element.particle_life_time.rotate.LifeTimeRotateElement;
import com.til.particle_system.element.particle_life_time.rotate.LifeTimeSpeedRotateElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSizeElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSpeedSizeElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedExtendElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedLimitElement;
import com.til.particle_system.element.system.StartSpeedLifeElement;
import org.jetbrains.annotations.Nullable;

/**
 * 粒子系统原数据
 *
 * @author til
 */
@BaseClass(sonClass = ParticleSystem.class)
@SonClass()
public class ParticleSystem {

    @JsonField
    public MainElement mainElement;

    @JsonField
    public LaunchElement launchElement;

    @JsonField
    public ShapeElement shapeElement;

    @JsonField
    @Nullable
    public LifeTimeColourElement lifeTimeColourElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedColourElement lifeTimeSpeedColourElement;

    @JsonField
    @Nullable
    public LifeTimeSizeElement lifeTimeSizeElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedSizeElement lifeTimeSpeedSizeElement;

    @JsonField
    @Nullable
    public LifeTimeRotateElement lifeTimeRotateElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedRotateElement lifeTimeSpeedRotateElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedElement lifeTimeSpeedElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedLimitElement lifeTimeSpeedLimitElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedExtendElement lifeTimeSpeedExtendElement;

    @JsonField
    @Nullable
    public StartSpeedLifeElement startSpeedLifeElement;


}
