package cl.daplay.sur;

import cl.daplay.jsurbtc.model.Currency;
import cl.daplay.jsurbtc.model.balance.BalanceEventType;
import cl.daplay.jsurbtc.model.market.MarketID;
import cl.daplay.jsurbtc.model.order.OrderPriceType;
import cl.daplay.jsurbtc.model.order.OrderState;
import cl.daplay.jsurbtc.model.order.OrderType;
import cl.daplay.jsurbtc.model.trades.Direction;

import java.time.temporal.ChronoUnit;

public interface Constants {

    /**
     * Members of enumerations listed here are directly imported into the script execution environment
     */
    Class<Enum>[] LITERAL_VALUES = new Class[]{
            ChronoUnit.class,
            Currency.class,
            MarketID.class,
            OrderPriceType.class,
            BalanceEventType.class,
            OrderState.class,
            OrderType.class,
            Direction.class
    };

}
