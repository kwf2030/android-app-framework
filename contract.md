android-app-framework
===

## 项目约定（请务必遵守）

1. 遵循MVP架构，所有View与Model不要直接交互（Adapter除外）
2. Model与View的管理由Presenter负责（包括页面更新），Presenter持有Model与View的接口（不是实例），View的更新和事件的处理均有Presenter负责
3. Fragment（特别简单的页面除外）持有Presenter，
4. 遵循单一职责原则，Activity仅负责管理Fragment，不要将业务和View逻辑写到Activity中
5. 请务必删除生成代码时的TODO标签行，严重影响TODO的使用

### 类名

所有类名遵循驼峰规则，使用英文，不要使用拼音（独有的除外）

1. 所有Activity继承自AppActivity，且类名以Activity结尾
2. 所有Fragment继承自AppFragment，且类名以Fragment结尾
3. 所有Presenter继承自AppPresenter，且类名以Presenter结尾

### 资源名

#### Layout布局文件

1. Activity布局文件使用activity_开头，后接功能名称
2. Fragment布局文件使用fragment_开头，后街功能名称
3. 列表项布局文件使用item_开头，后接功能名称
4. 非Fragment的布局资源（即放在Activity的Toolbar和Bottombar之间的全部以content_开头）
5. 需要include的资源请以include_开头，后接功能名称

#### 图片文件

1. 通用分辨率图片请放入mipmap-xhdpi下，资源（XML）请放入drawable下
2. 按钮之类的Selector名称以_selector结尾
3. Selector图片命名以_n（普通）_p（按下）_s（多选框选择）_c（单选框选择）_d（禁用）结尾

#### 其他

1. 颜色已定义好（颜色值查看colors.xml文件），尽量不要再定义，若确实需要，以颜色命名（不要以功能/模块名）
2. 尺寸同上
3. 字符串同上，按字符串内容命名，不要以功能/模块命名
4. 样式同上，style.xml中仅定义可通用的样式，不可通用的不要定义，写在布局中即可

## 项目依赖

- Support-v7

- Support-design

- Dagger2
依赖注入框架，管理项目各种依赖关系

- Requery
数据库框架

- GSON
JSON工具库

- OKHttp3
HTTP网络库

- Retrofit2
Rest API框架（结合OKHttp3使用）

- Rx系列
  - RxJava
  - RxAndroid
  - RxCache

- 测试依赖
  - JUnit
  - Mockito
  - Hamcrest
  - DexMaker
  - Support-TestRunner
  - Support-Espresso

## 包结构

- app
App组件基类及EventBus的简单实现

- data
数据提供者，包括缓存、本地数据存储、远程数据获取（API）

- util
实用工具类

- [_module_]/model
业务逻辑处理，子包按功能分类

- [_module_]/presenter
View与Model的管理者，子包按功能分类

- [_module_]/ui
界面，包括Activity与Fragment及其他的一些UI元素，与业务解耦，子包按功能分类

- [_module_]/view
MVP中的V，ui的接口定义

- [_module_]/viewmodel
UI中用到的实体类，与数据层的model不完全一样
