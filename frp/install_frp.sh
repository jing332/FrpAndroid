#!/bin/bash

export frp_dir=$PWD
function build() {
    echo "Building $1 $2 $3 ${PWD}"

    export CGO_ENABLED=1
    export GOOS=android
    export GOARCH="$2"

    FN="lib$1.so"
    rm -f ${FN}
    go build -ldflags "-s -w" -o ${FN}

    mkdir -p ${frp_dir}/../app/libs/$3
    cp -f ${FN} ${frp_dir}/../app/libs/$3
}

cp -f ./frp-*/conf/* ../app/src/main/assets/defaultData

cd ./frp-*/cmd/$1
build $1 $2 $3

# function build_all() {
#     rm -f $1
#     build $1 "arm" "armeabi-v7a"
#     build $1 "arm64" "arm64-v8a"
#     build $1 "386" "x86"
#     build $1 "amd64" "x86_64"
# }

# cd frp-*/cmd
# cd ./frpc
# build_all frpc

# cd ./frps
# build_all frps
