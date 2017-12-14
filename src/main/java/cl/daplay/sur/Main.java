package cl.daplay.sur;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import cl.daplay.jsurbtc.JSurbtc;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public final class Main {

    public static void main(String[] _args) throws IOException {
        // arguments processing
        boolean unsafe = false;
        String apiKey = "";
        String apiSecret = "";
        String argumentScript = "";

        // remaining arguments will be passed on to script
        final List<String> args = new LinkedList<>();

        // for each argument
        for (int i = 0; i < _args.length; i++) {
            final String arg = _args[i];
            final String nextArgument = (i + 1) < _args.length ? _args[i + 1] : "";

            if ("--api-key".equals(arg)) {
                apiKey = nextArgument; // save next argument
                i++; // advance index to nextArgument's
            } else if ("--api-secret".equals(arg)) {
                apiSecret = nextArgument;
                i++; // advance index to nextArgument's
            } else if ("-e".equals(arg) || "--eval".equals(arg)) {
                argumentScript = nextArgument;
                i++; // advance index to nextArgument's
            } else if ("-U".equals(arg) || "--unsafe".equals(arg)) {
                unsafe = true;
            } else {
                // keep this guy for the script
                args.add(arg);
            }
        }

        // get source of input
        Reader input = new InputStreamReader(System.in);
        String fileName = "inline script";

        if (args.size() >= 1) {
            fileName = args.remove(0);
            input = new FileReader(fileName);
        } else if (!argumentScript.isEmpty()) {
            fileName = "argument script";
            input = new StringReader(argumentScript);
        } else {
            if (System.in.available() == 0) {
                System.exit(1);
            }
        }

        // read input
        final String script = new Scanner(input)
                .useDelimiter("\\Z")
                .next()
                .trim();
        if (script.isEmpty()) {
            return;
        }

        // customized imports
        final ImportCustomizer customizer = new ImportCustomizer();

        for (final Class clazz : Constants.LITERAL_VALUES) {
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
        
        final JSurbtc surbtc = JSurbtcFactory.newInstance(apiKey, apiSecret);
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

        // eval
        try {
            shell.run(script, fileName, args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
