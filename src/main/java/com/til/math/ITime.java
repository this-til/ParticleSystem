package com.til.math;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.til.json_read_write.annotation.*;
import com.til.json_read_write.JsonAnalysis;
import com.til.json_read_write.JsonTransform;
import com.til.util.Util;
import com.til.util.List;
import com.til.util.UseString;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;

/**
 * @author til
 */
public interface ITime<E> {

    /***
     * 生成
     * @param time 0~1之间一般表示粒子的生命周期或者系统的生命周期
     * @return 随机生成的值
     */
    E as(double time);

    @BaseClass(sonClass = {ITimeNumber.NumberFinal.class, ITimeNumber.RandomNumber.class, ITimeNumber.Curve.class})
    @DefaultNew(newExample = ITimeNumber.NumberFinal.class)
    interface ITimeNumber extends ITime<Number> {
        @SonClass(transform = NumberFinal.NumberFinalJsonTransform.class)
        class NumberFinal implements ITimeNumber {

            public Number number = 0;

            @Override
            public Number as(double time) {
                return number;
            }

            public static class NumberFinalJsonTransform extends JsonTransform<NumberFinal> {
                public NumberFinalJsonTransform(Class<NumberFinal> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
                    super(type, sonClass, jsonAnalysis);
                }

                @Override
                public JsonElement from(NumberFinal numberFinal) throws Exception {
                    return new JsonPrimitive(numberFinal.number);
                }

                @Override
                public NumberFinal as(JsonElement jsonElement) throws Exception {
                    NumberFinal numberFinal = new NumberFinal();
                    numberFinal.number = jsonElement.getAsNumber();
                    return numberFinal;
                }
            }
        }

        @SonClass(name = UseString.RANDOM)
        class RandomNumber implements ITimeNumber {
            @JsonField
            public ITimeNumber max;
            @JsonField
            public ITimeNumber min;
            public Random random = new Random();

            @Override
            public Number as(double time) {
                double a = max.as(time).doubleValue();
                double b = min.as(time).doubleValue();
                double max = Math.max(a, b);
                double min = Math.min(a, b);
                return random.nextDouble(max, min);
            }
        }

        @SonClass(name = UseString.CURVE)
        @NeedInit(initMethod = {"init"})
        class Curve implements ITimeNumber {

            @JsonField
            public CurvePosList posList;

            public List<CurveSection> curveSectionList;

            public void init() throws Exception {
                this.curveSectionList = new List<>();
                if (posList.isEmpty()) {
                    throw new Exception("初始化曲线时传入List为空");
                }
                if (posList.size() == 1) {
                    curveSectionList.add(new CurveSection(null, 0, 1) {
                        @Override
                        public V2 cubicBezier(double t) {
                            return posList.get(0).mainPos;
                        }
                    });
                    return;
                }
                CurvePos oldCurvePos = null;
                for (CurvePos curvePos : posList) {
                    if (oldCurvePos == null) {
                        oldCurvePos = curvePos;
                        continue;
                    }
                    if (oldCurvePos.afterAnchorPoint.isEmpty() && curvePos.frontAnchorPoint.isEmpty()) {
                        V2 spacing = curvePos.mainPos.reduce(oldCurvePos.mainPos);
                        curveSectionList.add(new CurveSection(null, oldCurvePos.mainPos.x, curvePos.mainPos.x - oldCurvePos.mainPos.y) {
                            @Override
                            public V2 cubicBezier(double t) {
                                return spacing.multiply(t);
                            }
                        });
                        return;
                    }
                    curveSectionList.add(new CurveSection(new V2[]{
                            oldCurvePos.mainPos,
                            oldCurvePos.mainPos.add(oldCurvePos.afterAnchorPoint),
                            curvePos.mainPos.add(oldCurvePos.frontAnchorPoint),
                            curvePos.mainPos
                    }, oldCurvePos.mainPos.x, curvePos.mainPos.x - oldCurvePos.mainPos.y));
                }
            }

            @Override
            public Number as(double time) {
                for (CurveSection curveSection : curveSectionList) {
                    if (curveSection.start < time && curveSection.start + curveSection.deviation > time) {
                        return curveSection.cubicBezier((time - curveSection.start) / curveSection.deviation).y;
                    }
                }
                return 0;
            }

            public static class CurveSection {

                public final V2[] pos;
                /***
                 * 主线起始点
                 */
                public final double start;

                /***
                 * 偏移量
                 */
                public final double deviation;

                public CurveSection(V2[] pos, double start, double deviation) {
                    this.pos = pos;
                    this.start = start;
                    this.deviation = deviation;
                }

                /***
                 * 通过循环 返回当t为某个值时，返回Bezier曲线上这个点
                 * @param t 0~1表示曲线位置
                 */
                public V2 cubicBezier(double t) {
                    V2[] temp = new V2[pos.length];
                    System.arraycopy(pos, 0, temp, 0, pos.length);
                    for (int i = 0; i < pos.length - 1; i++) {
                        for (int j = 0; j < pos.length - i - 1; j++) {
                            double x = (1 - t) * temp[j].x + t * temp[j + 1].x;
                            double y = (1 - t) * temp[j].y + t * temp[j + 1].y;
                            temp[j] = new V2(x, y);
                        }
                    }
                    return temp[0];
                }
            }

            /***
             * 曲线中的一个节点
             */
            @BaseClass(sonClass = CurvePos.class)
            @SonClass()
            @NeedInit(initMethod = "verification")
            public static class CurvePos {
                /***
                 * 主点位
                 */
                @JsonField
                public V2 mainPos;
                /***
                 * 前锚点
                 * 以主点位为中心
                 */
                @JsonField
                public V2 frontAnchorPoint;
                /***
                 * 后锚点
                 * 以主点位为中心
                 */
                @JsonField
                public V2 afterAnchorPoint;

