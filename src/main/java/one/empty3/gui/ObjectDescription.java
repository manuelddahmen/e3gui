package one.empty3.gui;

import one.empty3.library.Representable;

/**
 * Created by manue on 28-06-19.
 */
public class ObjectDescription {
    Class<? extends Representable> r;
    String name;

    public ObjectDescription() {
    }

    public Class<? extends Representable> getR() {
        return r;
    }

    public void setR(Class<Representable> r) {
        this.r = r;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
