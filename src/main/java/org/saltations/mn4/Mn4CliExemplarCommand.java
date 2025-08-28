package org.saltations.mn4;

import java.util.concurrent.Callable;

import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Outcomes;

import io.micronaut.configuration.picocli.MicronautFactory;
import io.micronaut.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Command(name = "mn4-cli-exemplar", 
        description = "Exemplar application of preferred best practices for micronaut commandline applications",
        mixinStandardHelpOptions = true,
        subcommands = {
            TZCommand.class,
            IPCommand.class
        })
public class Mn4CliExemplarCommand implements Callable<Outcome<Integer>> {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String... args) throws Exception {

        try (ApplicationContext ctx = ApplicationContext.run()) {

            var root = ctx.createBean(Mn4CliExemplarCommand.class);

            // Create a new CommandLine instance with the root command and the Micronaut factory

            var factory = new MicronautFactory(ctx);
            var executionStrategy = factory.create(RunAllStopOnError.class);

            var cmdLine = new CommandLine(root, factory);
            cmdLine.setExecutionStrategy(executionStrategy);

            int exit = cmdLine.execute(args);

            System.exit(exit);
        }
    }

    @Override
    public Outcome<Integer> call() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
        return Outcomes.succeed(0);
    }

}
