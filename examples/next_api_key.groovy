#!/usr/bin/env sur -U

// create params
expiration = now().plus(10, DAYS);
name = "generated-${uuid()}"

// get new api key
apiKey = surbtc.newAPIKey(name, expiration)

println "new key: ${apiKey.id}"
println "new secret.: ${apiKey.secret}"
