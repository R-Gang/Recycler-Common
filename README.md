# Recycler-Common

## 试图列表组件管理

引入方式：

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

    dependencies {
         implementation 'com.github.R-Gang:Recycler-Common:latest.integration'
    }

## Usage

### 初始化RecyclerView

```
    LayoutManager.instance?.init(this) // 初始化RecyclerView
```

