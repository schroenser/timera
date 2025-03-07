import {useCallback, useEffect, useState} from "react";
import moment from "moment";
import Worklog from "./Worklog";
import WorklogCalendar from "./WorklogCalendar";
import "./App.css";
import {useDisclosure, useLocalStorage} from "@mantine/hooks";
import {SlotInfo} from "react-big-calendar";
import CreateDialog from "./CreateDialog";
import DetailsDialog from "./DetailsDialog";
import Issue from "./Issue";

const baseUrl = document.getElementsByTagName("meta")["ajs-base-url"].getAttribute("content");
const recentIssuesMaxLength = 20;

async function getWorklogs(start: moment.Moment, end: moment.Moment): Promise<Worklog[]> {
    const response = await fetch(baseUrl + "/rest/timera/1.0/worklog?" + new URLSearchParams({
        start: start.toISOString(),
        end: end.toISOString()
    }), {
        headers: new Headers({
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Worklog[];
}

async function createWorklog(worklog: Worklog): Promise<Worklog> {
    const response = await fetch(baseUrl + "/rest/timera/1.0/worklog", {
        method: "POST",
        body: JSON.stringify(worklog),
        headers: new Headers({
            "Content-Type": "application/json; charset=UTF-8",
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Worklog;
}

async function updateWorklog(worklog: Worklog): Promise<Worklog> {
    const response = await fetch(baseUrl + `/rest/timera/1.0/worklog/${worklog.worklogId}`, {
        method: "PUT",
        body: JSON.stringify(worklog),
        headers: new Headers({
            "Content-Type": "application/json; charset=UTF-8",
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Worklog;
}

async function deleteWorklog(worklog: Worklog): Promise<void> {
    const response = await fetch(baseUrl + `/rest/timera/1.0/worklog/${worklog.worklogId}?` + new URLSearchParams({
        issueId: worklog.issueId.toString()
    }), {
        method: "DELETE"
    });
    if (!response.ok) {
        throw new Error("Could not delete worklog. HTTP " + response.status + " " + response.statusText);
    }
}

function App() {
    const [worklogs, setWorklogs] = useState<Worklog[]>([]);
    const [recentIssues, setRecentIssues] = useLocalStorage<Issue[]>({
        key: "recentIssues",
        defaultValue: []
    });

    const [
        createDialogOpened, {
            open: createDialogOpen,
            close: createDialogClose
        }
    ] = useDisclosure(false);

    const [createWorklogRange, setCreateWorklogRange] = useState<{
        start: moment.Moment, end: moment.Moment
    }>({
        start: moment(),
        end: moment()
    });

    const [
        detailsDialogOpened, {
            open: detailsDialogOpen,
            close: detailsDialogClose
        }
    ] = useDisclosure(false);

    const [selectedWorklog, setSelectedWorklog] = useState<Worklog>();

    const [loading, setLoading] = useState(false);

    const onNavigate = useCallback((date: moment.Moment) => {
        setLoading(true);
        getWorklogs(moment(date).startOf("week"), moment(date).endOf("week"))
        .then(worklogs => {
            setWorklogs(worklogs);
            setLoading(false);
        });
    }, [setWorklogs]);

    const onSelectSlot = useCallback((slotInfo: SlotInfo) => {
        setCreateWorklogRange({
            start: moment(slotInfo.start),
            end: moment(slotInfo.end)
        });
        createDialogOpen();
    }, [setCreateWorklogRange, createDialogOpen]);

    const onSelectWorklog = useCallback((worklog: Worklog) => {
        setSelectedWorklog(worklog);
        detailsDialogOpen();
    }, [setSelectedWorklog, detailsDialogOpen]);

    const onCreateWorklog = useCallback((worklog: Worklog) => {
        createDialogClose();
        setLoading(true);
        createWorklog(worklog)
        .then(worklog => {
            setWorklogs((prev) => {
                return [
                    ...prev, worklog
                ];
            });
            setRecentIssues((prev) => {
                const filtered = prev.filter((current) => current.id !== worklog.issueId);
                const trimmed = filtered.slice(0, recentIssuesMaxLength - 1);
                return [
                    {
                        id: worklog.issueId,
                        key: worklog.issueKey,
                        summary: worklog.issueSummary
                    }, ...trimmed
                ];
            });
            setLoading(false);
        });
    }, [createDialogClose, setWorklogs, setRecentIssues]);

    const onWorklogChange = useCallback((worklog: Worklog) => {
        detailsDialogClose();
        setLoading(true);
        updateWorklog(worklog)
        .then(worklog => {
            setWorklogs((prev) => {
                const filtered = prev.filter((current) => current.worklogId !== worklog.worklogId);
                return [
                    ...filtered, worklog
                ];
            });
            setRecentIssues((prev) => {
                const filtered = prev.filter((current) => current.id !== worklog.issueId);
                const trimmed = filtered.slice(0, recentIssuesMaxLength - 1);
                return [
                    {
                        id: worklog.issueId,
                        key: worklog.issueKey,
                        summary: worklog.issueSummary
                    }, ...trimmed
                ];
            });
            setLoading(false);
        });
    }, [detailsDialogClose, setWorklogs, setRecentIssues]);

    const onWorklogDelete = useCallback((worklog: Worklog) => {
        detailsDialogClose();
        setLoading(true);
        deleteWorklog(worklog)
        .then(() => {
            setWorklogs((prev) => {
                const filtered = prev.filter((current) => current.worklogId !== worklog.worklogId);
                return [
                    ...filtered
                ];
            });
            setRecentIssues((prev) => {
                const filtered = prev.filter((current) => current.id !== worklog.issueId);
                const trimmed = filtered.slice(0, recentIssuesMaxLength - 1);
                return [
                    {
                        id: worklog.issueId,
                        key: worklog.issueKey,
                        summary: worklog.issueSummary
                    }, ...trimmed
                ];
            });
            setLoading(false);
        }).catch(console.log);
    }, [detailsDialogClose, setWorklogs, setRecentIssues]);

    useEffect(() => {
        onNavigate(moment());
    }, [onNavigate]);

    return (
        <>
            <span className={loading ? "loading" : ""}>
            <WorklogCalendar worklogs={worklogs}
                onNavigate={onNavigate}
                onWorklogChange={onWorklogChange}
                onSelectSlot={onSelectSlot}
                onSelectWorklog={onSelectWorklog}/>
            </span>
            <CreateDialog opened={createDialogOpened}
                onCancel={createDialogClose}
                onCreate={onCreateWorklog}
                range={createWorklogRange}
                recentIssues={recentIssues}/>
            <DetailsDialog opened={detailsDialogOpened}
                onCancel={detailsDialogClose}
                onUpdate={onWorklogChange}
                onDelete={onWorklogDelete}
                worklog={selectedWorklog}/>
        </>
    );
}

export default App;
