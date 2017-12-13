package cl.daplay.sur;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.model.Currency;
import cl.daplay.jsurbtc.model.balance.BalanceEventType;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.OrderPriceType;
import cl.daplay.jsurbtc.model.order.OrderState;
import cl.daplay.jsurbtc.model.order.OrderType;
import cl.daplay.jsurbtc.model.trades.Direction;
import groovy.lang.Binding;
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

        final Class<Enum>[] enums = new Class[] { ChronoUnit.class,
            Currency.class,
            MarketID.class,
            OrderPriceType.class,
            BalanceEventType.class,
            OrderState.class,
            OrderType.class,
            Direction.class };

        // customized imports
        final ImportCustomizer customizer = new ImportCustomizer();

        for (final Class clazz : enums) {
            for (final Object _member : clazz.getEnumConstants()) {
                final Enum member = (Enum) _member;
                customizer.addStaticImport(member.name(), clazz.getName(), member.name());
            }

            final String fullName = clazz.getName();
            final String alias = clazz.getSimpleName();

            customizer.addImport(alias, fullName);
        }
         
        // compiler settings
        final CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        // script base class
        compilerConfiguration.setScriptBaseClass(SurScript.class.getName());
        compilerConfiguration.addCompilationCustomizers(customizer);
        
        final JSurbtc surbtc = JSurbtcFactory.newInstance();
        final Binding binding = new Binding();

        binding.setProperty("in", System.in);
        binding.setProperty("out", System.out);
        binding.setProperty("err", System.err);
        
        if (unsafe) {
            binding.setProperty("surbtc", surbtc);
        } else {
            binding.setProperty("surbtc", new SafeClient(surbtc));
        }

        final GroovyShell shell = new GroovyShell(Main.class.getClassLoader(),
        		binding,
        		compilerConfiguration);

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

}
