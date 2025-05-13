package org.saltations.mn4;

import lombok.Getter;
import org.saltations.endeavour.FailureType;

@Getter
public enum ExemplarFailureType implements FailureType 
{
    GENERIC("generic-failure", "A generic failure occurred"),
    GENERIC_EXCEPTION("generic-exception-failure", "An exception occurred: {0}");

    private final String title;
    private final String template;

    ExemplarFailureType(String title, String template) {
        this.title = title;
        this.template = template;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getTemplate() {
        return template;
    }
}