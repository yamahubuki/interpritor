import newlang3.*;
import newlang4.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class newlang4Main {

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

			program = Program.getHandrar(env);
			program.parse();
			System.out.println(program.toString(0));
//			System.out.println("value = " + program.getValue());
	}
}
