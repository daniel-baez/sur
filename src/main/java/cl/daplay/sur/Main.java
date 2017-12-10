package cl.daplay.sur;

import cl.daplay.jsurbtc.Constants;
import cl.daplay.jsurbtc.model.Currency;
import cl.daplay.jsurbtc.model.balance.BalanceEventType;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.Order;
import cl.daplay.jsurbtc.model.order.OrderPriceType;
import cl.daplay.jsurbtc.model.order.OrderState;
import cl.daplay.jsurbtc.model.order.OrderType;
import cl.daplay.jsurbtc.model.trades.Direction;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public final class Main {

    // - procesar los argumentos a constantes para que no haya que hacer eso en el script
    // - bajar siempre la ultima version disponible de la libreria

    public static void main(final String[] args) throws IOException {
        // check input
        final InputStream input = args.length >= 1 ? new FileInputStream(args[0]) : System.in;
        if (input.available() == 0) {
            return;
        }

        final String fileName = args.length >= 1 ? args[0] : "inline script";

        try {
            Class.forName(Order.class.getName());
        } catch (ClassNotFoundException e) {
        }

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
        // shell.run("import java")

        final Class<Enum>[] enums = new Class[] {
                Currency.class,
                MarketID.class,
                OrderPriceType.class,
                BalanceEventType.class,
                OrderState.class,
                OrderType.class,
                Direction.class
        };

        // populate DSL constants
        loadEnums(shell,
                Currency.class,
                MarketID.class,
                OrderPriceType.class,
                BalanceEventType.class,
                OrderState.class,
                OrderType.class,
                Direction.class);

        shell.setVariable("format", Constants.newBigDecimalFormat());

        // process arguments that look like any of the constants
        final List<Object> arguments = new ArrayList<>(args.length);
        for (int i = 1; i < args.length; i++) {
            arguments.add(args[i]);
        }

        // eval the script
        try {
            shell.run(script, fileName, arguments);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <T extends Enum<T>> Optional<T> parse(String name, Class<T>[] enums) {
        for (Class<T> e : enums) {
            try {
                return Optional.of(Enum.valueOf(e, name));
            } catch (IllegalArgumentException ex) {
                return Optional.empty();
            }
        }

        return Optional.empty();
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

    private static void loadEnums(GroovyShell shell, Class first, Class... more) {
        final List<Class> all = new ArrayList<>(1 + more.length);
        all.add(first);
        all.addAll(Arrays.asList(more));

        all.forEach(elementType -> loadEnum(shell, elementType));
    }

}
