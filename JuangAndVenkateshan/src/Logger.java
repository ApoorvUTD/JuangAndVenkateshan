import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {
	public static boolean logoff=false;
	public static void log(Host h,String line){
		if(!logoff){
			System.out.println(h.getMe().getPID() + ">" + line);
		}
	}
	public static void log( String line){
		try{
			Files.write(Paths.get("foo.out"), (line + "\n").getBytes(), StandardOpenOption.APPEND);
		}
		catch(Exception e){

		}
	}

}
