#!/usr/bin/env sur

/**
 * Prints your last n (by default 10) transactions.
 * usage: sur head.groovy ETH_CLP ETH_PEN 10
 */

def usage() {
    err.println "Usage: sur head.groovy market_name [n]"
    err.println "\texample: sur head.groovy ETH_CLP"
    err.println "\texample: sur head.groovy BTC_CLP 15"
}

def usage_and_exit() {
    usage()
    exit(1)
}

if (!args.length) {
    usage_and_exit()
}

int n = 10;

// check if last argument is number
def last_arg = args.last()
if (last_arg.isNumber()) {
    n = last_arg as Integer
    args = args[0..(args.size() - 2)]
}

for (arg in args) {
    def orders = surbtc.getOrders(arg as MarketID, TRADED)
    println "MarketID: $arg"
    orders.take(n).each { o ->
        println "-   ${o.id}, marketId:${o.marketID}, type: ${o.type}, state: ${o.state}, createdAt:${o.createdAt}, "
        println "    priceType: $o.priceType, limit: $o.limit, "
        println "    amount: $o.amount, actualAmount: $o.actualAmount, exchanged: $o.totalExchanged, rate: $o.exchangeRate "
        println "    feeCurrency: ${o.feeCurrency}, paidFee: $o.paidFee, paidFeeQuoted: ${o.paidFeeQuoted} "
        println ""
    }
}
