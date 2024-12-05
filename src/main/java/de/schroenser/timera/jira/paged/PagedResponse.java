package de.schroenser.timera.jira.paged;

import java.util.List;

public interface PagedResponse<E>
{
    int startAt();

    int maxResults();

    int total();

    List<E> elements();
}
