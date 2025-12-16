### Ubuntu激活Typora

要去官网中下载（https://typora.io/releases/all）

> [!IMPORTANT]
>
> 使用的版本是：1.9.3
>
> 有最新版的没有用，1.9.3是可以的

### 第一步

```bash
# 克隆项目
git clone https://github.com/hazukieq/Yporaject.git
sudo apt install cargo

cd Yporaject/

# 构建项目
cargo build
ls target/debug
```

> [!NOTE]
>
> 上面步骤完成后，看看结果有没有`node_inject`

### 第二步

```bash
cargo run
sudo cp target/debug/node_inject /usr/share/typora
```

### 第三步

正常安装的Typora是在`/usr/share/typora/`下的，如果没有需要自己找下安装目录。

上边的终端别关，新开终端：

```bash
cd /usr/share/typora/
sudo chmod 777 node_inject
sudo ./node_inject 
```

### 第四步

返回之前的终端：

```bash
cd license-gen/
cargo build
cargo  run
```

### 常见问题

如果没有成功关闭Typora重新尝试下，试了再次才成功的。
