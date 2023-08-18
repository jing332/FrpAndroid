---
title: "代理配置"
weight: 3
description: >
  frp 代理的详细配置说明。
---

## 通用配置

通用配置是指不同类型的代理共同使用的一些配置参数。

### 基础配置

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :--- | :---|
| type | string | 代理类型 | 是 | tcp | tcp, udp, http, https, stcp, sudp, xtcp, tcpmux | |
| use_encryption | bool | 是否启用加密功能 | 否 | false | | 启用后该代理和服务端之间的通信内容都会被加密传输 |
| use_compression | bool | 是否启用压缩功能 | 否 | false | | 启用后该代理和服务端之间的通信内容都会被压缩传输 |
| proxy_protocol_version | string | 启用 proxy protocol 协议的版本 | 否 | | v1, v2 | 如果启用，则 frpc 和本地服务建立连接后会发送 proxy protocol 的协议，包含了原请求的 IP 地址和端口等内容 |
| bandwidth_limit | string | 设置单个 proxy 的带宽限流 | 否 | | | 单位为 MB 或 KB，0 表示不限制，如果启用，会作用于对应的 frpc |
| bandwidth_limit_mode | string | 限流类型，客户端限流或服务端限流 | 否 | client | client, server | |

### 本地服务配置

`local_ip` 和 `plugin` 的配置必须配置一个，且只能生效一个，如果配置了 `plugin`，则 `local_ip` 配置无效。

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| local_ip | string | 本地服务 IP | 是 | 127.0.0.1 | | 需要被代理的本地服务的 IP 地址，可以为所在 frpc 能访问到的任意 IP 地址 |
| local_port | int | 本地服务端口 | 是 | | | 配合 local_ip |
| plugin | string | 客户端插件名称 | 否 | | 见客户端插件的功能说明 | 用于扩展 frpc 的能力，能够提供一些简单的本地服务，如果配置了 plugin，则 local_ip 和 local_port 无效，两者只能配置一个 |
| plugin_params | map | 客户端插件参数 | 否 | | | map 结构，key 需要都以 "plugin_" 开头，每一个 plugin 需要的参数也不一样，具体见客户端插件参数中的内容 |

### 负载均衡和健康检查

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| group | string | 负载均衡分组名称 | 否 | | | 用户请求会以轮询的方式发送给同一个 group 中的代理 |
| group_key | string | 负载均衡分组密钥 | 否 | | | 用于对负载均衡分组进行鉴权，group_key 相同的代理才会被加入到同一个分组中 |
| health_check_type | string | 健康检查类型 | 否 | | tcp,http | 配置后启用健康检查功能，tcp 是连接成功则认为服务健康，http 要求接口返回 2xx 的状态码则认为服务健康 |
| health_check_timeout_s | int | 健康检查超时时间(秒) | 否 | 3 | | 执行检查任务的超时时间 |
| health_check_max_failed | int | 健康检查连续错误次数 | 否 | 1 | | 连续检查错误多少次认为服务不健康 |
| health_check_interval_s | int | 健康检查周期(秒) | 否 | 10 | | 每隔多长时间进行一次健康检查 |
| health_check_url | string | 健康检查的 HTTP 接口 | 否 | | | 如果 health_check_type 类型是 http，则需要配置此参数，指定发送 http 请求的 url，例如 "/health" |

## TCP

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| remote_port | int | 服务端绑定的端口 | 是 | | | 用户访问此端口的请求会被转发到 local_ip:local_port |

## UDP

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| remote_port | int | 服务端绑定的端口 | 是 | | | 用户访问此端口的请求会被转发到 local_ip:local_port |

## HTTP

