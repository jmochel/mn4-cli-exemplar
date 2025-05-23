package org.saltations.mn4;

import java.util.List;
import java.util.concurrent.Callable;

import org.saltations.endeavour.FailureDescription;
import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Outcomes;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Slf4j
@Command(name = "tz", 
    description = "WorldTimeAPI Timezone query command",
    mixinStandardHelpOptions = true)
public class TZCommand implements Callable<Outcome<FailureDescription, Integer>> {


    @Override
    public Outcome<FailureDescription, Integer> call() {
        return Outcomes.succeed(0);
    }
}