package CMP330.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Refactor this mess
public class Validator {
    public static String checkString(String string, ValidatorOptions[] options) throws Exception{
        // Check all options presented
        for (ValidatorOptions option : options) {
            if(option.equals(ValidatorOptions.isNotEmpty)){
                // Check if string is not empty
                if(!string.isEmpty()) return string;

                throw new Exception("One more fields is empty");
            }else if(option.equals(ValidatorOptions.isEmail)){
                // Check email matches regex pattern for emails.
                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher matcher = pattern.matcher(string);

                if(matcher.matches()) return string;

                throw new Exception("Please enter a correct email");
            }else if(option.equals(ValidatorOptions.isPassword)){
                // Check if password is too small
                if(string.length() < 7){
                    throw new Exception("Please enter a more secure password");
                }
                return string;
            }

        }
        return string;
    }
}

