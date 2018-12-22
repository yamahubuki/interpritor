# BASIC language interpreter on Java


## 概要

- ３年次後期選択科目「実践的プログラミング」で作成しているBASIC言語のインタプリタ
- 実装は授業で配布された文法一覧に基づいており、DIMやfunctionなどは予約語であるが実装がない


## フォルダ構成

### newlang3

- 課題１　字句解析プログラム(LexicalAnalyzer)
- Javaのパッケージとなっており、newlang4やnewlang5からも利用している
- newlang4の作成の過程でいくつかの機能(関数)を追加している


### newlang3old

- 課題１で作成・提出した当時のnewlang3パッケージ


### newlang4

- 課題２　文法解析プログラム(SyntaxAnalyzer)
- Javaのパッケージとなっており、newlang5からも利用している

### newlang5

- 課題３　interpreter
- Javaのパッケージとなっており、ここまでで作成しなかったクラス郡

### readme.md

- 本ファイル

### compile.bat

- 全てのjavaファイルをまとめてコンパイルするためのWindows用batファイル

### test.bas

- 作成したプログラムのテスト用BASICソースファイル


### newlang3Main.java
### newlang4Main.java
### newlang5Main.java

- 各段階までのプログラムをテストするためのMainクラス


### 文法.txt

- 授業で配布された文法一覧
- ファイル末尾に私による追記がある
- <VARLIST>など、一度も使われない記述は削除してある






