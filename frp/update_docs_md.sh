#!/bin/bash

function doc {
    curl -L -o ../app/src/main/assets/docs/$1/$2.md $3
}

doc zh server-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/zh-cn/docs/Reference/server-configures.md
#doc en server-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/en/docs/Reference/server-configures.md

doc zh client-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/zh-cn/docs/Reference/client-configures.md
#doc en client-configures.md https://raw.githubusercontent.com/gofrp/frp-doc/master/content/en/docs/Reference/client-configures.md

