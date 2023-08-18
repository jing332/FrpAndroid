---
title: "visitor 配置"
weight: 4
description: >
  frp visitor 的详细配置说明。
---

## 通用配置

通用配置是指不同类型的 visitor 共同使用的一些配置参数。

### 基础配置

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :--- | :--- | :--- | :--- | :--- | :---|
| role | string | 角色 | 是 | visitor | visitor | visitor 表示访问端 |
| server_user | string | 要访问的 proxy 所属的用户名 | 否 | 当前用户 | | 如果为空，则默认为当前用户 |
| server_name | string | 要访问的 proxy 名称 | 是 | | | |
| type | string | visitor 类型 | 是 | | stcp, sudp, xtcp | |
| sk | string | 密钥 | 是 | | | 服务端和访问端的密钥需要一致，访问端才能访问到服务端 |
| use_encryption | bool | 是否启用加密功能 | 否 | false | | 启用后该代理和服务端之间的通信内容都会被加密传输 |
| use_compression | bool | 是否启用压缩功能 | 否 | false | | 启用后该代理和服务端之间的通信内容都会被压缩传输 |
| bind_addr | string | visitor 监听的本地地址 | 否 | 127.0.0.1 | | 通过访问监听的地址和端口，连接到远端代理的服务 |
| bind_port | int | visitor 监听的本地端口 | 否 | | | 如果为 -1，表示不需要监听物理端口，通常可以用于作为其他 visitor 的 fallback |

## XTCP

| 参数 | 类型 | 说明 | 是否必须 | 默认值 | 可选值 | 备注 |
| :--- | :---: | :--- | :---: | :---: | :--- | :--- |
| keep_tunnel_open | bool | 是否保持隧道打开 | 否 | false | | 如果开启，会定期检查隧道状态并尝试保持打开 |
| max_retries_an_hour | int | 每小时尝试打开隧道的次数 | 否 | 8 | | 仅在 keep_tunnel_open 为 true 时有效 |
| min_retry_interval | int | 重试打开隧道的最小间隔时间，单位: 秒 | 否 | 90 | | 仅在 keep_tunnel_open 为 true 时有效 |
| protocol | string | 隧道底层通信协议 | 否 | quic | quic, kcp | |
| fallback_to | string | 回退到的其他 visitor 名称 | 否 | | | |
| fallback_timeout_ms | int | 连接建立超过多长时间后回退到其他 visitor | 否 | | | |
