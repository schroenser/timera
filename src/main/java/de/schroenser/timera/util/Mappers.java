package de.schroenser.timera.util;

import java.util.List;
import java.util.function.Function;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Mappers
{
    public <S, D> List<D> map(List<S> source, Function<S, D> mapper)
    {
        if (source == null)
        {
            return null;
        }
        return source.stream()
            .map(mapper)
            .toList();
    }
}
