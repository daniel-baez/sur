# DISCLAIMER

All of this is experimental, under heavy development, use it at your own risk.

# What is this?

This is a Groovy DSL for Surbtc REST API, once you've configured your key and secret
at `$HOME/.sur/config.properties`, `sur` offers you an scripting environment to build your own
trading strategies

It can also call public endpoint without any configuration, that's why the following works

```bash
./gradlew -q clean installDist && echo 'surbtc.markets.each { println it  }' | PATH=$PATH:$(pwd)/build/install/sur/bin sur
```

# Important

Scripts are executed by default on "safe mode", which means that methods:

- `surbtc.newApiKey`
- `surbtc.newOrder`
- `surbtc.cancelOrder`

will fail, to run in unsafe mode use option `-U` or `--unsafe`

## Examples

- shebang:  `#!/usr/bin/env sur -U`
- `sur --unsafe [script.groovy]`
- `sur -U [script.groovy]`

# HOW AM I USING THIS (for now)

- create file (and directories as needed) `$HOME/.sur/config.properties`
    - On this file place:
        - surbtc.key={{your api key}}
        - surbtc.secret={{your api secret}}
    - You can also OPTIONALLY add
        - surbtc.proxy.host={{proxy host}}
        - surbtc.proxy.port={{proxy port}}
- stand at the dir you clone this repo
- execute: `./gradlew -q clean installDist && PATH=$PATH:$(pwd)/build/install/sur/bin sur {{your_script.sur}}`

There are some example scripts under examples/

