package cl.daplay.sur.extensions;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.model.Ticker;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.Order;
import cl.daplay.jsurbtc.model.order.OrderState;
import cl.daplay.jsurbtc.model.order.OrderType;
import cl.daplay.jsurbtc.model.trades.Direction;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class GetTickerExtension {

    private GetTickerExtension() {}

    public static Ticker getOrders(final JSurbtc surbtc, final String marketID) throws Exception {
        return surbtc.getTicker(MarketID.valueOf(marketID));
    }

}
