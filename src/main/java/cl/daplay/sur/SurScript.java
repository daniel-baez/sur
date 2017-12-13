package cl.daplay.sur;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.daplay.jsurbtc.model.Amount;

import groovy.lang.Script;

public abstract class SurScript extends Script {
    
    public SurScript() {
    } 

    public void exit(int status) {
        System.exit(status);
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
