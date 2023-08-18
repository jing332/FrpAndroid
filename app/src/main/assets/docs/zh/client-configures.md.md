---
title: "客户端配置"
weight: 2
description: >
  frp 客户端的详细配置说明。
---

## 基础配置

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| server_addr | string | 连接服务端的地址 | 0.0.0.0 | | |
| server_port | int | 连接服务端的端口 | 7000 | | |
| nat_hole_stun_server | string | xtcp 打洞所需的 stun 服务器地址 | stun.easyvoip.com:3478 | | |
| connect_server_local_ip | string | 连接服务端时所绑定的本地 IP | | | |
| dial_server_timeout | int | 连接服务端的超时时间 | 10 | | |
| dial_server_keepalive | int | 和服务端底层 TCP 连接的 keepalive 间隔时间，单位秒 | 7200 | | 负数不启用 |
| http_proxy | string | 连接服务端使用的代理地址 | | | 格式为 {protocol}://user:passwd@192.168.1.128:8080 protocol 目前支持 http、socks5、ntlm |
| log_file | string | 日志文件地址 | ./frpc.log | | 如果设置为 console，会将日志打印在标准输出中 |
| log_level | string | 日志等级 | info | trace, debug, info, warn, error | |
| log_max_days | int | 日志文件保留天数 | 3 | | |
| disable_log_color | bool | 禁用标准输出中的日志颜色 | false | | |
| pool_count | int | 连接池大小 | 0 | | |
| user | string | 用户名 | | | 设置此参数后，代理名称会被修改为 {user}.{proxyName}，避免代理名称和其他用户冲突 |
| dns_server | string | 使用 DNS 服务器地址 | | | 默认使用系统配置的 DNS 服务器，指定此参数可以强制替换为自定义的 DNS 服务器地址 |
| login_fail_exit | bool | 第一次登陆失败后是否退出 | true | | |
| protocol | string | 连接服务端的通信协议 | tcp | tcp, kcp, quic, websocket, wss | |
| quic_keepalive_period | int | quic 协议 keepalive 间隔，单位: 秒 | 10 | | |
| quic_max_idle_timeout | int | quic 协议的最大空闲超时时间，单位: 秒 | 30 | | |
| quic_max_incoming_streams | int | quic 协议最大并发 stream 数 | 100000 | | |
| tls_enable | bool | 启用 TLS 协议加密连接 | true | | |
| tls_cert_file | string | TLS 客户端证书文件路径 | | | |
| tls_key_file | string | TLS 客户端密钥文件路径 | | | |
| tls_trusted_ca_file | string | TLS CA 证书路径 | | | |
| tls_server_name | string | TLS Server 名称 | | | 为空则使用 server_addr |
| disable_custom_tls_first_byte | bool | TLS 不发送 0x17 | true | | 当为 true 时，不能端口复用 |
| tcp_mux_keepalive_interval | int | tcp_mux 的心跳检查间隔时间 | 60 | | 单位：秒 |
| heartbeat_interval | int | 向服务端发送心跳包的间隔时间 | 30 | | 建议启用 tcp_mux_keepalive_interval，将此值设置为 -1 |
| heartbeat_timeout | int | 和服务端心跳的超时时间 | 90 | | |
| udp_packet_size | int | 代理 UDP 服务时支持的最大包长度 | 1500 | | 服务端和客户端的值需要一致 |
| start | string | 指定启用部分代理 | | | 当配置了较多代理，但是只希望启用其中部分时可以通过此参数指定，默认为全部启用 |
| meta_xxx | map | 附加元数据 | | | 会传递给服务端插件，提供附加能力 |

## 权限验证

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| authentication_method | string | 鉴权方式 | token | token, oidc | 需要和服务端一致 |
| authenticate_heartbeats | bool | 开启心跳消息鉴权 | false | | 需要和服务端一致 |
| authenticate_new_work_conns | bool | 开启建立工作连接的鉴权 | false | | 需要和服务端一致 |
| token | string | 鉴权使用的 token 值 | | | 需要和服务端设置一样的值才能鉴权通过 |
| oidc_client_id | string | oidc_client_id | | | |
| oidc_client_secret | string | oidc_client_secret | | | |
| oidc_audience | string | oidc_audience | | | |
| oidc_scope | string | oidc_scope | | | |
| oidc_token_endpoint_url | string | oidc_token_endpoint_url | | | |
| oidc_additional_xxx | map | OIDC 附加参数 | | | map 结构，key 需要以 `oidc_additional_` 开头 |

## UI

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| admin_addr | string | 启用 AdminUI 监听的本地地址 | 0.0.0.0 | | |
| admin_port | int | 启用 AdminUI 监听的本地端口 | 0 | | |
| admin_user | string | HTTP BasicAuth 用户名 | | | |
| admin_pwd | string | HTTP BasicAuth 密码 | | | |
| asserts_dir | string | 静态资源目录 | | | AdminUI 使用的资源默认打包在二进制文件中，通过指定此参数使用自定义的静态资源 |
| pprof_enable | bool | 启动 Go HTTP pprof | false | | 用于应用调试 |
