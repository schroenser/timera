package de.schroenser.timera.jira;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PagedResponseSpliterator<E, P extends PagedResponse<E>> implements Spliterator<E>
{
    private final Function<Integer, P> pageProvider;

    private int startAt = 0;

    private List<E> elements = List.of();
    private int index = 0;

    @Override
    public boolean tryAdvance(Consumer<? super E> action)
    {
        if (index >= elements.size())
        {
            P response = pageProvider.apply(startAt);
            elements = response.elements();
            index = 0;
            startAt += response.maxResults();
        }

        if (index < elements.size())
        {
            E element = elements.get(index);
            action.accept(element);
            index++;
            return true;
        }

        return false;
    }

    @Override
    public Spliterator<E> trySplit()
    {
        return null;
    }

    @Override
    public long estimateSize()
    {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics()
    {
        return DISTINCT | IMMUTABLE | NONNULL;
    }
}
