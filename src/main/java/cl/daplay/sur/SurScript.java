package cl.daplay.sur;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.math.BigDecimal;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.model.Amount;

import groovy.lang.Script;

public abstract class SurScript extends Script {

    public final JSurbtc surbtc;

    public SurScript() {
        this.surbtc = JSurbtcFactory.INSTANCE.get();
    }

    public final JSurbtc getSurbtc() {
        return surbtc;
    }

    public Instant now() {
        return Instant.now();
    }

    public UUID uuid() {
        return UUID.randomUUID();
    }

    // public String format(String t, Object[] args) {
    //     return String.format(t, args);
    // }

    public String format(String t, BigDecimal value) {
        return value.toString();
    }

    public String format(String t, Amount value) {
        return value.toString();
    }
}
