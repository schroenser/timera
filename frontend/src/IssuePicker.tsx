import {ComboboxItem, Select} from "@mantine/core";
import {useCallback, useState} from "react";
import IssuePickerIssue from "./IssuePickerIssue";

async function getIssuePickerIssues(query: string): Promise<IssuePickerIssue[]> {
    const response = await fetch("/api/issuepicker?" + new URLSearchParams({
        query: query
    }), {
        headers: new Headers({
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as IssuePickerIssue[];
}

type IssuePickerProps = {
    onIssuePicked: (issueKey: string) => void
}

function IssuePicker({onIssuePicked}: IssuePickerProps) {
    const [data, setData] = useState<ComboboxItem[]>([]);
    const [value, setValue] = useState<ComboboxItem | null>();

    const onChangeInternal = useCallback((_value: string | null, option: ComboboxItem) => {
        setValue(option);
        onIssuePicked(option.value);
    }, [setValue]);

    const [searchValue, setSearchValue] = useState("");

    const onSearchChange = useCallback((searchValue: string) => {
        setSearchValue(searchValue);
        getIssuePickerIssues(searchValue)
        .then(issuePickerIssues => issuePickerIssues.map(issuePickerIssue => {
            return {
                value: issuePickerIssue.key,
                label: issuePickerIssue.key + ": " + issuePickerIssue.summary
            };
        }))
        .then(data => {
            if (data && data.length === 0) {
                data.push({
                    value: searchValue,
                    label: searchValue
                });
            }
            return data;
        })
        .then(setData);
    }, [setSearchValue, setData]);

    return (
        <Select label="Issue"
            data={data}
            value={value ? value.value : null}
            onChange={onChangeInternal}
            searchable
            searchValue={searchValue}
            onSearchChange={onSearchChange}/>
    );
}

export default IssuePicker;
