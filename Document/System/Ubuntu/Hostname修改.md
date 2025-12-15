## 方法一：使用 `hostnamectl` 命令（推荐）

这是 Ubuntu 17.04 及以后版本最推荐的方法：

```bash
# 查看当前主机名
hostnamectl

# 临时修改主机名（重启后失效）
sudo hostnamectl set-hostname 新主机名 --transient

# 永久修改静态主机名
sudo hostnamectl set-hostname 新主机名

# 修改漂亮主机名（用于显示）
sudo hostnamectl set-hostname "漂亮名称" --pretty
```

## 方法二：修改 `/etc/hostname` 文件

```bash
# 1. 编辑 hostname 文件
sudo nano /etc/hostname

# 或使用 echo
sudo echo "new-hostname" > /etc/hostname

# 2. 更新当前会话的主机名
sudo hostname new-hostname
```

## 验证修改

```bash
# 查看当前主机名
hostname

# 或
hostnamectl status

# 或
cat /etc/hostname
```
