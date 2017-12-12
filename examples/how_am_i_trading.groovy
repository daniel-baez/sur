#!/usr/bin/env sur

def get_all_time_balance(market) {
    def (baseCurrency, quoteCurrency) = market;

    // orders from oldest to actual
    def orders = surbtc.getOrders(market, TRADED).reverse()

    def balance = 0

    for (order in orders) {
        if (order.type == BID) {
            balance -= order.totalExchanged
        } else {
            balance += order.totalExchanged
        }
    }

    return balance
}

for (market in [ETH_CLP, BTC_CLP]) {
    def past = get_all_time_balance(market)

    def balance = surbtc.getBalance(market.baseCurrency)
    def ticker = surbtc.getTicker(market)

    def balanceValued = balance.availableAmount * ticker.lastPrice

    println "market: ${market} $CLP ${past + balanceValued}"
}
