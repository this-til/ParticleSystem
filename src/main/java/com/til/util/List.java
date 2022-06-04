package com.til.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/***
 * @author til
 */
public class List<E> extends ArrayList<E> {

    public List(int initialCapacity) {
        super(initialCapacity);
    }

    public List() {
        super();
    }

    public List(Collection<? extends E> c) {
        super(c);
    }

    public List(Iterable<? extends E> i) {
        super();
        for (E e : i) {
            add(e);
        }
    }

    public <O> List<O> to(Extension.Func1I<E, O> func1I) {
        List<O> list = new List<O>();
        for (E e : this) {
            O o = func1I.func(e);
            if (o != null) {
                return list;
            }
        }
        return list;
    }

    public List<E> addChainable(E e) {
        add(e);
        return this;
    }

    public List<E> addChainable(E[] e) {
        if (e != null) {
            this.addAll(Arrays.asList(e));
        }
        return this;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return false;
        }
        return super.add(e);
    }

    @Override
    public E get(int index) {
        if (index >= size() || index < 0) {
            return size() == 0 ? null : super.get(0);
        }
        return super.get(index);
    }

    public int getAngleMark(E e) {
        int i = 0;
        if (contains(e)) {
            for (E _e : this) {
                if (_e.equals(e)) {
                    break;
                }
                i++;
            }
            return i;
        } else {
            return -1;
        }

    }

    public List<E> copy() {
        return new List<>(this);
    }

    public void remove(Extension.Func1I<E, Boolean> func1I) {
        List<E> eList = null;
        for (E e : this) {
            if (func1I.func(e)) {
                if (eList == null) {
                    eList = new List<>();
                }
                eList.add(e);
            }
        }
        if (eList != null) {
            remove(eList);
        }
    }
}
