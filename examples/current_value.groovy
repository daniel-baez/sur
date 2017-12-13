#!/usr/bin/env sur

/**
 * Prints the actual value (by ticket and book) for your assets
 * usage: sur current_value.groovy ETH_CLP
 */

def usage() {
    err.println "Usage: sur current_value.groovy market_name"
    err.println "\texample: sur current_value.groovy ETH_CLP"
}

def usage_and_exit() {
    usage()
    exit(1)
}

if (!args.length) {
    usage_and_exit()
}

/**
 * given a collection of orders and a amount
 * returns a collection of the orders need to add up to amount
 */
def getRelevantOrders(orders, limit) {
    def current = 0

    return orders.takeWhile { order ->
        current += order.actualAmount
        return current <= limit;
    }
}

def fee(x) {
    x * 0.993
}

def report(order, price) {
    println "- Order $order.id ($order.actualAmount) was aquired for $order.totalExchanged, and now worths: ${fee(order.actualAmount * price)}"
}

for (arg in args) {
    def market = arg as MarketID
    def balance = surbtc.getBalance(market.baseCurrency)
    def ticker = surbtc.getTicker(market)

    def orders = getRelevantOrders(surbtc.getOrders(market, TRADED, BUY), balance.availableAmount)

    for (order in orders) {
        report(order, ticker.maxBid)
    }
}
