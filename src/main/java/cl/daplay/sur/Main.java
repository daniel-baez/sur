package cl.daplay.sur;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Stream;
import cl.daplay.jsurbtc.JSurbtc;

import org.codehaus.groovy.control.CompilerConfiguration;

import cl.daplay.jsurbtc.model.Currency;
import cl.daplay.jsurbtc.model.balance.BalanceEventType;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.OrderPriceType;
import cl.daplay.jsurbtc.model.order.OrderState;
import cl.daplay.jsurbtc.model.order.OrderType;
import cl.daplay.jsurbtc.model.trades.Direction;

import groovy.lang.GroovyShell;

public final class Main {

    private static Predicate<String> isUnsafeOption = (arg) -> {
        return arg != null && ("-U".equals(arg) || "--unsafe".equals(arg));
    };

    public static void main(String[] args) throws IOException {
        // -- remove options
        final boolean unsafe = Stream.of(args)
            .filter(Main.isUnsafeOption)
            .findFirst()
            .map(it -> true)
            .orElse(false);

        if (unsafe) {
            args = Stream.of(args)
                .filter(Main.isUnsafeOption.negate())
                .toArray(String[]::new);
        }

        // check input
        final InputStream input = args.length >= 1 ? new FileInputStream(args[0]) : System.in;
        if (input.available() == 0) {
            return;
        }

        final String fileName = args.length >= 1 ? args[0] : "inline script";

        // read input
        final String script = new Scanner(input)
                .useDelimiter("\\Z")
                .next()
                .trim();
        if (script.isEmpty()) {
            return;
        }

        // compiler settings
        final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(SurScript.class.getName());

        final GroovyShell shell = new GroovyShell(Main.class.getClassLoader(), compilerConfiguration);
        final JSurbtc surbtc = JSurbtcFactory.newInstance();

        if (unsafe) {
            shell.setProperty("surbtc", surbtc);
        } else {
            shell.setProperty("surbtc", new SafeClient(surbtc));
        }

        final Class<Enum>[] enums = new Class[] { ChronoUnit.class,
            Currency.class,
            MarketID.class,
            OrderPriceType.class,
            BalanceEventType.class,
            OrderState.class,
            OrderType.class,
            Direction.class };

        // populate DSL constants
        Arrays.stream(enums).forEach(it -> loadEnum(shell, it));

        // process arguments that look like any of the constants
        final List<String> arguments = new ArrayList<>(args.length);
        for (int i = 1; i < args.length; i++) {
            arguments.add(args[i]);
        }

        // eval
        try {
            shell.run(script, fileName, arguments);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static <T extends Enum<T>> void loadEnum(GroovyShell shell, Class<T> elementType) {
        if (!elementType.isEnum()) {
            return;
        }

        for (final Enum e : elementType.getEnumConstants()) {
            shell.setProperty(e.name(), e);
            shell.setVariable(e.name(), e);
        }
    }

}
