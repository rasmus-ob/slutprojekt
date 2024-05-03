import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ScriptException {
        Excel excel = new Excel();
        Scanner scan = new Scanner(System.in);
        excel.welcomeScreen();
        while(true) {
            excel.draw();
            System.out.println("Vilken box vill du ändra? Ex: A1 ");
            String box = scan.nextLine();
            System.out.println("Vilket commando vill du ange");
            String command = scan.nextLine();

            excel.update(box, command);
        }

    }
}
// variables === $A1 example