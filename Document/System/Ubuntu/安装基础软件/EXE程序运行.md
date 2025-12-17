## 卸载旧版 Wine（没装过wine跳过）

```bash
sudo apt remove --purge wine* -y 
sudo apt autoremove --purge -y 
sudo apt autoclean
rm -rf ~/.wine rm -rf ~/.config/wine
sudo rm -rf /usr/lib/x86_64-linux-gnu/wine /usr/lib/wine /usr/share/wine /etc/wine
rm -f ~/.local/share/applications/wine* 
rm -f ~/.local/share/icons/hicolor/*/apps/wine*
sudo apt autoremove --purge -y
sudo apt update
```

## 安装新版 wine

```bash
sudo dpkg --add-architecture i386
sudo apt update
sudo apt install wget -y
wget -qO - https://dl.winehq.org/wine-builds/Release.key | sudo apt-key add -

sudo add-apt-repository 'deb https://dl.winehq.org/wine-builds/ubuntu/ focal main'

sudo apt update
sudo apt install --install-recommends winehq-stable -y

wine --version

winecfg
```

### 安装额外的依赖（如果需要）

```bash
sudo apt install winetricks -y

sudo apt install winetricks -y
winetricks vcrun2015 vcrun2019 corefonts
```

## 使用方式

```bash
wine WeCom_x.x.x.x.exe
```

## 修改Windows版本

```bash
# 先关闭所有 Wine 相关进程
wineserver -k

# 修改为 Windows 10
WINEPREFIX=~/.wine winecfg
# 在弹出的图形界面中：
# 1. 选择"Windows 10"
# 2. 应用 → 确定

# 或命令行修改
WINEPREFIX=~/.wine wine reg add \
  'HKCU\Software\Wine' \
  /v 'Version' \
  /d 'win10' \
  /f
```