                /***
                 * 验证点是不是正确的
                 * 如果错误将报错
                 */
                public void verification() throws Exception {
                    if (mainPos.x < 0 && mainPos.x > 0) {
                        throw new Exception(MessageFormat.format("检验[{0}]时，mainPos为的x越界，应该在[0,1]之间", Util.toString(this)));
                    }
                    if (frontAnchorPoint.x > 0) {
                        throw new Exception(MessageFormat.format("检验[{0}]时，frontAnchorPoint越界，应该在(-∞,0]之间", Util.toString(this)));

                    }
                    if (afterAnchorPoint.x < 0) {
                        throw new Exception(MessageFormat.format("检验[{0}]时，mainPos为[{}]，他的x越界，应该在[0,+∞)之间", Util.toString(this)));
                    }
                }
            }

            /***
             * 曲线点位的列表
             */
            @BaseClass(sonClass = CurvePosList.class)
            @SonClass(transform = CurvePosList.CurvePosListTransform.class)
            public static class CurvePosList extends List<CurvePos> {

                public CurvePosList() {
                    super();
                }

                public CurvePosList(Collection<? extends CurvePos> c) {
                    super(c);
                }

                public static class CurvePosListTransform extends JsonTransform.ListCurrencyJsonTransform<CurvePos, CurvePosList> {

                    public CurvePosListTransform(Class<CurvePosList> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
                        super(type, sonClass, jsonAnalysis);
                    }

                    @Override
                    public CurvePosList as(JsonElement jsonElement) throws Exception {
                        CurvePosList curvePosList = super.as(jsonElement);
                        curvePosList = new CurvePosList(curvePosList.stream().sorted(Comparator.comparing(e -> e.mainPos.x)).toList());
                        if (curvePosList.size() > 1) {
                            if (curvePosList.get(0).mainPos.x != 0) {
                                throw new Exception(MessageFormat.format("首点[{0}]，mainPos横坐标不为0", Util.toString(curvePosList.get(0))));
                            }
                            if (curvePosList.get(curvePosList.size() - 1).mainPos.x != 1) {
                                throw new Exception(MessageFormat.format("尾点[{0}]，mainPos横坐标不为1", Util.toString(curvePosList.get(curvePosList.size() - 1))));
                            }
                        }
                        return curvePosList;
                    }

                    @Override
                    public Class<CurvePos> getElementType() {
                        return CurvePos.class;
                    }
                }

            }
        }
    }

    @BaseClass(sonClass = {ITimeV3.DefaultTimeV3.class, ITimeV3.NoSeparationTimeV3.class})
    @DefaultNew(newExample = ITimeV3.DefaultTimeV3.class)
    interface ITimeV3 extends ITime<V3> {

        @SonClass()
        class DefaultTimeV3 implements ITimeV3 {
            @JsonField
            public ITimeNumber x;
            @JsonField
            public ITimeNumber y;
            @JsonField
            public ITimeNumber z;

            @Override
            public V3 as(double time) {
                return new V3(x.as(time).doubleValue(), y.as(time).doubleValue(), z.as(time).doubleValue());
            }

        }

        @SonClass(name = UseString.NO_SEPARATION)
        class NoSeparationTimeV3 implements ITimeV3 {
            @JsonField
            public ITimeNumber value;

            @Override
            public V3 as(double time) {
                return new V3(value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue());
            }

        }
    }

    @BaseClass(sonClass = {ITimeV2.DefaultTimeV2.class, ITimeV2.NoSeparationTimeV2.class})
    @DefaultNew(newExample = ITimeV2.DefaultTimeV2.class)
    interface ITimeV2 extends ITime<V2> {

        @SonClass()
        class DefaultTimeV2 implements ITimeV2 {
            @JsonField
            public ITimeNumber x;
            @JsonField
            public ITimeNumber y;

            @Override
            public V2 as(double time) {
                return new V2(x.as(time).doubleValue(), y.as(time).doubleValue());
            }
        }

        @SonClass(name = UseString.NO_SEPARATION)
        class NoSeparationTimeV2 implements ITimeV2 {

            @JsonField
            public ITimeNumber value;

            @Override
            public V2 as(double time) {
                return new V2(value.as(time).doubleValue(), value.as(time).doubleValue());
            }
        }
    }

    @BaseClass(sonClass = {ITimeColour.DefaultTimeColour.class, ITimeColour.NoSeparationTimeColour.class})
    @DefaultNew(newExample = ITimeColour.DefaultTimeColour.class)
    interface ITimeColour extends ITime<Colour> {

        @SonClass(name = UseString.VALUE)
        class DefaultTimeColour implements ITimeColour {

            @JsonField
            public ITimeNumber r;
            @JsonField
            public ITimeNumber g;
            @JsonField
            public ITimeNumber b;
            @JsonField
            public ITimeNumber a;

            @Override
            public Colour as(double time) {
                return new Colour(r.as(time).doubleValue(), g.as(time).doubleValue(), b.as(time).doubleValue(), a.as(time).doubleValue());
            }

        }

        @SonClass(name = UseString.NO_SEPARATION)
        class NoSeparationTimeColour implements ITimeColour {
            @JsonField
            public ITimeNumber value;

            @Override
            public Colour as(double time) {
                return new Colour(value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue());
            }

        }


    }

}
