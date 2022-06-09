package com.til.particle_system.element.main;

import com.til.json_read_write.JsonAnalysis;
import com.til.json_read_write.JsonTransform;
import com.til.json_read_write.annotation.*;
import com.til.math.ITime;
import com.til.math.UV;
import com.til.util.List;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.common.reflection.qual.NewInstance;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Comparator;

/***
 * 粒子的渲染
 * @author til
 */
@BaseClass(sonClass = {RenderElement.EmptyRenderElement.class, RenderElement.TextureRender.class, RenderElement.TextureOnlyZRotate.class})
@DefaultNew(newExample = RenderElement.EmptyRenderElement.class)
public abstract class RenderElement {

    @Nullable
    public abstract ResourceLocation getTexture();

    @SonClass(name = EmptyRenderElement.NAME)
    public static class EmptyRenderElement extends RenderElement {
        public static final String NAME = "empty";

        @Override
        public ResourceLocation getTexture() {
            return null;
        }
    }

    @SonClass(name = TextureRender.NAME)
    @NeedInit(initMethod = "init")
    public static class TextureRender extends RenderElement {

        public static final String NAME = "render";
        /***
         * 贴图路径
         */
        @JsonField
        public String texture;

        @JsonField
        public UVCell.UVCellList timeUV;

        public void init() throws Exception {
            if (timeUV.isEmpty()) {
                throw new Exception((MessageFormat.format("初始化验证[{0}]数据时，timeUV没有元素", this)));
            }
            timeUV = new UVCell.UVCellList(timeUV.stream().sorted(Comparator.comparing(e -> e.time)).toList());
            if (timeUV.get(timeUV.size() - 1).time != 1) {
                throw new Exception((MessageFormat.format("初始化验证[{0}]数据时，timeUV[{1}]尾元素时间值不等于1", this, timeUV)));
            }
        }


        /***
         * 获取UV
         * @param time 时间0~1
         * @return 当前时间的UV
         */
        public UV getUV(double time) {
            for (UVCell uvCell : timeUV) {
                if (uvCell.time >= time) {
                    return uvCell.uv;
                }
            }
            return null;
        }

        @Override
        public @Nullable ResourceLocation getTexture() {
            return new ResourceLocation(texture);
        }


    }

    /***
     * 仅仅选择z轴
     */
    @SonClass(name = TextureOnlyZRotate.NAME)
    @NeedInit(initMethod = "init")
    public static class TextureOnlyZRotate extends RenderElement {
        public static final String NAME = "render_only_z";

        @JsonField
        public String texture;

        @JsonField
        public UVCell.UVCellList timeUV;

        public void init() throws Exception {
            if (timeUV.isEmpty()) {
                throw new Exception((MessageFormat.format("初始化验证[{0}]数据时，timeUV没有元素", this)));
            }
            timeUV = new UVCell.UVCellList(timeUV.stream().sorted(Comparator.comparing(e -> e.time)).toList());
            if (timeUV.get(timeUV.size() - 1).time != 1) {
                throw new Exception((MessageFormat.format("初始化验证[{0}]数据时，timeUV[{1}]尾元素时间值不等于1", this, timeUV)));
            }
        }


        /***
         * 获取UV
         * @param time 时间0~1
         * @return 当前时间的UV
         */
        public UV getUV(double time) {
            for (UVCell uvCell : timeUV) {
                if (uvCell.time >= time) {
                    return uvCell.uv;
                }
            }
            return null;
        }

        @Override
        public @Nullable ResourceLocation getTexture() {
            return new ResourceLocation(texture);
        }

    }

    @BaseClass(sonClass = UVCell.class)
    @SonClass()
    public static class UVCell {

        @JsonField
        public UV uv;

        @JsonField
        public double time;


        @BaseClass(sonClass = UVCellList.class)
        @SonClass(transform = UVCellList.UVListJsonTransform.class)
        @DefaultNew(newExample = UVCellList.class)
        public static class UVCellList extends List<UVCell> {

            public UVCellList() {
                super();
            }

            public UVCellList(Iterable<? extends UVCell> i) {
                super(i);
            }

            public static class UVListJsonTransform extends JsonTransform.ListCurrencyJsonTransform<UVCell, UVCellList> {

                public UVListJsonTransform(Class<UVCellList> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
                    super(type, sonClass, jsonAnalysis);
                }

                @Override
                public Class<UVCell> getElementType() {
                    return UVCell.class;
                }
            }

        }

    }

}
