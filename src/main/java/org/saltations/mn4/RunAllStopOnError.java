
package org.saltations.mn4;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.saltations.endeavour.Failure;
import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Success;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.IExecutionStrategy;


/**
 * This is a custom execution strategy that will execute all commands in the chain, and stop on the first error.
 * It is used to ensure that all commands in the chain are executed, and that the first error is propagated.
 * <h4>Note</h4>
 * This should only be used for commands and subcommands that implement Callable<Outcome<Integer>>.
 */

@Slf4j
@Introspected
@Singleton
public class RunAllStopOnError implements IExecutionStrategy  {


    private final Validator validator;

    public RunAllStopOnError(Validator validator) {
        this.validator = validator;
    }

    @Override 
    public int execute(CommandLine.ParseResult pr) throws CommandLine.ExecutionException {

        var helpExit = CommandLine.executeHelpRequest(pr);

        if (helpExit != null) 
        {
            return ExitCode.OK;
        }

        var cmdChain = pr.asCommandLineList();

        // Validate each populated command object in the command chain before execution.

        for (var cmdLine : cmdChain) {

            var userObj = cmdLine.getCommandSpec().userObject();

            // Bail if the command class does not implements Callable

            if (!(userObj instanceof Callable)) {
                System.err.println("Command class for " +  cmdLine.getCommandSpec().name() +  "  does not implement Callable");
                return ExitCode.SOFTWARE;
            }

            // Bail if the user object can be validated and is not valid

            if (userObj != null) {

                // Perform bean validation if a Validator is available on the classpath

                try {
                    // Call validator.validate(userObj)

                    var violations = validator.validate(userObj);

                    if (!violations.isEmpty()) {

                        var message = violations.stream()
                            .map(v -> v.getPropertyPath() + " " + v.getMessage())
                            .collect(Collectors.joining(", "));

                        System.err.println(message);
                        return ExitCode.USAGE;
                    }

                } catch (CommandLine.ParameterException pe) {
                    throw pe; // propagate validation errors
                } catch (Exception e) {
                    // If validation infrastructure fails, treat as non-fatal and continue
                    // (could log here if desired, but per project rules, do not log here)
                }
            }
        }

        // Execute the command chain and handle the results

        var results = new ArrayList<Object>();

        for (var cmdObj : cmdChain) {

            var result = cmdObj.getCommandSpec().userObject();

            // Bail if the command does not implements Callable

            if (!(result instanceof Callable)) {
                log.error("Command class for{} does not implement Callable", cmdObj.getCommandSpec().name());
                return ExitCode.SOFTWARE;
            }

            // Execute the command and check if it returns an Outcome object

            try {

                var outcome = ((Callable<?>) result).call();

                // Bail if the command does not return an Outcome object
                if (!(outcome instanceof Outcome)) {
                    System.err.println("Command does not return an operating result of Outcome");
                    return ExitCode.SOFTWARE;
                }

                // Evaluate the outcome 

                var operatingResult = ((Outcome<Integer>) outcome);

                switch (operatingResult) {
                    case Success success:
                        results.add(success.get());
                        break;
                    case Failure failure:
                        System.err.println(failure.getDetail());
                        return ExitCode.SOFTWARE;
                    default:
                        throw new CommandLine.ExecutionException(cmdObj, "Error checking command return type", new Exception("Unknown outcome type"));
                }

            } catch (Exception e) {
                throw new CommandLine.ExecutionException(cmdObj, "Error checking command return type", e);
            }

        }
  
        return ExitCode.OK;

    }

}