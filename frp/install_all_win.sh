#!/bin/bash

function build() {
    export CC="E:\Android_NDK\android-ndk-r25c\toolchains\llvm\prebuilt\windows-x86_64\bin\\$1-linux-android21-clang.cmd"
    ./install_frpc.sh $2 $3 $4
}

build aarch64 frpc arm64 arm64-v8a
build aarch64 frps arm64 arm64-v8a