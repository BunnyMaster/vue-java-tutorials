## Git查看提交次数

要查看Git仓库中的提交次数，可以使用以下几种方法：

**1. 查看所有分支的总提交次数**

```bash
git rev-list --all --count
```

这个命令会返回仓库中所有分支的总提交次数。

**2. 查看当前分支的提交次数**

```bash
git rev-list HEAD --count
```

或者更简单的：

```bash
git log --oneline | wc -l
```

**3. 查看特定分支的提交次数**

```bash
git rev-list 分支名 --count
```

例如查看master分支的提交次数：

```bash
git rev-list master --count
```

**4. 查看某个作者的提交次数**

```bash
git log --author="作者名" --oneline | wc -l
```

**5. 查看一段时间内的提交次数**

```bash
git log --since="2023-01-01" --until="2023-12-31" --oneline | wc -l
```

**6. 查看详细的提交统计（包括每个贡献者的提交次数）**

```bash
git shortlog -s -n
```

这会显示每个贡献者的提交次数，按提交次数从多到少排序。

**7. 查看图形化的提交历史（包括分支情况）**

```bash
git log --oneline --graph --all
```
