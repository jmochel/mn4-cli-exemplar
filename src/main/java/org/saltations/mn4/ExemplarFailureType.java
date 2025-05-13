package org.saltations.mn4;

import lombok.Getter;

import org.saltations.endeavour.FailureType;

import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum ExemplarFailureType implements FailureType 
{
    GENERIC("generic-failure", ""),
    GENERIC_EXCEPTION("generic-exception-failure", "")
    ;

    private final String title;
    private final String template;
}