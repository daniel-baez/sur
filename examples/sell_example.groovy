#!/usr/bin/env sur

/**
 * Prints the actual value (by ticket and book) for your assets
 * usage: sur current_value.groovy ETH_CLP
 */

si ya tengo una orden ejecutando hacer exit()

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

for (arg in args) {
    def market = arg as MarketID
    def balance = surbtc.getBalance(market.baseCurrency)
    def ticker = surbtc.getTicker(market)
    def orderBook = surbtc.getOrderBook(market)

    println "balance: $balance"
    println "ticker: $ticker"
    println "actual: $balance."

    orderBook.asks.take(5).eachWithIndex { it, index ->
        println "venta: $index $it"
    }

    orderBook.bids.take(5).eachWithIndex { it, index ->
        println "compra: $index $it"
    }
}
