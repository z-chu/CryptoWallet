# Crypto Wallet Demo

## 项目概述
这是一个加密货币钱包演示项目，主要展示如何实现多钱包管理系统，包括实时价格更新、余额变动监听和资产统计功能。

## 核心实现

### 1. 数据架构设计
- **Room 数据库设计**
  - `CoinEntry`: 币种基础信息
  - `CoinBalanceEntry`: 钱包余额信息
  - `CurrencyRateEntry`: 币种汇率信息
  - 目前 `Wallet` 仅 Mock , 实际可支持多钱包切换

- **实时数据更新机制**
  - `RealtimeRateTransport`: 处理实时汇率更新
  - `RealtimeBalanceTransport`: 处理实时余额更新

### 2. 数据流设计
- **Repository 模式**
  - `WalletDataRepository`: 统一管理数据获取和存储
  - 处理网络请求、本地存储和实时数据分发

- **Provider 设计**
  - `WalletProvider`: 管理当前钱包信息
  - `CoinBalanceProvider`: 处理余额和汇率计算
  - `CoinAssetProvider`: 整合代币信息和余额数据

### 3. UI 架构
- **MVVM 模式**
  - 使用 `LiveData` 实现数据观察
  - `ViewModel` 处理业务逻辑
  - 数据绑定实现 UI 更新

## 技术栈
- Kotlin
- Android Jetpack (LiveData, ViewModel, Room)
- Koin 依赖注入
- Coroutines 异步处理

## 核心流程
1. 钱包数据初始化
2. 实时价格订阅
3. 余额变动监听
4. 资产总额计算
