import {Combobox, InputBase, useCombobox} from "@mantine/core";
import {useCallback, useMemo, useState} from "react";
import Issue from "./Issue";
import {useDebouncedCallback} from "@mantine/hooks";
import "@mantine/core/styles/Combobox.css";

async function getIssuePickerIssues(query: string): Promise<Issue[]> {
    const response = await fetch("/api/issuepicker?" + new URLSearchParams({
        query: query
    }), {
        headers: new Headers({
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Issue[];
}

type IssuePickerProps = {
    issue: Issue | undefined, recentIssues: Issue[], onChange: (issue: Issue | undefined) => void
}

function IssuePicker({
    issue,
    recentIssues,
    onChange
}: Readonly<IssuePickerProps>) {
    const combobox = useCombobox({
        onDropdownClose: () => {
            combobox.resetSelectedOption();
        }
    });

    const [issues, setIssues] = useState<Issue[]>(recentIssues);

    const onOptionSubmit = useCallback((issueId: string) => {
        if (issueId) {
            issues.filter(issue => issue.id === issueId).forEach(issue => {
                setSearchValue(issue.key + ": " + issue.summary);
                onChange(issue);
            });
        } else {
            setSearchValue("");
            onChange(undefined);
        }
        setIssues(recentIssues);
        combobox.closeDropdown();
    }, [issues, onChange, recentIssues, combobox]);

    const [searchValue, setSearchValue] = useState(issue ? issue.key + ": " + issue.summary : "");

    const handleSearch = useDebouncedCallback((searchValue: string) => {
        getIssuePickerIssues(searchValue)
        .then(setIssues);
    }, 500);

    const onSearchChange = useCallback((searchValue: string) => {
        setSearchValue(searchValue);
        handleSearch(searchValue);
    }, [handleSearch]);

    const options = useMemo(() => issues.map((issue) => (
        <Combobox.Option value={issue.id} key={issue.id}>
            {issue.key}: {issue.summary}
        </Combobox.Option>
    )), [issues]);

    return (
        <Combobox store={combobox} onOptionSubmit={onOptionSubmit}>
            <Combobox.Target>
                <InputBase label="Issue"
                    rightSection={<Combobox.Chevron/>}
                    rightSectionPointerEvents="none"
                    onClick={() => combobox.openDropdown()}
                    onFocus={() => combobox.openDropdown()}
                    onBlur={() => {
                        combobox.closeDropdown();
                        setSearchValue(issue ? issue.key + ": " + issue.summary : "");
                        setIssues(recentIssues);
                    }}
                    value={searchValue}
                    onChange={(event) => {
                        combobox.updateSelectedOptionIndex();
                        onSearchChange(event.currentTarget.value);
                    }}/>
            </Combobox.Target>

            <Combobox.Dropdown>
                <Combobox.Options>
                    {options.length > 0 ? options : <Combobox.Empty>Nothing found</Combobox.Empty>}
                </Combobox.Options>
            </Combobox.Dropdown>
        </Combobox>
    );
}

export default IssuePicker;
