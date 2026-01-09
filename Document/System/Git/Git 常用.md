## 配置SSH

```bash
# 1. 检查是否已有SSH密钥
ls -al ~/.ssh

# 2. 如果没有，生成SSH密钥
ssh-keygen -t ed25519 -C "zhukewei@sonfir.com"
# 或者使用RSA
ssh-keygen -t rsa -b 4096 -C "zhukewei@sonfir.com"

# 3. 启动SSH代理
eval "$(ssh-agent -s)"

# 4. 添加SSH密钥到代理
ssh-add ~/.ssh/id_ed25519

# 5. 复制公钥到剪贴板
cat ~/.ssh/id_ed25519.pub

# 6. 将公钥添加到GitHub/GitLab
# GitHub: Settings → SSH and GPG keys → New SSH key
# GitLab: Preferences → SSH Keys

# 7. 将远程仓库URL改为SSH格式
git remote set-url origin git@github.com:username/repo.git

# 或者重新克隆仓库
git clone git@github.com:username/repo.git
```

## Git多仓库推送

### 添加仓库

```bash
# 添加一个新的远程仓库
git remote set-url --add origin 新的仓库地址
```

### 删除仓库

```bash
git remote set-url --delete origin 仓库地址
```

### 删除全部仓库

```bash
git remote remove origin
```

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

## Git 分支管理

### 基础分支操作
| 命令                            | 说明                              |
| ------------------------------- | --------------------------------- |
| `git branch`                    | 查看本地分支（当前分支前有 * 号） |
| `git branch -a`                 | 查看所有分支（本地+远程）         |
| `git branch -r`                 | 查看远程分支                      |
| `git branch <branch-name>`      | 创建新分支                        |
| `git checkout <branch-name>`    | 切换到分支                        |
| `git switch <branch-name>`      | 切换到分支（Git 2.23+ 推荐）      |
| `git checkout -b <branch-name>` | 创建并切换到新分支                |
| `git switch -c <branch-name>`   | 创建并切换到新分支（Git 2.23+）   |

### 远程分支操作
| 命令                                     | 说明                     |
| ---------------------------------------- | ------------------------ |
| `git push origin <branch-name>`          | 推送本地分支到远程       |
| `git push origin --delete <branch-name>` | 删除远程分支             |
| `git fetch origin`                       | 获取远程分支信息         |
| `git checkout -t origin/<branch-name>`   | 检出远程分支到本地       |
| `git branch -u origin/<branch-name>`     | 设置当前分支跟踪远程分支 |

### 删除分支
| 命令                                    | 说明                       |
| --------------------------------------- | -------------------------- |
| `git branch -d <branch-name>`           | 安全删除分支（已合并）     |
| `git branch -D <branch-name>`           | 强制删除分支（未合并）     |
| `git branch -d -r origin/<branch-name>` | 删除本地记录的远程分支引用 |

### 合并与变基
| 命令                              | 说明                         |
| --------------------------------- | ---------------------------- |
| `git merge <branch-name>`         | 合并指定分支到当前分支       |
| `git merge --no-ff <branch-name>` | 非快进式合并（保留分支历史） |
| `git rebase <branch-name>`        | 变基当前分支到指定分支       |
| `git rebase --abort`              | 终止变基操作                 |
| `git rebase --continue`           | 继续变基操作                 |

### 分支信息与比较
| 命令                              | 说明                 |
| --------------------------------- | -------------------- |
| `git log --oneline --graph --all` | 图形化显示分支历史   |
| `git show-branch`                 | 显示分支提交历史     |
| `git diff <branch1>..<branch2>`   | 比较两个分支的差异   |
| `git log <branch1>..<branch2>`    | 查看分支间的提交差异 |

### 清理与维护
| 命令                      | 说明                         |
| ------------------------- | ---------------------------- |
| `git remote prune origin` | 清理本地已删除的远程分支引用 |
| `git branch --merged`     | 查看已合并到当前分支的分支   |
| `git branch --no-merged`  | 查看未合并到当前分支的分支   |

### 分支重命名
| 命令                                     | 说明                       |
| ---------------------------------------- | -------------------------- |
| `git branch -m <new-name>`               | 重命名当前分支             |
| `git branch -m <old-name> <new-name>`    | 重命名指定分支             |
| `git push origin :<old-name> <new-name>` | 重命名远程分支（先删后推） |

## 实用技巧
1. **查看分支最后一次提交**
   
   ```bash
   git branch -v
   ```
   
2. **批量删除已合并的分支**
   ```bash
   git branch --merged | grep -v "\*" | xargs -n 1 git branch -d
   ```

3. **创建基于远程分支的新分支**
   ```bash
   git checkout -b feature origin/develop
   ```

4. **更新所有远程分支信息**
   ```bash
   git fetch --all --prune
   ```

### 最佳实践建议
- 使用有意义的命名（如 `feature/login`、`bugfix/header`）
- 及时删除已合并的无用分支
- 定期同步远程分支信息（`git fetch --prune`）
- 合并前先拉取最新代码，避免冲突
- 重要分支（如 main、develop）设置保护
