package de.schroenser.timera.issuepicker;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/issuepicker")
@RequiredArgsConstructor
public class IssuePickerController
{
    private final IssuePickerService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Issue> search(@RequestParam String query)
    {
        return service.search(query);
    }
}
