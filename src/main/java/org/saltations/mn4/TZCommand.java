package org.saltations.mn4;

import java.util.List;
import java.util.concurrent.Callable;

import org.saltations.endeavour.FailureAnalysis;
import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Outcomes;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Slf4j
@Command(name = "tz", 
    description = "WorldTimeAPI Timezone query command",
    mixinStandardHelpOptions = true)
public class TZCommand implements Callable<Outcome<FailureAnalysis, Integer>> {

    @Inject
    private WorldTimeApiClient client;

    @Override
    public Outcome<FailureAnalysis, Integer> call() {
        // business logic here
        List<String> timezones = client.listTimezones();

        System.out.println("Timezones:");
        for (String timezone : timezones) {
            System.out.println(timezone);
        }
        
        return Outcomes.succeed(0);
    }
}