import newlang3.*;
import newlang4.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class newlang5Main {

	public static void main(String[] args) throws Exception {
			InputStream in;
			String fileName="test.bas";
			LexicalAnalyzer lex;
			Environment		env;
			Node			program;
	  
			System.out.println("basic parser");

			if (args.length>0){
				fileName=args[0];
			}

			try{
				in=new FileInputStream(fileName);
			} catch(IOException e) {
				System.out.println(fileName+"Çì«Ç›çûÇﬂÇ‹ÇπÇÒÇ≈ÇµÇΩÅB");
				return;
			}

			lex = new LexicalAnalyzerImpl(in);
			env = new Environment(lex);

			program = Program.getHandler(env);
			program.parse();
			System.out.println(program.getValue());
//			System.out.println("value = " + program.getValue());
	}
}
