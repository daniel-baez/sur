package cl.daplay.sur;

import java.time.Instant;
import java.util.UUID;
import java.math.BigDecimal;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.model.Amount;
import groovy.lang.Script;

public abstract class UnsafeSurScript extends SurScript {

    private static boolean UNSAFE_MODE = false;

    public UnsafeSurScript() {
        super(UNSAFE_MODE);
    } 

}
