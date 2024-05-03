import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.lang.reflect.Method;

public class Excel {

    String sheet[][];
    String calculatedSheet[][];
    String variableInitializer;
    Excel() {
        // add one because we want to show the names of the columns and rows, but also follow the input for the sheet
        //                      rows | column
        this.sheet = new String[10+1][26+1];
        this.variableInitializer = "\\$";

        Arrays.stream(this.sheet).forEach(a -> Arrays.fill(a, " "));

        // adds the coords on top
        for (int i = 1; i < this.sheet[0].length; i++) {
           this.sheet[0][i] = Character.toString((char) i+64);
        }

        // adds the coords on left
        for (int i = 1; i < this.sheet.length; i++) {
            this.sheet[i][0] = Integer.toString(i);
        }

        this.calculatedSheet= Arrays.stream(this.sheet).map(String[]::clone).toArray(String[][]::new);
    }

    void welcomeScreen() {
        System.out.println("WELCOME TO A RIPOFF SHITTY EXCEL PRODUCT");
        System.out.println("developed by shitty productions");
        System.out.println("----------------------------------------");
    }

    // generic classes, because we want to be able to put whatever kind of array into the method
    public static <T> void prettyPrint2DArray(T[][] cells) {
        int rows = cells.length;
        int cols = cells[0].length;
        int pad = Arrays.stream(cells).flatMap(Arrays::stream).mapToInt(e -> e.toString().length()).max().orElse(0);
        StringBuilder buffer = new StringBuilder();
        System.out.printf("%" + pad + "s%n", "-".repeat(((cols * pad) + (2 * cols) + 1)));
        // iterate rows
        IntStream.range(0, rows).forEach(row -> {
            // iterate cols
            IntStream.range(0, cols).forEach(col -> buffer.append(
                    String.format("%s%" + pad + "s %1s", col == 0 ? "|" : "", cells[row][col], "|")));
            // back to rows loop
            buffer.append(String.format("%n%-" + (pad) + "s%n", "-".repeat(buffer.length())));
            System.out.printf("%s", buffer.toString());
            buffer.delete(0, buffer.length());
        });
    }

    String getDataFromBox(String[][] sheet, String coord) {
        // ## check so no row is 0
        // adding 1 because the first element is the topbar and leftbar
        int col = ((int) coord.charAt(0)) - 64;
        int row = Integer.parseInt(String.valueOf(coord.charAt(1)));
        String result = sheet[row][col];
        return result;
    }

    String[][] calculateArray(String[][] arr) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String[][] copy = Arrays.stream(arr).map(String[]::clone).toArray(String[][]::new);

        final Method[] methodList = Commands.class.getDeclaredMethods();
        String[] commands = new String[methodList.length];

        // äckligt
        for (int i = 0; i < methodList.length; i++) {
            commands[i] = methodList[i].getName();
        }

        for (int row = 0; row < copy.length; row++) {
            for (int col = 0; col < copy[0].length; col++) {
                final String[] textInBox = copy[row][col].split("[()]");
                String commandInBox = "";
                String argumentsInBox = "";

                if(textInBox.length > 0) {
                    commandInBox = textInBox[0];
                }
                if(textInBox.length > 1) {
                    argumentsInBox = textInBox[1];
                }

                String[] variables = getVariables(argumentsInBox);

                if(variables.length > 0) {
                    // there are some variables
                }

                String calculatedArgumentsInBox = argumentsInBox;

                //You can use contains(), indexOf() and lastIndexOf() method to check if one String contains another String in Java or not. If a String contains another String then it's known as a substring. The indexOf() method accepts a String and returns the starting position of the string if it exists, otherwise, it will return -1.
                for (int i = 0; i < variables.length; i++) {
                    String data = getDataFromBox(copy, variables[i]);
                    calculatedArgumentsInBox = switchVariableToResultInText(calculatedArgumentsInBox, variables[i], data);
                }



                // fix this shit right now plz
                if(Arrays.asList(commands).contains(commandInBox)) {
                    Method method = Commands.class.getMethod(commandInBox, String.class);
                    Object result = method.invoke(new Commands(), calculatedArgumentsInBox);
                    copy[row][col] = (String) result;
                }

            }
        }
        return copy;
    }

    private String switchVariableToResultInText(String text, String variable, String data) {
        // fix this later change to variableinitializer
        String variableWithInitizializer = this.variableInitializer.charAt(this.variableInitializer.length()-1) + variable;
        String calculatedText = text;

        if(calculatedText.contains(variableWithInitizializer)) {
            calculatedText = calculatedText.replace(variableWithInitizializer, data);
        }

        return calculatedText;
    }

    String[] getVariables(String arguments) {
        // split on $
        String[] splitOnVariableInitializer = arguments.split(this.variableInitializer);

        // checks if the array has been split, if it has it is implied that there also is an variable in the array
        if(splitOnVariableInitializer.length < 2) {
           return new String[0];
        }

        // create variables array
        // we remove one because we dont count the one that doesn't have a variable aka the first one
        String[] variables = new String[splitOnVariableInitializer.length-1];

        // get the two characters after each split
        for (int i = 1; i < splitOnVariableInitializer.length; i++) {
            // add to array
            // we remove one from the index in variables, because we want to start from the beginning of the array
            // coord is 2 in length, which is why we get the first and second after the initializer
            variables[i-1] = splitOnVariableInitializer[i].substring(0, 2);
        }
        return variables;
    }

    void draw() {
        prettyPrint2DArray(this.calculatedSheet);
        //This allows you to print the array as matrix
    }

    void update(String box, String command) throws ScriptException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        char letter = box.charAt(0);
        // we remove 64 because we want to get the index not the number for the ascii character
        int columnIndex= letter-64;
        int rowIndex = Integer.parseInt(box.substring(1));
        this.sheet[rowIndex][columnIndex] = command;
        this.calculatedSheet = calculateArray(this.sheet);

    }
}

