---
title: "服务端配置"
weight: 1
description: >
  frp 服务端详细配置说明。
---

## 基础配置

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| bind_addr | string | 服务端监听地址 | 0.0.0.0 | | |
| bind_port | int | 服务端监听端口 | 7000 | | 接收 frpc 的连接 |
| kcp_bind_port | int | 服务端监听 KCP 协议端口 | 0 | | 用于接收采用 KCP 连接的 frpc |
| quic_bind_port | int | 服务端监听 QUIC 协议端口 | 0 | | 用于接收采用 QUIC 连接的 frpc |
| quic_keepalive_period | int | quic 协议 keepalive 间隔，单位: 秒 | 10 | | |
| quic_max_idle_timeout | int | quic 协议的最大空闲超时时间，单位: 秒 | 30 | | |
| quic_max_incoming_streams | int | quic 协议最大并发 stream 数 | 100000 | | |
| proxy_bind_addr | string | 代理监听地址 | 同 bind_addr | | 可以使代理监听在不同的网卡地址 |
| log_file | string | 日志文件地址 | ./frps.log | | 如果设置为 console，会将日志打印在标准输出中 |
| log_level | string | 日志等级 | info | trace, debug, info, warn, error | |
| log_max_days | int | 日志文件保留天数 | 3 | | |
| disable_log_color | bool | 禁用标准输出中的日志颜色 | false | | |
| detailed_errors_to_client | bool | 服务端返回详细错误信息给客户端 | true | | |
| tcp_mux_keepalive_interval | int | tcp_mux 的心跳检查间隔时间 | 60 | | 单位：秒 |
| tcp_keepalive | int | 和客户端底层 TCP 连接的 keepalive 间隔时间，单位秒 | 7200 | | 负数不启用 |
| heartbeat_timeout | int | 服务端和客户端心跳连接的超时时间 | 90 | | 单位：秒 |
| user_conn_timeout | int | 用户建立连接后等待客户端响应的超时时间 | 10 | | 单位：秒 |
| udp_packet_size | int | 代理 UDP 服务时支持的最大包长度 | 1500 | | 服务端和客户端的值需要一致 |
| tls_cert_file | string | TLS 服务端证书文件路径 | | | |
| tls_key_file | string | TLS 服务端密钥文件路径 | | | |
| tls_trusted_ca_file | string | TLS CA 证书路径 | | | |
| nat_hole_analysis_data_reserve_hours | int | 打洞策略数据的保留时间 | 168 | | |

## 权限验证

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| authentication_method | string | 鉴权方式 | token | token, oidc | |
| authenticate_heartbeats | bool | 开启心跳消息鉴权 | false | | |
| authenticate_new_work_conns | bool | 开启建立工作连接的鉴权 | false | | |
| token | string | 鉴权使用的 token 值 | | | 客户端需要设置一样的值才能鉴权通过 |
| oidc_issuer | string | oidc_issuer | | | |
| oidc_audience | string | oidc_audience | | | |
| oidc_skip_expiry_check | bool | oidc_skip_expiry_check | | | |
| oidc_skip_issuer_check | bool | oidc_skip_issuer_check | | | |

## 管理配置

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| allow_ports | string | 允许代理绑定的服务端端口 | | | 格式为 1000-2000,2001,3000-4000 |
| max_pool_count | int | 最大连接池大小 | 5 | | |
| max_ports_per_client | int | 限制单个客户端最大同时存在的代理数 | 0 | | 0 表示没有限制 |
| tls_only | bool | 只接受启用了 TLS 的客户端连接 | false | | |

## Dashboard, 监控

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| dashboard_addr | string | 启用 Dashboard 监听的本地地址 | 0.0.0.0 | | |
| dashboard_port | int | 启用 Dashboard 监听的本地端口 | 0 | | |
| dashboard_user | string | HTTP BasicAuth 用户名 | | | |
| dashboard_pwd | string | HTTP BasicAuth 密码 | | | |
| dashboard_tls_mode | bool | 是否启用 TLS 模式 | false | | |
| dashboard_tls_cert_file | string | TLS 证书文件路径 | | | |
| dashboard_tls_key_file | string | TLS 密钥文件路径 | | | |
| enable_prometheus | bool | 是否提供 Prometheus 监控接口 | false | | 需要同时启用了 Dashboard 才会生效 |
| asserts_dir | string | 静态资源目录 | | | Dashboard 使用的资源默认打包在二进制文件中，通过指定此参数使用自定义的静态资源 |
| pprof_enable | bool | 启动 Go HTTP pprof | false | | 用于应用调试 |

## HTTP & HTTPS

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| vhost_http_port | int | 为 HTTP 类型代理监听的端口 | 0 | | 启用后才支持 HTTP 类型的代理，默认不启用 |
| vhost_https_port | int | 为 HTTPS 类型代理监听的端口 | 0 | | 启用后才支持 HTTPS 类型的代理，默认不启用 |
| vhost_http_timeout | int | HTTP 类型代理在服务端的 ResponseHeader 超时时间 | 60 | | |
| subdomain_host | string | 二级域名后缀 | | | |
| custom_404_page | string | 自定义 404 错误页面地址 | | | |

## TCPMUX

| 参数 | 类型 | 说明 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :---|
| tcpmux_httpconnect_port | int | 为 TCPMUX 类型代理监听的端口 | 0 | | 启用后才支持 TCPMUX 类型的代理，默认不启用 | 
| tcpmux_passthrough | bool | 是否透传 CONNECT 请求 | false | | 通常在本地服务是 HTTP Proxy 时使用 |
