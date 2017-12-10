package cl.daplay.sur.extensions;

import cl.daplay.jsurbtc.model.Amount;

import java.math.BigDecimal;
import java.math.MathContext;

public final class AmountMathExtensions {

    private AmountMathExtensions() {}

    public static Amount minus(final Amount self, final BigDecimal other) {
        return self.subtract(other);
    }

    public static Amount minus(final Amount self, final Amount other) {
        return self.subtract(other);
    }

    public static Amount div(final Amount self, final BigDecimal other) {
        return self.divide(other);
    }

    public static Amount div(final Amount self, final Amount other) {
        return self.divide(other);
    }

}