`custom_domains` 和 `subdomain` 必须要配置其中一个，两者可以同时生效。

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| custom_domains | []string | 服务器绑定自定义域名 | 是(和 subdomain 两者必须配置一个) | | | 用户通过 vhost_http_port 访问的 HTTP 请求如果 Host 在 custom_domains 配置的域名中，则会被路由到此代理配置的本地服务 |
| subdomain | string | 自定义子域名 | 是(和 custom_domains 两者必须配置一个) | | | 和 custom_domains 作用相同，但是只需要指定子域名前缀，会结合服务端的 subdomain_host 生成最终绑定的域名 |
| locations | []string | URL 路由配置 | 否 | | | 采用最大前缀匹配的规则，用户请求匹配响应的 location 配置，则会被路由到此代理 |
| route_by_http_user | string | 根据 HTTP Basic Auth user 路由 | 否 | | | |
| http_user | string | 用户名 | 否 | | | 如果配置此参数，暴露出去的 HTTP 服务需要采用 Basic Auth 的鉴权才能访问 |
| http_pwd | string | 密码 | 否 | | | 结合 http_user 使用 |
| host_header_rewrite | string | 替换 Host header | 否 | | | 替换发送到本地服务 HTTP 请求中的 Host 字段 |
| headers | map | 替换 header | 否 | | | map 中的 key 是要替换的 header 的 key，value 是替换后的内容 |

## HTTPS

`custom_domains` 和 `subdomain` 必须要配置其中一个，两者可以同时生效。

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| custom_domains | []string | 服务器绑定自定义域名 | 是(和 subdomain 两者必须配置一个) | | | 用户通过 vhost_http_port 访问的 HTTP 请求如果 Host 在 custom_domains 配置的域名中，则会被路由到此代理配置的本地服务 |
| subdomain | string | 自定义子域名 | 是(和 custom_domains 两者必须配置一个) | | | 和 custom_domains 作用相同，但是只需要指定子域名前缀，会结合服务端的 subdomain_host 生成最终绑定的域名 |

## STCP

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| role | string | 角色 | 是 | server | server | server 表示服务端 |
| sk | string | 密钥 | 是 | | | 服务端和访问端的密钥需要一致，访问端才能访问到服务端 |
| allow_users | []string | 允许访问的 visitor 用户 | 否 | | | 默认只允许同一用户下的 visitor 访问，配置为 * 则允许任何 visitor 访问 |

## SUDP

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| role | string | 角色 | 是 | server | server,visitor | server 表示服务端，visitor 表示访问端 |
| sk | string | 密钥 | 是 | | | 服务端和访问端的密钥需要一致，访问端才能访问到服务端 |
| allow_users | []string | 允许访问的 visitor 用户 | 否 | | | 默认只允许同一用户下的 visitor 访问，配置为 * 则允许任何 visitor 访问 |

## XTCP

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| role | string | 角色 | 是 | server | server,visitor | server 表示服务端，visitor 表示访问端 |
| sk | string | 密钥 | 是 | | | 服务端和访问端的密钥需要一致，访问端才能访问到服务端 |
| allow_users | []string | 允许访问的 visitor 用户 | 否 | | | 默认只允许同一用户下的 visitor 访问，配置为 * 则允许任何 visitor 访问 |

## TCPMUX

`custom_domains` 和 `subdomain` 必须要配置其中一个，两者可以同时生效。

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| multiplexer | string | 复用器类型 | 是 | | httpconnect | |
| custom_domains | []string | 服务器绑定自定义域名 | 是(和 subdomain 两者必须配置一个) | | | 用户通过 tcpmux_httpconnect_port 访问的 CONNECT 请求如果 Host 在 custom_domains 配置的域名中，则会被路由到此代理配置的本地服务 |
| subdomain | string | 自定义子域名 | 是(和 custom_domains 两者必须配置一个) | | | 和 custom_domains 作用相同，但是只需要指定子域名前缀，会结合服务端的 subdomain_host 生成最终绑定的域名 |
| route_by_http_user | string | 根据 HTTP Basic Auth user 路由 | 否 | | | |
| http_user | string | 用户名 | 否 | | | 如果配置此参数，通过 HTTP CONNECT 建立连接时需要通过 Proxy-Authorization 附加上正确的身份信息 |
| http_pwd | string | 密码 | 否 | | | 结合 http_user 使用 |
