import {Event} from "react-big-calendar";

const palette = [
    {
        color: "#ffffff",
        backgroundColor: "#e6194B"
    }, {
        color: "#ffffff",
        backgroundColor: "#3cb44b"
    }, {
        color: "#000000",
        backgroundColor: "#ffe119"
    }, {
        color: "#ffffff",
        backgroundColor: "#4363d8"
    }, {
        color: "#ffffff",
        backgroundColor: "#f58231"
    }, {
        color: "#ffffff",
        backgroundColor: "#911eb4"
    }, {
        color: "#000000",
        backgroundColor: "#42d4f4"
    }, {
        color: "#ffffff",
        backgroundColor: "#f032e6"
    }, {
        color: "#000000",
        backgroundColor: "#bfef45"
    }, {
        color: "#000000",
        backgroundColor: "#fabed4"
    }, {
        color: "#ffffff",
        backgroundColor: "#469990"
    }, {
        color: "#000000",
        backgroundColor: "#dcbeff"
    }, {
        color: "#ffffff",
        backgroundColor: "#9A6324"
    }, {
        color: "#000000",
        backgroundColor: "#fffac8"
    }, {
        color: "#ffffff",
        backgroundColor: "#800000"
    }, {
        color: "#000000",
        backgroundColor: "#aaffc3"
    }, {
        color: "#ffffff",
        backgroundColor: "#808000"
    }, {
        color: "#000000",
        backgroundColor: "#ffd8b1"
    }, {
        color: "#ffffff",
        backgroundColor: "#000075"
    }, {
        color: "#ffffff",
        backgroundColor: "#a9a9a9"
    }
];

export function eventColors(event: Event) {
    return palette[event.resource.issueId % palette.length];
}
