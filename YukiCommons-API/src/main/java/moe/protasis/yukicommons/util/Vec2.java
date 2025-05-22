package moe.protasis.yukicommons.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.api.json.IJsonSerializable;

import java.util.List;

@AllArgsConstructor
@Getter
public class Vec2 implements IJsonSerializable {
    public final double x;
    public final double y;

    public Vec2(List<Double> values) {
        this.x = !values.isEmpty() ? values.get(0) : 0;
        this.y = values.size() > 1 ? values.get(1) : 0;
    }

    public Vec2 Add(Vec2 vec) {
        return Add(vec.x, vec.y);
    }

    public Vec2 Add(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }

    public double Random() {
        return x + Math.random() * (y - x);
    }

    public Vec2 LerpTo(Vec2 vec, double t) {
        return Lerp(this, vec, t);
    }

    public boolean IsInRange(double value) {
        return value >= x && value <= y;
    }

    public double InverseLerp(double value) {
        return MathUtil.InverseLerp(x, y, value);
    }

    public boolean Overlaps(Vec2 other) {
        return x <= other.y && y >= other.x;
    }

    public double Lerp(double t) {
        return MathUtil.Lerp(x, y, t);
    }
    public double Interval() {
        return Math.abs(x - y);
    }

    public List<Double> ToList() {
        return List.of(x, y);
    }

    @Override
    public String toString() {
        return "Vec2(%s, %s)".formatted(x, y);
    }

    public static Vec2 Lerp(Vec2 a, Vec2 b, double t) {
        return new Vec2(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t
        );
    }
}
