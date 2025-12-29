# JavaProject4GDUST
广科Java实训周指导 - VSCode Java开发环境配置

## 🚀 快速开始

### 1. 安装JDK
- **版本要求**: JDK 17 或更高版本（推荐LTS版本）
- **下载地址**: [Adoptium (OpenJDK)](https://adoptium.net/zh-CN/temurin/releases?version=17&os=any&arch=any)
- **验证安装**: 打开终端，输入以下命令检查版本
  ```bash
  java --version
  ```

### 2. 安装VSCode Java扩展
在VSCode中安装以下扩展：

1. 打开VSCode扩展面板 (`Ctrl+Shift+X`)
2. 搜索并安装 **"Extension Pack for Java"** (由Microsoft提供)

这个扩展包包含：
- ✅ **Language Support for Java™** - 代码提示、智能补全
- ✅ **Debugger for Java** - 调试功能
- ✅ **Test Runner for Java** - 单元测试支持
- ✅ **Project Manager for Java** - 项目管理
- ✅ **Maven for Java** - Maven项目管理
- ✅ **Gradle for Java** - Gradle项目管理

### 3. 配置项目结构
项目应遵循标准Java项目结构：
```
JavaProject4GDUST/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── Main.java
├── lib/           # 第三方库（如有）
├── .vscode/       # VSCode配置文件
└── README.md
```

### 4. 创建第一个Java程序
在 `src/main/java/com/example/Main.java` 中创建示例代码：

```java src/main/java/com/example/Main.java
package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, 广科Java实训!");
    }
}
```

### 5. 运行Java程序
**方法一：使用VSCode运行按钮**
1. 打开Java文件
2. 点击右上角的 ▶️ **Run** 按钮
3. 或按 `F5` 启动调试

**方法二：使用终端**
```bash
# 编译
javac src/main/java/com/example/Main.java -d bin

# 运行
java -cp bin com.example.Main
```

**方法三：使用运行配置**
在 `.vscode/launch.json` 中添加：

```json .vscode/launch.json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Run Main",
            "request": "launch",
            "mainClass": "com.example.Main",
            "projectName": "JavaProject4GDUST"
        }
    ]
}
```

### 6. 调试Java程序
1. 在代码行号左侧点击设置断点
2. 按 `F5` 开始调试
3. 使用调试工具栏：
   - ▶️ 继续 (`F5`)
   - ⏸️ 暂停
   - ⏭️ 单步跳过 (`F10`)
   - ⬇️ 单步进入 (`F11`)
   - ⬆️ 单步跳出 (`Shift+F11`)

### 7. 常用VSCode快捷键
| 功能 | 快捷键 |
|------|--------|
| 运行程序 | `Ctrl+F5` |
| 调试程序 | `F5` |
| 格式化代码 | `Shift+Alt+F` |
| 快速修复 | `Ctrl+.` |
| 查找引用 | `Shift+F12` |
| 重命名符号 | `F2` |
| 导入包 | `Ctrl+Shift+O` |

### 8. 常见问题解决

**Q: VSCode找不到Java项目？**
A: 确保项目根目录有正确的项目文件：
- Maven项目：`pom.xml`
- Gradle项目：`build.gradle`
- 普通项目：创建 `.vscode/settings.json`

**Q: 如何添加外部库？**
1. 将JAR文件放入 `lib/` 目录
2. 在 `.vscode/settings.json` 中配置：

```json .vscode/settings.json
{
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}
```

**Q: 如何配置Java版本？**
在 `.vscode/settings.json` 中：

```json .vscode/settings.json
{
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-17",
            "path": "C:/Program Files/Java/jdk-17",
            "default": true
        }
    ]
}
```

### 9. 推荐设置
在VSCode设置 (`Ctrl+,`) 中推荐配置：

```json
{
    "java.saveActions.organizeImports": true,
    "editor.formatOnSave": true,
    "java.format.settings.url": ".vscode/java-formatter.xml",
    "java.debug.settings.onBuildFailureProceed": true
}
```

### 10. 学习资源
- [VSCode Java官方文档](https://code.visualstudio.com/docs/languages/java)
- [Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)

---

## 📞 技术支持
如有问题，请检查：
1. JDK是否正确安装并配置环境变量
2. VSCode Java扩展是否安装完整
3. 项目结构是否符合标准
