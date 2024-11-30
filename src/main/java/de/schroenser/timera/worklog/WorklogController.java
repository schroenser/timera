package de.schroenser.timera.worklog;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.schroenser.timera.util.Mappers;

@RestController
@RequestMapping("api/worklog")
@RequiredArgsConstructor
public class WorklogController
{
    private final WorklogService service;

    @GetMapping
    public List<WorklogDto> list(@RequestParam OffsetDateTime start, @RequestParam OffsetDateTime end)
    {
        return Mappers.map(service.list(start, end), WorklogDtoMapper::fromPojo);
    }
}
