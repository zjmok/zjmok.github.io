---
layout: post
tags: IDE
---

# Android Studio or IDEA 修改代码行的换行长度

1. **打开设置**：
   - 在菜单栏中选择 `File` -> `Settings`（对于 macOS 是 `Android Studio` -> `Preferences`）。

2. **导航到代码风格**：
   - 在设置窗口中，导航到 `Editor` -> `Code Style` -> `Java`（或选择 `Kotlin`，具体取决于你使用的语言）。

3. **调整换行长度**：
   - 在 `Wrapping and Braces` 选项卡中，你可以找到 `Hard wrap at` 字段，默认通常是 120。
   - 修改这个值以设置每行代码的最大长度，超过这个长度时会自动换行。

4. **应用更改**：
   - 点击 `Apply` 然后 `OK` 来保存更改。

### 额外设置

- **设置特定语言**：确保在 Java 和 Kotlin 等不同语言的代码风格中都进行相应的设置。
- **格式化代码**：在修改后，可以通过 `Code` -> `Reformat Code`（快捷键：`Ctrl + Alt + L`）来重新格式化你的代码，使其符合新的换行规则。
