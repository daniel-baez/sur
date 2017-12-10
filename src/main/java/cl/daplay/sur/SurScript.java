package cl.daplay.sur;

import cl.daplay.jsurbtc.JSurbtc;
import groovy.lang.Script;

public abstract class SurScript extends Script {

    public final JSurbtc surbtc;

    public SurScript() {
        this.surbtc = JSurbtcFactory.INSTANCE.get();
    }

    public final JSurbtc getSurbtc() {
        return surbtc;
    }
}
