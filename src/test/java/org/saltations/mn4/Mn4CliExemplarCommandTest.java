package org.saltations.mn4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Mn4CliExemplarCommandTest {

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testMainCommandWithoutOptions() throws Exception {

        int exitCode = Mn4CliExemplarCommand.run();

        assertEquals(0, exitCode);
    }

    @Test
    public void testMainCommandWithVerboseOption() throws Exception {
        String[] args = new String[] { "-v" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
        assertTrue(outContent.toString().contains("Hi!"));
    }

    @Test
    public void testMainCommandWithLongVerboseOption() throws Exception {
        String[] args = new String[] { "--verbose" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
        assertTrue(outContent.toString().contains("Hi!"));
    }

    @Test
    public void testHelpOption() throws Exception {
        
        var args = new String[] { "--help" };
        
        int exitCode = Mn4CliExemplarCommand.run(args);

        assertEquals(0, exitCode);
        assertTrue(outContent.toString().contains("mn4-cli-exemplar"));
        assertTrue(outContent.toString().contains("Usage:"));
    }

    @Test
    public void testTZSubcommand() throws Exception {
        String[] args = new String[] { "tz" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
    }

    @Test
    public void testTZSubcommandWithHelp() throws Exception {
        String[] args = new String[] { "tz", "--help" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
        assertTrue(outContent.toString().contains("tz"));
        assertTrue(outContent.toString().contains("WorldTimeAPI Timezone query command"));
    }

    @Test
    public void testIPSubcommand() throws Exception {
        String[] args = new String[] { "ip" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
    }

    @Test
    public void testIPSubcommandWithHelp() throws Exception {
        String[] args = new String[] { "ip", "--help" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
        assertTrue(outContent.toString().contains("ip"));
        assertTrue(outContent.toString().contains("IP address lookup command"));
    }

    @Test
    public void testInvalidSubcommand() throws Exception {
        String[] args = new String[] { "invalid" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        // Should return non-zero exit code for invalid subcommand
        assertTrue(exitCode != 0);
    }

    @Test
    public void testInvalidOption() throws Exception {
        String[] args = new String[] { "--invalid-option" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        // Should return non-zero exit code for invalid option
        assertTrue(exitCode != 0);
    }

    @Test
    public void testMultipleOptions() throws Exception {
        String[] args = new String[] { "-v", "--help" };

        int exitCode = Mn4CliExemplarCommand.run(args);
        
        assertEquals(0, exitCode);
        // Help should be displayed, but verbose output should not appear
        assertTrue(outContent.toString().contains("Usage:"));
        assertFalse(outContent.toString().contains("Hi!"));
    }

}
