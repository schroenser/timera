package de.schroenser.timera.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@AllArgsConstructor
@Getter
class Wrapper
{
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class")
    public final Object value;
}
