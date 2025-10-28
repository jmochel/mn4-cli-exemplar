package org.saltations.mn4;

import java.util.concurrent.Callable;

import org.saltations.endeavour.Result;
import org.saltations.endeavour.Try;

import io.micronaut.configuration.picocli.MicronautFactory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.Introspected;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Singleton
@Introspected
@Command(name = "mn4-cli-exemplar", description = "Exemplar application of preferred best practices for micronaut commandline applications", mixinStandardHelpOptions = true, subcommands = {
        TZCommand.class,
        IPCommand.class
})
public class Mn4CliExemplarCommand implements Callable<Result<Integer>> {

    @Option(names = { "-v", "--verbose" }, description = "...")
    boolean verbose;

    public static void main(String... args) throws Exception {
        int exitCode = run(args);

        System.exit(exitCode);
    }

    public static int run(String... args) throws Exception {
        int exitCode;
        try (ApplicationContext ctx = ApplicationContext.run()) {

            var root = ctx.createBean(Mn4CliExemplarCommand.class);

            // Create a new CommandLine instance with the root command and the Micronaut
            // factory

            var factory = new MicronautFactory(ctx);
            var executionStrategy = factory.create(RunAllStopOnError.class);

            var cmdLine = new CommandLine(root, factory);
            cmdLine.setExecutionStrategy(executionStrategy);

            exitCode = cmdLine.execute(args);
        }
        return exitCode;
    }

    @Override
    public Result<Integer> call() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
        return Try.success(0);
    }

}
