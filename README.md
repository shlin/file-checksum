# Project: file-checksum
本專案以 Java 開發用來計算檔案與純文字的Checksum

# Required library
+ commons-cli-1.3.1.jar
+ commons-io-2.5

# Usage
```
usage: java -jar file-checksum.jar [OPTION]
 -digest <arg>       雜湊運算演算法，預設：MD5。
 -digestlist         列出支援的雜湊運算演算法
 -file <file path>   檔案路徑
 -help               顯示HELP訊息
 -text <string>      計算純文字內容
```
