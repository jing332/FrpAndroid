#!/bin/bash

TAG_NAME=$(curl -s https://api.github.com/repos/fatedier/frp/releases/latest | grep -o '"tag_name": ".*"' | cut -d'"' -f4)

URL="https://github.com/fatedier/frp/archive/refs/tags/${TAG_NAME}.tar.gz"
echo "Downloading frpc sources ${TAG_NAME} from ${URL}"

curl -L -o "src.tgz" $URL
rm -rf frp-*
tar -xzvf src.tgz
rm -f src.tgz