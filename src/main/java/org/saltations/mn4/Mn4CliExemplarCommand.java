package org.saltations.mn4;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Failure;
import org.saltations.endeavour.FailureAnalysis;
import org.saltations.endeavour.Outcomes;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "mn4-cli-exemplar", 
        description = "Exemplar application of preferred best practices for micronaut commandline applications",
        mixinStandardHelpOptions = true)
public class Mn4CliExemplarCommand implements Callable<Outcome<FailureAnalysis, Integer>> {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String... args) throws Exception {

        var cmd = new CommandLine(new Mn4CliExemplarCommand());
        
        cmd.execute(args);

        var result = (Outcome<?,?>) cmd.getExecutionResult();
        if (result instanceof Failure) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    @Override
    public Outcome<FailureAnalysis, Integer> call() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
        return Outcomes.succeed(0);
    }

}
