package org.saltations.mn4;

import java.util.concurrent.Callable;

import org.saltations.endeavour.Outcome;
import org.saltations.endeavour.Outcomes;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Slf4j
@Command(name = "ls", 
    description = "List Timezones",
    mixinStandardHelpOptions = true)
public class TZListTimezonesCommand implements Callable<Outcome<Integer>> 
{
    @Inject
    private WorldTimeApiClient client;

    @Override
    public Outcome<Integer> call() {
        // business logic here
        var timezones = client.listTimezones();

        System.out.println("Timezones:");
        
        for (String timezone : timezones) {
            System.out.println(timezone);
        }
        
        return Outcomes.succeed(0);
    }
}
