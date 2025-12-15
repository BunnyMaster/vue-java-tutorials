## SSH方式

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

