package com.til.particle_system.client.render;

import com.til.math.Colour;
import com.til.math.Quaternion;
import com.til.math.V3;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.client.cell.IParticleSystemSupport;
import com.til.particle_system.client.cell.ParticleCell;
import com.til.particle_system.element.main.RenderElement;
import com.til.util.Map;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.checker.units.qual.C;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MainParticleSystem.MOD_ID, value = Dist.CLIENT)
public class RenderTypeManage {

    public static final RenderTypeManage RENDER_TYPE_MANAGE = new RenderTypeManage();

    public final Map<Class<?>, IRenderType<?>> MAP = new Map<>();

    public static RenderTypeManage getInstance() {
        return RENDER_TYPE_MANAGE;
    }

    public <E extends RenderElement> void add(Class<E> type, IRenderType<E> iRenderType) {

    }

    public static final IRenderType<RenderElement.TextureRender> TEXTURE_RENDER_I_RENDER_TYPE = (render, particleSystemCell, vertexConsumer, camera, time) -> {
        IParticleSystemSupport systemSupport = particleSystemCell.iParticleSystemSupport;
        V3 cameraPos = new V3(camera.getPosition());
        V3 particleSystemPos = V3.lerp(systemSupport.getPos(), systemSupport.getOldPos(), time);
        V3 renderPos = particleSystemPos.reduce(cameraPos);
        Quaternion particleSystemQuaternion = Quaternion.lerp(systemSupport.getRotate(), systemSupport.getOldRotate(), time);
        V3 particleSystemSize = V3.lerp(systemSupport.getSize(), systemSupport.getOldSize(), time);
        for (ParticleCell particleCell : particleSystemCell.particleCells) {
            V3 particlePos = renderPos.add(V3.lerp(particleCell.pos, particleCell.oldPos, time));
            Quaternion particleQuaternion = particleCell.rotate.multiply(particleSystemQuaternion);
            V3 particleSize = particleCell.size.multiply(particleSystemSize);
            Colour colour = particleCell.colour;
            V3 u1v1 = new V3(-1.0, -1.0, 0.0).multiply(particleSize).transform(particleQuaternion).add(particlePos);
            V3 u1v0 = new V3(-1.0, 1.0, 0.0).multiply(particleSize).transform(particleQuaternion).add(particlePos);
            V3 u0v0 = new V3(1.0, 1.0, 0.0).multiply(particleSize).transform(particleQuaternion).add(particlePos);
            V3 u0v1 = new V3(1.0, -1.0, 0.0).multiply(particleSize).transform(particleQuaternion).add(particlePos);
            float u0 = render.u0;
            float u1 = render.u1;
            float v0 = render.v0;
            float v1 = render.v1;
            float r = (float) colour.r;
            float g = (float) colour.b;
            float b = (float) colour.g;
            float a = (float) colour.a;
            vertexConsumer.vertex(u1v1.x, u1v1.y, u1v1.z).uv(u1, v1).color(r, g, b, a).uv2(0).endVertex();
            vertexConsumer.vertex(u1v0.x, u1v0.y, u1v0.z).uv(u1, v0).color(r, g, b, a).uv2(0).endVertex();
            vertexConsumer.vertex(u0v0.x, u0v0.y, u0v0.z).uv(u0, v0).color(r, g, b, a).uv2(0).endVertex();
            vertexConsumer.vertex(u0v1.x, u0v1.y, u0v1.z).uv(u0, v1).color(r, g, b, a).uv2(0).endVertex();
        }
    };

    static {
        getInstance().add(RenderElement.TextureRender.class, TEXTURE_RENDER_I_RENDER_TYPE);
    }
}
