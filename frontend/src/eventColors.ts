import {Event} from "react-big-calendar";

const calculateHash = (string: string) => {
    let hash = 0;
    for (let i = 0; i < string.length; i++) {
        hash =
            string.charCodeAt(i) +
            (
                (
                    hash << 5
                ) - hash
            );
        hash = hash & hash;
    }
    return hash;
};

const inRange = (hash: number, min: number, max: number) => {
    const diff = max - min;
    const x = (
        (
            hash % diff
        ) + diff
    ) % diff;
    return x + min;
};

const toHsl = (string: string) => {
    const hash = calculateHash(string);
    const h = inRange(hash, 0, 360);
    const s = inRange(hash, 40, 60);
    const l = inRange(hash, 40, 60);
    return `hsl(${h}, ${s}%, ${l}%)`;
};

export function eventColors(event: Event) {
    return {
        color: "white",
        backgroundColor: toHsl(event.resource.issueKey as string)
    };
}
