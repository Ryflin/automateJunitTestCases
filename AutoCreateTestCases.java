import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.HashSet;

import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * FORMATTING SPECIFICATIONS
 * The @Test is used to identify the start of the test function.
 *
 * Have at at least one @Test (yes it can be in comments) before the function name.
 *
 * Do not put block comments /* inline with your variables that have test cases,
 * otherwise the program will assume that this line is comments and
 * not look for an equals sign.
 *
 * Additionally multi-line equations are not currently supported.
 * Each assignment that you want to modify should get its own line.
 * If there are multiple assignments per line the program should extract the first one.
 *
    @Test //needed to identify the testAverage() as a function.
    public void testAverage() {
        int i = Integer.MAX_VALUE; // the equals should not be touched
        int j = Integer.MAX_VALUE - 1;
        int rEx = Integer.MAX_VALUE - 1;
        int iEx = i;  // ignored because i and j are known variables
        int jEx = j;
        int r = functionCall(i, j); // ignored because function call is a defined function.
        assertEquals(r, rEx);
        assertEquals(i, iEx);
        assertEquals(j, jEx);
    }
 *
 */

/**
 * Automatically makes test cases based on the first template.
 *
 * @author Ryan Schley
 */
public final class AutoCreateTestCases {
    /**
     * Whatever the default function call is so that it ignores that function.
     */
    private static String FunctionCall = "functionCall";
    /**
     * If you are using a function to create classes,(this is similar to regular
     * object assignment) However, object assignment is identified by the
     * keyword new.
     */
    private static String CreateFromArgs = "createFromArgs";

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private AutoCreateTestCases() {
        // no code needed here
    }

    /**
     * Reads all the input and stops when it encounters a q on its own line.
     *
     * @return a sequence of the lines
     */
    private static Sequence<String> readAll(SimpleReader in) {

        Sequence<String> input = new Sequence1L<>();
        String readLine = in.nextLine();
        while (!readLine.equals("q")) {
            input.add(input.length(), readLine);
            readLine = in.nextLine();
        }
        return input;
    }

    /**
     * Checks if a string can parse into an integer.
     *
     * @param str
     *            the string you are trying to parse
     * @return true or false
     */
    private static boolean canParseInt(String str) {
        boolean parsable = true;
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        out.println("Paste your whole program for the initial test case.");

        Sequence<String> input = readAll(in);
        Sequence<String> generatedCases = new Sequence1L<>();
        out.print(
                "Confirm a test case or insert a number of test cases to do. ? (yY) ");
        String again = in.nextLine();
        int caseNumber = 1;
        if (canParseInt(again)) {
            int numberOfTests = Integer.parseInt(again) + 1;
            for (int i = 0; i < numberOfTests; i++) {
                out.println("Test case #" + caseNumber + 1);
                generatedCases.add(generatedCases.length(), "");
                generatedCases.append(writeTestCase(in, input, caseNumber));
                caseNumber++;
            }
        } else {
            while (again.equals("y") || again.equals("Y")) {
                out.println("Test Case #" + caseNumber + 1);
                generatedCases.add(generatedCases.length(), "");
                generatedCases.append(writeTestCase(in, input, caseNumber));
                out.print("Do another test case? (yY): ");
                again = in.nextLine();
                caseNumber++;
            }
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringBuilder textToCopy = new StringBuilder();

        for (String line : input) {
            textToCopy.append(line + "\n");
        }
        for (String line : generatedCases) {
            textToCopy.append(line + "\n");
        }
        Transferable transferable = new StringSelection(textToCopy.toString());

        clipboard.setContents(transferable, null);
        // should that not work
        //out.print("Copied to your clipboard");
        in.close();
        out.close();
    }

    /**
     * Creates a sequence of lines that are the test cases.
     *
     * @param in
     *            the Simple reader
     * @param input
     *            the lines that are given as a sample.
     * @param caseNumber
     *            the current test case number so that function names are not
     *            the same
     * @return the created sequence of lines
     */
    private static Sequence<String> writeTestCase(SimpleReader in,
            Sequence<String> input, int caseNumber) {
        boolean findFunction = false, functionSeenYet = false;
        SimpleWriter out = new SimpleWriter1L();
        HashSet<String> varStore = new HashSet<>();
        Sequence<String> nextCase = new Sequence1L<String>();
        boolean comment = false;
        for (String line : input) {

            if (line.indexOf("/*") != -1) {
                comment = true;
            }
            if (line.indexOf("*/") != -1) {
                comment = false;
            }
            if (line.indexOf("@Test") != -1 && !functionSeenYet) {
                findFunction = true;
            } 
            if (findFunction && line.indexOf("(") != -1) {
                findFunction = false;
                nextCase.add(nextCase.length(),
                        line.substring(0, line.indexOf("(")) + caseNumber
                                + line.substring(line.indexOf("(")));
            } else if (line.contains("=") && !comment
                    && line.indexOf(FunctionCall) == -1) {
                nextCase.add(nextCase.length(),
                        parseAndInsertvarStore(line, varStore, out, in));
            } else {
                nextCase.add(nextCase.length(), line);
            }
        }

        return nextCase;

    }

    /**
     * Locates the variable, and where a value would be put in the test case and
     * prompts the user for that value.
     *
     * @param line
     *            the current line it is parsing
     * @param varStore
     *            A hash set of all the variables thus encountered so that
     * @param out
     *            simple writer
     * @param in
     *            simple reader
     * @return the parsed string
     */
    private static String parseAndInsertvarStore(String line,
            HashSet<String> varStore, SimpleWriter out, SimpleReader in) {
        String varLine = line.substring(0, line.indexOf("=") + 1);
        String pastEquals = line.substring(line.indexOf("=") + 1);
        String varNoEquals = varLine.substring(0, varLine.lastIndexOf(" "));
        // finds the varStore
        varStore.add(varNoEquals.substring(varNoEquals.lastIndexOf(" ") + 1,
                varNoEquals.length()));

//        System.out.println(varLine);
//        System.out.println("varStore: " + varStore.toString());

        if (pastEquals.indexOf("new ") != -1
                || pastEquals.indexOf(CreateFromArgs) != -1) {
            if (varStore.contains(pastEquals.substring(
                    pastEquals.indexOf("(") + 2, pastEquals.indexOf(")")))) {
                return line;
            }
            varLine += pastEquals.substring(0, pastEquals.indexOf("(") + 1);
            pastEquals = pastEquals.substring(pastEquals.lastIndexOf(")"));
        } else {
            if (varStore.contains(
                    pastEquals.substring(0, pastEquals.indexOf(";")).trim())) {
                return line;
            }
            pastEquals = ";";

        }

        return varLine + userInput(in, varLine, pastEquals) + pastEquals;
    }

    /**
     * Reads input from the user.
     *
     * @param in
     *            simple reader
     * @param varLine
     *            the part of the line before the x that the user is inputting
     * @param pastEquals
     *            the part of the line after the x that the user is inputting
     * @return the completed line
     */
    private static String userInput(SimpleReader in, String varLine,
            String pastEquals) {
        System.out.print("What goes in " + varLine.trim() + " x"
                + pastEquals.trim() + " ");
        return in.nextLine();
    }

}
