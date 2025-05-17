package org.saltations.mn4;

import lombok.extern.slf4j.Slf4j;

import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Failure;
import org.saltations.endeavour.FailureDescription;
import org.saltations.endeavour.Outcomes;

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.util.concurrent.Callable;

@Slf4j
@Command(name = "mn4-cli-exemplar", 
        description = "Exemplar application of preferred best practices for micronaut commandline applications",
        mixinStandardHelpOptions = true,
        subcommands = {
            TZCommand.class,
            IPCommand.class
        })
public class Mn4CliExemplarCommand implements Callable<Outcome<FailureDescription, Integer>> {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String... args) throws Exception {

        var result = PicocliRunner.call(Mn4CliExemplarCommand.class, args);

        if (result instanceof Failure) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    @Override
    public Outcome<FailureDescription, Integer> call() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
        return Outcomes.succeed(0);
    }

}
