import newlang3.*;
import java.io.*;

class Main {

	public static void main(String[] args) {
		InputStream in;
		String fileName="test1.bas";
		LexicalAnalyzer la;
		LexicalUnit lu;

		if (args.length>0){
			fileName=args[0];
		}

		try{
			in=new FileInputStream(fileName);
		} catch(IOException e) {
			System.out.println(fileName+"を読み込めませんでした。");
			return;
		}
		la=new LexicalAnalyzerImpl(in);

		try {
			while(true){
				lu=la.get();
				System.out.println(lu);
				if (lu.getType()==LexicalType.EOF){
					break;
				}
			}
		} catch (IOException e){
			System.out.println("I/Oエラーが発生しました。");
		} catch (SyntaxException e){
			System.out.println(e.getMessage());
		} catch (Exception e){
			System.out.println("不明な例外："+e);
		} finally {
			try {
				in.close();
			} catch(IOException e){
				System.out.println("ファイルのクローズに失敗しました。");
			}
		}
	}
}
