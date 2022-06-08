package com.til.math;

import com.til.json_read_write.JsonAnalysis;
import com.til.json_read_write.JsonTransform;
import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.util.List;

/***
 * 用来记录UV信息
 * @author til
 */
@BaseClass(sonClass = UV.class)
@SonClass
@DefaultNew(newExample = UV.class)
public class UV {
    @JsonField
    public float u0;

    @JsonField
    public float u1;

    @JsonField
    public float v0;

    @JsonField
    public float v1;

}
