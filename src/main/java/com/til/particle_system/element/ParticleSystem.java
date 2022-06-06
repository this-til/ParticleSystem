package com.til.particle_system.element;


import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.particle_system.element.main.LaunchElement;
import com.til.particle_system.element.main.MainElement;
import com.til.particle_system.element.main.RenderElement;
import com.til.particle_system.element.main.ShapeElement;
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
import com.til.particle_system.element.control.StartSpeedLifeElement;
import com.til.particle_system.element.particle_life_time.speed.LifeTimeSpeedResistanceElement;
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
    public RenderElement renderElement;

    @JsonField
    @Nullable
    public StartSpeedLifeElement startSpeedLifeElement;

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
    public LifeTimeForceElement lifeTimeForceElement;

    @JsonField
    @Nullable
    public LifeTimeSpeedResistanceElement lifeTimeSpeedResistanceElement;

    @JsonField
    @Nullable
    public LifeTimeTrackElement lifeTimeTrackElement;


}
