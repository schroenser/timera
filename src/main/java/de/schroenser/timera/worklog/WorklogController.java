package de.schroenser.timera.worklog;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<WorklogDto> list(@RequestParam OffsetDateTime start, @RequestParam OffsetDateTime end)
    {
        return Mappers.map(service.list(start, end), WorklogDtoMapper::fromPojo);
    }

    @PutMapping(value = "{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<WorklogDto> update(@PathVariable String id, @RequestBody WorklogDto dto)
    {
        if (!id.equals(dto.worklogId()))
        {
            return ResponseEntity.badRequest()
                .build();
        }
        return ResponseEntity.ok(WorklogDtoMapper.fromPojo(service.update(WorklogDtoMapper.toPojo(dto))));
    }
}
