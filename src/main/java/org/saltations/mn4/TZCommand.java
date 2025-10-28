package org.saltations.mn4;

import java.util.concurrent.Callable;

import org.saltations.endeavour.Result;
import org.saltations.endeavour.Try;

import io.micronaut.core.annotation.Introspected;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import jakarta.inject.Singleton;

@Slf4j
@Singleton
@Introspected
@Command(name = "tz", 
    description = "WorldTimeAPI Timezone query command",
    mixinStandardHelpOptions = true)
public class TZCommand implements Callable<Result<Integer>> {


    @Override
    public Result<Integer> call() {
        return Try.success(0);
    }
}