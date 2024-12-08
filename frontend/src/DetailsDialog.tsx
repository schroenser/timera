import {Button, Group, Modal, Space, Text, TextInput} from "@mantine/core";
import Worklog from "./Worklog";
import {useCallback, useEffect, useState} from "react";

type DetailsDialogProps = {
    opened: boolean,
    onCancel: () => void,
    onUpdate: (worklog: Worklog) => void,
    onDelete: (worklog: Worklog) => void,
    worklog: Worklog | undefined
}

function DetailsDialog({
    opened,
    onCancel,
    onUpdate,
    onDelete,
    worklog
}: DetailsDialogProps) {
    const [worklogComment, setWorklogComment] = useState<string>("");

    useEffect(() => {
        if (worklog) {
            setWorklogComment(worklog.worklogComment);
        }
    }, [worklog, setWorklogComment]);

    const onDeleteInternal = useCallback(() => {
        if (worklog) {
            onDelete(worklog);
        }
    }, [worklog, onDelete]);

    const onUpdateInternal = useCallback(() => {
        const updatedWorklog: Worklog = {
            ...worklog,
            worklogComment: worklogComment
        } as Worklog;
        onUpdate(updatedWorklog);
    }, [worklog, worklogComment, onUpdate]);

    return (
        <Modal opened={opened} onClose={onCancel} size="auto" title="Worklog" centered>
            <Group>
                <Text>Start: {worklog?.start}</Text>
                <Text>End: {worklog?.end}</Text>
            </Group>
            <Space h="md"/>
            <Text>Issue: {worklog?.issueKey}: {worklog?.issueSummary}</Text>
            <Space h="md"/>
            <TextInput label="Worklog comment"
                value={worklogComment}
                onChange={(event) => setWorklogComment(event.currentTarget.value)}/>
            <Space h="md"/>
            <Group justify="flex-end">
                <Button onClick={onDeleteInternal} color="red">Delete</Button>
                <Button onClick={onUpdateInternal}>Update</Button>
            </Group>
        </Modal>
    );
}

export default DetailsDialog;
