package newlang3;

public enum CharType {
	SKIP,			//半角スペースと水平タブ
	NEWLINE,		//改行文字
	ESCAPE,			//半角の\
	LETTER,			//半角アルファベット
	DIGIT,			//半角数字
	LITERAL,		//"と'
	SYMBOL,			//LexicalTypeで定義された記号に用いられる文字
	OTHER,			//その他
	EOF				//ファイル終端
}
