package org.saltations.mn4;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.saltations.endeavour.FailureDescription;
import org.saltations.endeavour.Outcome;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Mn4CliExemplarCommandTest {

    @Test
    public void testWithCommandLineOption() throws Exception {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "-v" };

            Outcome<FailureDescription, Integer>  outcome = PicocliRunner.call(Mn4CliExemplarCommand.class, ctx, args);
            // mn4-cli-exemplar
            assertTrue(baos.toString().contains("Hi!"));
        }
    }
}
