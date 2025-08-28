package org.saltations.mn4;

import java.util.concurrent.Callable;

import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Outcomes;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Slf4j
@Command(name = "tz", 
    description = "WorldTimeAPI Timezone query command",
    mixinStandardHelpOptions = true)
public class TZCommand implements Callable<Outcome<Integer>> {


    @Override
    public Outcome<Integer> call() {
        return Outcomes.succeed(0);
    }
}