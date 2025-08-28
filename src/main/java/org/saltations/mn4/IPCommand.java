package org.saltations.mn4;

import java.util.concurrent.Callable;

import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Outcomes;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Slf4j
@Command(name = "ip", 
    description = "IP address lookup command",
    mixinStandardHelpOptions = true)
public class IPCommand implements Callable<Outcome<Integer>> {

    @Override
    public Outcome<Integer> call() {
        // business logic here
        return Outcomes.succeed(0);
    }
}