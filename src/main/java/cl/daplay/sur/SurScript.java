package cl.daplay.sur;

import java.time.Instant;
import java.util.UUID;
import java.math.BigDecimal;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.model.Amount;
import groovy.lang.Script;

public abstract class SurScript extends Script {

    public SurScript() {
    } 

    public Instant now() {
        return Instant.now();
    }

    public UUID uuid() {
        return UUID.randomUUID();
    }

    public String format(String t, BigDecimal value) {
        return value.toString();
    }

    public String format(String t, Amount value) {
        return value.toString();
    }

}
