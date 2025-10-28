
package org.saltations.mn4;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.Objects;
import java.util.stream.Collectors;

import org.saltations.endeavour.Result;
import org.saltations.endeavour.Try;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.IExecutionStrategy;


/**
 * This is a custom execution strategy that will execute all commands in the chain, and stop on the first error.
 * It is used to ensure that all commands in the chain are executed, and that the first error is propagated.
 * <h4>Note</h4>
 * This should only be used for commands and subcommands that implement Callable<Result<Integer>>.
 */

@Slf4j
@Introspected
@Singleton
public class RunAllStopOnError<T> implements IExecutionStrategy  {

    private final Validator validator;

    @Inject
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

        Result<T> validationResult = null;

        for (var cmdLine : cmdChain) {

            var userObj = cmdLine.getCommandSpec().userObject();

            // Bail if the command class does not implements Callable

            if (!(userObj instanceof Callable)) {
                log.error("Command class for {} does not implement Callable", cmdLine.getCommandSpec().name());
                return ExitCode.SOFTWARE;
            }

            // Bail if the user object can be validated and is not valid
            // Perform bean validation if a Validator is available on the classpath

            try {
                // Call validator.validate(userObj)

                var violations = validator.validate(userObj);

                if (!violations.isEmpty()) {

                    var message = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .collect(Collectors.joining(", "));

                    log.error("Validation errors for {}: {}", cmdLine.getCommandSpec().name(), message);
                    return ExitCode.SOFTWARE;
                }

            } catch (Exception e) {
                log.error("Error validating command class for {}: {}", cmdLine.getCommandSpec().name(), e);
                return ExitCode.SOFTWARE;
            }
        }

        // Execute the command chain and handle the results

        var results = new ArrayList<Result<T>>();

        for (var cmdObj : cmdChain) {

            var result = cmdObj.getCommandSpec().userObject();

            // Execute the command and check if it returns an Outcome object

            try {

                @SuppressWarnings("unchecked")
                var outcome = ((Callable<Result<T>>) result).call();

                // Bail if the command does not return an Outcome object
                if (!(outcome instanceof Result<T>)) {
                    log.error("Command does not return an operating result of Result<T> type");
                    return ExitCode.SOFTWARE;
                }   

                // Evaluate the outcome 

                var operatingResult = ((Result<T>) outcome);
                results.add(operatingResult);

            } catch (Exception e) {
                log.error("Error checking command return type for {}: {}", cmdObj.getCommandSpec().name(), e.getMessage());
                return ExitCode.SOFTWARE;
            }
        }
  
        return ExitCode.OK;
    }

}