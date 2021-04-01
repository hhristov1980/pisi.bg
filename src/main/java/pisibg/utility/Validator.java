package pisibg.utility;

import java.time.LocalDateTime;

public class Validator {
    public static boolean isValidString(String text){
        if(text==null){
            return false;
        }
        return text.length()>0;
    }
    public static boolean isValidInteger(int number){
        return number>0;
    }
    public static boolean isValidDouble(double number){
        return number>0;
    }
    public static boolean isValidDate(LocalDateTime localDateTime){
        return localDateTime.isAfter(LocalDateTime.now());
    }
}
