package cl.daplay.sur;

import java.math.BigDecimal;
import java.time.Instant;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.JSurbtcException;
import cl.daplay.jsurbtc.model.ApiKey;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.Order;
import cl.daplay.jsurbtc.model.order.OrderPriceType;
import cl.daplay.jsurbtc.model.order.OrderType;

public final class SafeClient extends JSurbtc {

    public SafeClient(JSurbtc delegate) {
        super(delegate);
    }

    @Override
	public ApiKey newAPIKey(final String name, final Instant expiration) throws JSurbtcException {
        throw new JSurbtcException("Safe mode doesn't allow to create an api key");
	}

    @Override
	public Order newOrder(final MarketID marketId,
            final OrderType orderType,
            final OrderPriceType orderPriceType,
            final BigDecimal qty,
            final BigDecimal price) throws JSurbtcException {
        throw new JSurbtcException("Safe mode doesn't allow to create an order.");
	}

    @Override
	public Order cancelOrder(final long orderId) throws JSurbtcException {
        throw new JSurbtcException("Safe mode doesn't allow to cancel order.");
	}
}

