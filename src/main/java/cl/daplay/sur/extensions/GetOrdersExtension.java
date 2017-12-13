package cl.daplay.sur.extensions;

import cl.daplay.jsurbtc.JSurbtc;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.Order;
import cl.daplay.jsurbtc.model.order.OrderState;
import cl.daplay.jsurbtc.model.order.OrderType;
import cl.daplay.jsurbtc.model.trades.Direction;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class GetOrdersExtension {

    private GetOrdersExtension() {}

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final OrderState orderState,
                                        final BigDecimal minimunExchanged,
                                        final Direction direction) throws Exception {
        final OrderType orderType = direction == Direction.BUY ? OrderType.BID : OrderType.ASK;

        return surbtc.getOrders(marketID, orderState, minimunExchanged)
                .stream()
                .filter(order -> orderType == null || order.getType() == orderType)
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final OrderState orderState,
                                        final Direction direction) throws Exception {
        final OrderType orderType = direction == Direction.BUY ? OrderType.BID : OrderType.ASK;

        return surbtc.getOrders(marketID, orderState)
                .stream()
                .filter(order -> orderType == null || order.getType() == orderType)
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final BigDecimal minimunExchanged,
                                        final Direction direction) throws Exception {
        final OrderType orderType = direction == Direction.BUY ? OrderType.BID : OrderType.ASK;

        return surbtc.getOrders(marketID, minimunExchanged)
                .stream()
                .filter(order -> orderType == null || order.getType() == orderType)
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final BigDecimal minimunExchanged) throws Exception {
        return surbtc.getOrders(marketID, minimunExchanged)
                .stream()
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final OrderState orderState) throws Exception {
        return surbtc.getOrders(marketID, orderState)
                .stream()
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final BigDecimal minimunExchanged,
                                        final OrderType orderType) throws Exception {
        return surbtc.getOrders(marketID, minimunExchanged)
                .stream()
                .filter(order -> orderType == null || order.getType() == orderType)
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final OrderState orderState,
                                        final OrderType orderType) throws Exception {
        return surbtc.getOrders(marketID, orderState)
                .stream()
                .filter(order -> orderType == null || order.getType() == orderType)
                .collect(toList());
    }

    public static List<Order> getOrders(final JSurbtc surbtc,
                                        final MarketID marketID,
                                        final OrderState orderState,
                                        final BigDecimal minimunExchanged,
                                        final OrderType orderType) throws Exception {
        return surbtc.getOrders(marketID, orderState, minimunExchanged)
                .stream()
                .filter(order -> orderType == null || order.getType() == orderType)
                .collect(toList());
    }

}
