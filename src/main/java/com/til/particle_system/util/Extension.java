package com.til.particle_system.util;

/**
 * @author til
 */
public class Extension {

    public interface Action {

        Action empty = () -> {
        };

        void action();
    }

    public interface Action1V<A> {
        void action(A a);
    }

    public interface Action2V<A, B> {
        void action(A a, B b);
    }

    public interface Action3V<A, B, C> {
        void action(A a, B b, C c);
    }

    public interface Action4V<A, B, C, D> {
        void action(A a, B b, C c, D d);
    }

    public interface Action5V<A, B, C, D, E> {
        void action(A a, B b, C c, D d, E e);
    }

    public interface Action6V<A, B, C, D, E, F> {
        void action(A a, B b, C c, D d, E e, F f);
    }

    public interface Func<O> {
        O func();
    }

    public interface Func1I<I, O> {
        O func(I i);
    }

    public interface Func2I<I1, I2, O> {
        O func(I1 i1, I2 i2);
    }

    public static class Data<D> {
        D d;

        public Data(D d) {
            this.d = d;
        }
    }

    public static class Data2<D1, D2> {
        D1 d1;
        D2 d2;

        public Data2(D1 d1, D2 d2) {
            this.d1 = d1;
            this.d2 = d2;
        }
    }


    public static class Data3<D1, D2, D3> {
        D1 d1;
        D2 d2;
        D3 d3;

        public Data3(D1 d1, D2 d2, D3 d3) {
            this.d1 = d1;
            this.d2 = d2;
            this.d3 = d3;
        }
    }

}
