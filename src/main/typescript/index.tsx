import React from "react";
import {createRoot} from "react-dom/client";
import "./index.css";
import "@mantine/core/styles.css";
import {MantineProvider} from "@mantine/core";
import App from "./App";

let domNode: HTMLElement = null;

document.addEventListener("DOMContentLoaded", () => {
    if (!domNode) {
        domNode = document.getElementById("root");
        const root = createRoot(domNode);
        root.render(<React.StrictMode>
            <MantineProvider>
                <App/>
            </MantineProvider>
        </React.StrictMode>);
    }
});
