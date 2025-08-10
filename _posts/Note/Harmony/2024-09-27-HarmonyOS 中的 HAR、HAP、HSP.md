---
layout: post
tags: Harmony
---

# HarmonyOS 中的 HAR、HAP、HSP

### 1. **HAR（HarmonyOS Application Runtime）**
- **定义**：HAR 是 HarmonyOS 中的运行时环境，用于运行 HAP 文件。它提供了执行和管理 HAP 所需的基本服务。
- **用途**：HAR 负责处理应用的生命周期、资源管理和与系统服务的交互。

- 每个 HAP 文件在运行时都会依赖 HAR 进行执行，但 HAR 本身不是在安装包中打包的，它是系统提供的运行时环境。

### 2. **HAP（HarmonyOS Ability Package）**
- **定义**：HAP 是 HarmonyOS 应用的基本构件，它包含一个或多个 Ability。HAP 文件是一个压缩包，通常包含应用的代码、资源和配置文件。
- **用途**：开发者将 HAP 部署到 HarmonyOS 设备上，作为完整的应用。

- 一个安装包中通常包含一个或多个 HAP 文件。每个 HAP 文件对应一个或多个 Ability，允许应用实现不同的功能。

### 3. **HSP（HarmonyOS Service Package）**
- **定义**：HSP 是一种服务包，主要用于在 HarmonyOS 中实现服务能力。HSP 文件可以包含服务的定义、实现和相关资源。
- **用途**：HSP 允许应用提供后台服务，供其他应用或系统使用。

- 一个安装包可以包含零个或多个 HSP 文件。如果应用需要提供后台服务，可以包含相应的 HSP 文件。

### 总结
- **HAR** 是运行 HAP 的环境。
- **HAP** 是应用的封装单元。
- **HSP** 是用于服务的封装单元，支持后台服务的开发。
