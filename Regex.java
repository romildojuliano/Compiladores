// package stacker.rpn.lexer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	public static boolean isNum(String token) {
        Pattern pattern = Pattern.compile("-?\\d+$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(token).matches();
    }
	
	public static boolean isOP(String token) {
        Pattern pattern = Pattern.compile("[+-/\\*]$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(token).matches();
    }

}