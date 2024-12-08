import {Button, Group, Modal, Space, Text, TextInput} from "@mantine/core";
import moment from "moment/moment";
import IssuePicker from "./IssuePicker";
import Worklog from "./Worklog";
import {useCallback, useState} from "react";
import Issue from "./Issue";

type CreateDialogProps = {
    opened: boolean, onCancel: () => void, onCreate: (worklog: Worklog) => void, range: {
        start: moment.Moment; end: moment.Moment;
    }
}

function CreateDialog({
    opened,
    onCancel,
    onCreate,
    range: {
        start,
        end
    }
}: CreateDialogProps) {
    const [issue, setIssue] = useState<Issue | undefined>();
    const [worklogComment, setWorklogComment] = useState<string>("");

    const onCloseInternal = useCallback(() => {
        setIssue(undefined);
        setWorklogComment("");
        onCancel();
    }, [setIssue, setWorklogComment, onCancel]);

    const onCreateInternal = useCallback(() => {
        if (issue) {
            const newWorklog: Worklog = {
                issueId: issue.id,
                issueKey: issue.key,
                issueSummary: issue.summary,
                start: start.toISOString(),
                end: end.toISOString()
            } as Worklog;
            if (worklogComment !== "") {
                newWorklog.worklogComment = worklogComment;
            }
            setIssue(undefined);
            setWorklogComment("");
            onCreate(newWorklog);
        }
    }, [start, end, issue, worklogComment, setIssue, setWorklogComment, onCreate]);

    return (
        <Modal opened={opened} onClose={onCloseInternal} size="auto" title="Create worklog" centered>
            <Group>
                <Text>Start: {start.format("DD.MM.YYYY HH:mm")}</Text>
                <Text>End: {end.format("DD.MM.YYYY HH:mm")}</Text>
            </Group>
            <Space h="md"/>
            <IssuePicker issue={issue} onChange={setIssue}/>
            <Space h="md"/>
            <TextInput label="Worklog comment"
                value={worklogComment}
                onChange={(event) => setWorklogComment(event.currentTarget.value)}/>
            <Space h="md"/>
            <Group justify="flex-end">
                <Button disabled={!issue} onClick={onCreateInternal}>Create</Button>
            </Group>
        </Modal>
    );
}

export default CreateDialog;
