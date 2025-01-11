import {ComboboxItem, Select} from "@mantine/core";
import {useCallback, useEffect, useMemo, useState} from "react";
import Issue from "./Issue";
import {useDebouncedCallback} from "@mantine/hooks";

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
    issue: Issue | undefined, onChange: (issue: Issue | undefined) => void
}

function toComboBoxItem(issue: Issue): { label: string; value: string } {
    return {
        value: issue.id,
        label: issue.key + ": " + issue.summary
    };
}

function IssuePicker({
    issue,
    onChange
}: Readonly<IssuePickerProps>) {
    const [issues, setIssues] = useState<Issue[]>([]);

    const comboBoxItems = useMemo(() => issues.map(toComboBoxItem), [issues]);
    const value = useMemo(() => issue ? toComboBoxItem(issue) : undefined, [issue]);

    const onValueChange = useCallback((_value: string | null, option: ComboboxItem) => {
        if (option) {
            issues.filter(issue => issue.id === option.value).forEach(onChange);
        } else {
            onChange(undefined);
        }

    }, [issues, onChange]);

    const [searchValue, setSearchValue] = useState("");

    const handleSearch = useDebouncedCallback((searchValue: string) => {
        getIssuePickerIssues(searchValue)
        .then(setIssues);
    }, 500);

    useEffect(() => handleSearch(""), [handleSearch]);

    const onSearchChange = useCallback((searchValue: string) => {
        setSearchValue(searchValue);
        handleSearch(searchValue);
    }, [setSearchValue, handleSearch]);

    return (
        <Select label="Issue"
            data={comboBoxItems}
            value={value?.value}
            onChange={onValueChange}
            searchable
            searchValue={searchValue}
            onSearchChange={onSearchChange}/>
    );
}

export default IssuePicker;
