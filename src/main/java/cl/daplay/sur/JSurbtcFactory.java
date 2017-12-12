package cl.daplay.sur;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Properties;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cl.daplay.jsurbtc.JSurbtc;

public class JSurbtcFactory {

    public static JSurbtc newInstance() {
        final Stream<Supplier<Optional<JSurbtc>>> strategies = Stream.of(
                JSurbtcFactory::newSurbtcFromConfigFile,
                JSurbtcFactory::newSurbtcFromEnviroment);

        return strategies.map(Supplier::get)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseGet(JSurbtc::new);
    }

    private static Optional<JSurbtc> newSurbtcFromConfigFile() {
        return Optional.of(new File(getEnviromentVariable("user.home")))
                .filter(homeDir -> homeDir.exists() && homeDir.isDirectory())
                .map(homeDir -> new File(homeDir, ".sur/"))
                .filter(surHome -> surHome.exists() && surHome.isDirectory())
                .map(surDir -> new File(surDir, "config.properties"))
                .filter(surConfig -> surConfig.exists() && surConfig.isFile())
                .map(file -> {
                    final Properties properties = new Properties();

                    try {
                        properties.load(new FileReader(file));
                        return properties;
                    } catch (IOException e) {
                        System.err.printf("can't find sur's config file. should be located at $HOME/.sur/config.properties %n");
                        return null;
                    }
                }).flatMap(properties -> {
                    final String key = properties.getProperty("surbtc.key", "");
                    final String secret = properties.getProperty("surbtc.secret", "");

                    final String proxyHost = properties.getProperty("surbtc.proxy.host", "");
                    final String proxyPort = properties.getProperty("surbtc.proxy.port", "");

                    return newSurbtc(key, secret, proxyHost, proxyPort);
                });
    }

    private static Optional<JSurbtc> newSurbtcFromEnviroment() {
        final String key = getEnviromentVariable("SURBTC_KEY");
        final String secret = getEnviromentVariable("SURBTC_SECRET");

        final String proxyHost = getEnviromentVariable("SURBTC_PROXY_HOST");
        final String proxyPort = getEnviromentVariable("SURBTC_PROXY_PORT");

        return newSurbtc(key, secret, proxyHost, proxyPort);
    }

    private static String getEnviromentVariable(String name) {
        final String env = System.getProperty(name);
        return env == null ? "" : env;
    }

    private static Optional<JSurbtc> newSurbtc(String key, String secret, String proxyHost, String proxyPort) {
        if (key.isEmpty() || secret.isEmpty()) {
            return Optional.empty();
        }

        InetSocketAddress httpProxy = null;

        if (!proxyHost.isEmpty() && !proxyPort.isEmpty() && proxyPort.matches("^\\d+$")) {
            httpProxy = new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort, 10));
        }

        final LongSupplier delegate = JSurbtc.newNonce();
        final LongSupplier nonceSupplier = () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return delegate.getAsLong();
        };

        return Optional.of(new JSurbtc(key, secret, nonceSupplier, httpProxy));
    }

}
