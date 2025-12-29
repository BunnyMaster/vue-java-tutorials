需要下载`1.9.3`版本的，试了很多的版本就这个版本能用。

## 激活

> [!IMPORTANT]
>
> 激活时候不要打开Typora，否则只能重新打开后才能成功

### 1、下载仓库

```bash
git clone https://github.com/hazukieq/Yporaject.git
sudo apt  install cargo
cd Yporaject/
cargo build
```

构建完成后看下是否有下面内容

```bash
ls target/debug

# 应该是有 node_inject
```

之后运行

```bash
cargo run
sudo cp target/debug/node_inject /usr/share/typora
```

### 2、换个终端

> [!Note]
>
> 换个终端继续，上面的不要关

```bash
cd /usr/share/typora/
sudo chmod 777 node_inject
sudo ./node_inject 
```

### 3、返回之前终端

```bash
cd license-gen/
cargo build
cargo  run
```

