#!/bin/bash

function doc {
    curl -L -o $PWD/../app/src/main/assets/docs/$1/$2 $3
}

doc zh server-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/zh-cn/docs/Reference/server-configures.md
#doc en server-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/en/docs/Reference/server-configures.md

doc zh client-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/zh-cn/docs/Reference/client-configures.md
#doc en client-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/en/docs/Reference/client-configures.md

doc zh proxy.md https://github.com/gofrp/frp-doc/raw/master/content/zh-cn/docs/Reference/proxy.md

doc zh visitor.md https://github.com/gofrp/frp-doc/raw/master/content/zh-cn/docs/Reference/visitor.md