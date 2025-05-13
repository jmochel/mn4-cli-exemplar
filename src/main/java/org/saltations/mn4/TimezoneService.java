package org.saltations.mn4;

import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class TimezoneService {

    private final WorldTimeApiClient client;

    @Inject
    public TimezoneService(WorldTimeApiClient client) {
        this.client = client;
    }

    public List<String> getAllTimezones() {
        return client.listTimezones();
    }

    public Map<String, Object> getTime(String area, String location) {
        return client.getTimeForLocation(area, location);
    }
}
