# subname

Rename subtitles to match video filenames with no configuration.

## Usage

### 1. Install Kotlin

macOS:

```shell
brew install kotlin
```

Ubuntu:

```shell
sudo snap install --classic kotlin
```

Windows:

```shell
choco install kotlin
```

### 2. Run script

Put all videos and subtitles in the same directory and run the script:

Download then execute:

```shell
wget https://raw.githubusercontent.com/him188/subname/main/subname.main.kts
chmod +x subname.main.kts
./subname.main.kts
```

Or execute once:

```shell
wget -O - https://raw.githubusercontent.com/him188/subname/main/subname.main.kts | kotlin
```
