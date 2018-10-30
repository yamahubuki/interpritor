package newlang3;

import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {

	//クラス変数
	private static final Map RESERVED_WORD=new HashMap();
	static {	//スタティックイニシャライザ。最初の１度だけ実行
		RESERVED_WORD.put("if",LexicalType.IF);
		RESERVED_WORD.put("then",LexicalType.THEN);
		RESERVED_WORD.put("else",LexicalType.ELSE);
		RESERVED_WORD.put("elseif",LexicalType.ELSEIF);
		RESERVED_WORD.put("endif",LexicalType.ENDIF);
		RESERVED_WORD.put("for",LexicalType.FOR);
		RESERVED_WORD.put("forall",LexicalType.FORALL);
		RESERVED_WORD.put("next",LexicalType.NEXT);
		RESERVED_WORD.put("func",LexicalType.FUNC);
		RESERVED_WORD.put("dim",LexicalType.DIM);
		RESERVED_WORD.put("as",LexicalType.AS);
		RESERVED_WORD.put("end",LexicalType.END);
		RESERVED_WORD.put("while",LexicalType.WHILE);
		RESERVED_WORD.put("do",LexicalType.DO);
		RESERVED_WORD.put("until",LexicalType.UNTIL);
		RESERVED_WORD.put("loop",LexicalType.LOOP);
		RESERVED_WORD.put("to",LexicalType.TO);
		RESERVED_WORD.put("wend",LexicalType.WEND);
	}
	private static final Map SIMBOL_FIRSTWORD=new HashMap();
	static {	//スタティックイニシャライザ。最初の１度だけ実行
		SIMBOL_FIRSTWORD.put('\n',LexicalType.NL);
		SIMBOL_FIRSTWORD.put('\r',LexicalType.NL);
//		SIMBOL_FIRSTWORD.put(13,LexicalType.NL);
		SIMBOL_FIRSTWORD.put(".",LexicalType.DOT);
		SIMBOL_FIRSTWORD.put("+",LexicalType.ADD);
		SIMBOL_FIRSTWORD.put("-",LexicalType.SUB);
		SIMBOL_FIRSTWORD.put("*",LexicalType.MUL);
		SIMBOL_FIRSTWORD.put("/",LexicalType.DIV);
		SIMBOL_FIRSTWORD.put("(",LexicalType.LP);
		SIMBOL_FIRSTWORD.put(")",LexicalType.RP);
		SIMBOL_FIRSTWORD.put(",",LexicalType.COMMA);
		SIMBOL_FIRSTWORD.put('=',null);
		SIMBOL_FIRSTWORD.put("<",null);
		SIMBOL_FIRSTWORD.put(">",null);
	}

	PushbackReader r;

	//コンストラクタ
	public LexicalAnalyzerImpl(PushbackReader in){
		r=in;
	}

	//１単語を返す
	public LexicalUnit get() throws Exception {
		int c;							//最初の１文字を格納
		while((c=r.read())>=0){
			if (c!=' ' && c!='\t'){		//スペースとタブは読み飛ばすべき
				break;					//それ以外だったらbreakしてつぎへすすむ
			}
		}
System.out.println(c);
		if(c>=0){						//EOF以外
			//c=アルファベット
			if ((c>='A' && c<='Z') || (c>='a' && c<='z')){
				r.unread(c);			//先頭文字も後で必要なので戻しておく
				return getString();
			}
			//c=数字
			if (c>='0' && c<='9'){
				r.unread(c);			//先頭文字も後で必要なので戻しておく
				return getNumber();
			}
			//c=クオーテーション
			if (c=='\"' || c=='\''){
				r.unread(c);			//先頭文字も後で必要なので戻しておく
				return getLiteral();
			}
			//c=記号
			if (SIMBOL_FIRSTWORD.containsKey((char)c)==true){
				r.unread(c);
				return getSimbol();
			}
			//解釈不能は例外処理へ
			System.out.println(c+": 解釈不能な文字を見つけました。");
			throw new Exception();
		} else {						//EOF
//System.out.println("EOFきた");
			LexicalUnit lu=new LexicalUnit(LexicalType.EOF);
			return lu;
		}
	}

	public boolean expect(LexicalType type) throws Exception{
		return false;
	}
	public void unget(LexicalUnit token) throws Exception{
		return;
	}

	//先頭がクオーテーションの場合
	private LexicalUnit getLiteral(){
		String s=null;					//結果文字列格納用。前後の\"や\'は入れないようにする。
		char c;							//一時的な保存領域
		try {
			char openChar=(char)r.read();	//開いた記号。\"か\'のいずれかが入るはず
			while(true){
				c=(char)r.read();
				if (c==openChar){			//ファイル終端に到達
					break;
				} else if (c==0){			//ファイル末尾に到達
					break;
				} else {					//リテラル内の普通の文字
					s+=c;
				}
			}		
		} catch (Exception e){
				System.out.println(e);
		}
		ValueImpl v=new ValueImpl(s);
		LexicalUnit lu=new LexicalUnit(LexicalType.LITERAL,v);
		return lu;
	}

	//先頭がアルファベットの場合
	private LexicalUnit getString(){
		String target="";
		char c;
		LexicalUnit lu;

		while(true){
			try {
				c=(char)r.read();
				//いらないものを読んでしまった場合
				if (!((c>='A' && c<='Z') || (c>='a' && c<='z') || (c>='0' && c<='9'))){
					r.unread((int)c);
					break;
				}
				target+=c;
			} catch (Exception e){
				System.out.println(e);
			}
		}
		if (RESERVED_WORD.containsKey(target.toLowerCase())==true){		//予約語
			lu=new LexicalUnit((LexicalType)RESERVED_WORD.get(target.toLowerCase()));
		} else {											//予約語以外＝変数名など
			ValueImpl v=new ValueImpl(target);
			lu=new LexicalUnit(LexicalType.NAME,v);
		}
		return lu;
	}

	//先頭が数字の場合
	private LexicalUnit getNumber(){
		String s="";					//結果文字列格納用。前後の\"や\'は入れないようにする。
		char c;							//一時的な保存領域
		boolean dp=false;				//小数点を見つけたらtrueにする
		try {
			while(true){
				c=(char)r.read();
				if (c>='0' && c<='9'){					//数字
					s+=c;
				} else if (c=='.' && dp==false){			//小数点
					s+=c;
					dp=true;
				} else {								//その他
					break;
				}
			}		
		} catch (Exception e){
				System.out.println(e);
		}
		ValueImpl v=new ValueImpl(s);
		LexicalUnit lu;
		if(dp){
			lu=new LexicalUnit(LexicalType.DOUBLEVAL,v);
		} else {
			lu=new LexicalUnit(LexicalType.INTVAL,v);
		}
		return lu;
	}

	private LexicalUnit getSimbol(){
		char c;							//一時的な１文字目の保存領域
		char c2;						//一時的な２文字目の保存領域
		LexicalUnit lu=null;
		try {
			c=(char)r.read();
			if (c=='='){
				c2=(char)r.read();
				if (c2=='<') {
					lu=new LexicalUnit(LexicalType.LE);
				} else if (c2=='>') {
					lu=new LexicalUnit(LexicalType.GE);
				} else {
					r.unread((int)c2);		//2文字目は関係ないものだったので戻す
					lu=new LexicalUnit(LexicalType.EQ);
				}
			} else if (c=='<'){
				c2=(char)r.read();
				if (c2=='=') {
					lu=new LexicalUnit(LexicalType.LE);
				} else if (c2=='>') {
					lu=new LexicalUnit(LexicalType.NE);
				} else {
					r.unread((int)c2);		//2文字目は関係ないものだったので戻す
					lu=new LexicalUnit(LexicalType.LT);
				}
			} else if (c=='>'){
				c2=(char)r.read();
				if (c2=='=') {
					lu=new LexicalUnit(LexicalType.GE);
				} else {
					r.unread((int)c2);		//2文字目は関係ないものだったので戻す
					lu=new LexicalUnit(LexicalType.GT);
				}
			} else {						//１文字で意味を成す記号
				lu=new LexicalUnit((LexicalType)SIMBOL_FIRSTWORD.get(c));
			}
		} catch (Exception e){
				System.out.println(e);
		}
		return lu;
	}
}


