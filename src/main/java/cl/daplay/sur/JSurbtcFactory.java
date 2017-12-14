package cl.daplay.sur;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import cl.daplay.jsurbtc.JSurbtc;

public final class JSurbtcFactory {

    private JSurbtcFactory () {}

    private static final AtomicReference<Properties> CONFIG = new AtomicReference<>(getProperties());

    /**
     * @return sur's properties... parsed
     */
    private static Properties getProperties() {
        final Properties properties = new Properties();

        return Optional.of(new File(getEnv("user.home")))
                .filter(homeDir -> homeDir.exists() && homeDir.isDirectory())
                .map(homeDir -> new File(homeDir, ".sur/"))
                .filter(surHome -> surHome.exists() && surHome.isDirectory())
                .map(surDir -> new File(surDir, "config.properties"))
                .filter(surConfig -> surConfig.exists() && surConfig.isFile())
                .map(file -> {

                    try {
                        properties.load(new FileReader(file));
                    } catch (IOException e) {
                        System.err.printf("Can't read sur's config file. should be located at $HOME/.sur/config.properties %n");
                        e.printStackTrace();
                    }

                    return properties;
                })
                .orElse(properties);
    }

    public static JSurbtc newInstance(String apiKey, String apiSecret) {
        apiKey = apiKey == null ? "" : apiKey;
        apiSecret = apiSecret == null ? "" : apiSecret;

        apiKey = first(apiKey::toString, property("surbtc.key"), env("SURBTC_KEY"));
        apiSecret = first(apiSecret::toString, property("surbtc.secret"), env("SURBTC_SECRET"));

        String proxyHost = first(property("surbtc.proxy.host"), env("SURBTC_PROXY_HOST"));
        String proxyPort = first(property("surbtc.proxy.port"), env("SURBTC_PROXY_PORT"));

        return newSurbtc(apiKey, apiSecret, proxyHost, proxyPort);
    }

    private static Supplier<String> property(String name) {
        return () -> CONFIG.get().getProperty(name, "");
    }

    private static Supplier<String> env(String name) {
        return () -> getEnv(name);
    }

    /**
     * @param suppliers
     * @return first non empty result
     */
    private static String first(Supplier<String>... suppliers) {
        return Arrays.stream(suppliers)
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .filter(str -> !str.isEmpty())
                .findFirst()
                .orElse("");
    }

    private static String getEnv(String name) {
        final String env = System.getProperty(name);
        return env == null ? "" : env;
    }

    private static JSurbtc newSurbtc(String key, String secret, String proxyHost, String proxyPort) {
        InetSocketAddress httpProxy = null;

        if (!proxyHost.isEmpty() && !proxyPort.isEmpty() && proxyPort.matches("^\\d+$")) {
            httpProxy = new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort, 10));
        }

        return new JSurbtc(key, secret, JSurbtc.newNonce(), httpProxy);
    }

}
