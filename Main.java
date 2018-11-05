import newlang3.*;

import java.io.*;

class Main {

	public static void main(String[] args) {
		PushbackReader pr;
		String fileName="test1.bas";
		if (args.length>0){
			fileName=args[0];
		}
		try{

			FileReader fr = new FileReader(fileName);
			pr = new PushbackReader(fr);
		} catch(IOException e) {
			System.out.println(fileName+"を読み込めませんでした。");
			return;
		}
		LexicalAnalyzerImpl la=new LexicalAnalyzerImpl(pr);

		LexicalUnit lu;
		try {
			while(true){
				lu=la.get();
				String tmp=lu.toString();
				System.out.println(tmp);
				if (tmp=="EOF"){
					break;
				}
			}
		} catch (Exception e){
			System.out.println(e);
		} finally {
			try {
				pr.close();
			} catch(IOException e){
				System.out.println("ファイルのクローズに失敗しました。");
			}
		}
	}
}
