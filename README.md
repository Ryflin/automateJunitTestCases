# automateJunitTestCaseCSE2231
This program is designed to automate test cases. So that the input can be fluid, the program assumes a few key identifiers that determine where input needs to be changed. Additionally String is used as the primary storage method so test cases that have more than Integer.MAX_VALUE / 2 characters will not be able to replicate. 

The generated contents are automatically added to the clipboard.

This project is still in testing phase. There may be unexpected bugs that need fixed.

# Formatting specifications
The @Test is used to identify the start of the test function.

Have at at least one @Test (yes it can be in comments) before the function name.

Do not put block comments /**/ inline with your variables that have test cases, otherwise the program will assume that this line is comments and not look for an equals sign.

Additionally multi-line equations are not currently supported.
Each assignment that you want to modify should get its own line.
If there are multiple assignments per line the program should extract the first one.

# Sample Test Case
    /**
      * Sample Test Case. 
      */
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
    q //to ensure that no line breaks are accidentally assumed to be the end of input, a q on its own line with no formatting is used to signal the end of the test case formatting.
 
