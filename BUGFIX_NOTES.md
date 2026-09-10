# 消灭泡泡糖 - 下落动画 Bug 修复记录

> 最终无 bug 版本。本文档记录本次调优过程中遇到的所有问题、根因与解决方案。

## 最终架构

- **数据层** `StarServiceImpl`：`createStars` / `tobeClearedStars` / `getYMovedStars`，纯逻辑，不碰界面。
- **界面层** `MainForm`：负责把 `Star` 渲染成 `Label`、处理点击、播放动画。
- **关联方式（关键）**：`Map<Star, Label> mStarLabels` 用**对象引用**把数据和界面控件绑定，`Label.setUserData(star)` 直接存星星对象。**坐标只用于摆放位置，不再用于"查找身份"。**

## 遇到的 Bug 及根因

### 1. 能消的消不了 / 点一个消一片 / 单点也能消
**根因**：界面与数据模型错位。点击时用 Label 的 `userData`（"行;列"字符串）去 `mCurretStars.getStar(r,c)` 查找，一旦 Label 的位置信息和星星实际 position 不同步，就会查到错的星或查不到。

### 2. 最上面的星直接掉到被消位置，中间的没动
**根因**：移动循环里"按旧 ID 查找 Label"和"改 Label 的 ID"混在一起。处理星A时把它的 Label ID 从 `#s0c` 改成 `#s1c`，处理星B时按旧 ID `#s1c` 查找，命中的是刚改名的星A的 Label → 星A 被挂了两个动画（位移叠加掉到底），星B 没人动。

### 3. 消过的列再点就出问题
**根因**：第一遍循环里用 `mCurretStars.getStar(oldR, oldC)` 查数据星星后**立刻** `setPosition(新位置)`。`getStar` 是按 position 线性匹配的，前一颗星 position 改完后，后一颗星按旧位置查找会命中前一颗 → 数据模型 position 被错改/漏改，颜色与位置错位，后续泛洪消除全乱。

### 4.（已回退的错误尝试）用 maxRow 限制下落范围
**教训**：`getYMovedStars` 里数空位到 `MAX_ROW_SIZE` 在满棋盘下是对的；试图用"扫描实际最大行号"来修残局反而帮倒忙。**算法本身没问题，问题一直在应用层的查找时机。**

## 核心教训（同一个病根，犯了三次）

> **绝不能在"遍历/查找"的过程中修改"被用来查找的字段"。**

三次 bug（ID 错挂、position 污染、坐标撞车）本质都是：循环里一边用某个字段（ID / position）做查找，一边又在循环里修改它，导致后续查找命中错误目标。

**解法（两遍分离模式）**：
1. 第一遍循环：纯查找，把所有目标对象（Label / 数据星星）按旧状态找出来存进列表，**不做任何修改**。
2. 第二遍循环：统一应用修改（setPosition / 改 ID / 播动画）。

## 方案演进

| 版本 | 关联方式 | 结果 |
|---|---|---|
| v1 | Label ID + 坐标字符串 userData | 动画错挂、数据错乱 |
| v2 | 两遍循环分离查找与修改 | 好转但仍依赖坐标查找，撞车 |
| **v3（最终）** | **Map 对象引用关联，坐标仅用于布局** | **无 bug** |

## 关键代码位置

- 点击处理与下落动画：`src/cn/campsg/practical/bubble/MainForm.java` 的 `initGameStars` 内 `starFrame.setOnMouseClicked`
- 下落位置计算：`src/cn/campsg/practical/bubble/service/StarServiceImpl.java` 的 `getYMovedStars`
- 连通块消除查找：同文件 `tobeClearedStars` / `lookupByPath`
