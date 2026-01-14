> [!NOTE]
>
> **删除分支不会减少存储容量**
>
> Git 使用以下方式存储内容：
>
> - **Blob**：存储文件内容
> - **Tree**：存储目录结构
> - **Commit**：存储提交信息（指向 tree）
> - **Tag/Branch**：指向特定提交的**指针**

## 删除分支

> [!NOTE]
>
> **不影响文件内容**：Blob 仍然存在
>
> **不影响提交历史**：Commit 仍然存在
>
> **不影响其他分支**：其他分支的内容不受影响
>
> **只删除指针**：仅仅是删除了一个名字引用

```bash
# 删除本地分支
git branch -d feature/old

# 删除远程分支
git push origin --delete feature/old
```

## Git GC

只有同时满足以下条件时，内容才会被删除：

1. **提交没有被任何分支或 tag 引用**
2. **提交不在 reflog 中**（默认 30-90 天）
3. **执行了垃圾回收**

```bash
# 删除不需要的远程分支
git remote prune origin
git branch --merged | grep -v "\*" | xargs -n 1 git branch -d

# 清理本地 reflog
git reflog expire --expire=now --all

# 运行垃圾回收
git gc --prune=now --aggressive

# 删除孤立的提交（危险！）
git prune --expire=now

# 使用垃圾回收（手动触发）
git gc

# 强制垃圾回收
git gc --prune=now
```
