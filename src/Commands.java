import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.Arrays;

public class Commands {
   public static String sum(String equation) throws ScriptException {
       if(equation.trim().length() == 0) {
           equation = "0";
       }
       ScriptEngineManager mgr = new ScriptEngineManager();
       ScriptEngine engine = mgr.getEngineByName("js");
       return engine.eval(equation).toString();
   }

   public static String ifElse(String arguments) {
       String[] argumentsSplitted = arguments.split(",");
       String result = argumentsSplitted[1];
       if(argumentsSplitted[0].toLowerCase().equals("true")) {
          result = argumentsSplitted[1];
       } else {
           result = argumentsSplitted[2];
       }
       return result;
   }

   public static String PI(String arguments) {
       return Double.toString(Math.PI);
   }

   public static String sekToDollar(String arguments) {
       final double sek = Double.parseDouble(arguments);
       // change conversion numbers
       return Double.toString(sek * 0.091595265);
   }

   public static String dollarToSek(String arguments) {
       final double dollar = Double.parseDouble(arguments);
       // change conversion numbers
       return Double.toString(dollar / 0.091595265);
   }



}
