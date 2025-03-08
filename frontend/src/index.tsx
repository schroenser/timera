import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import "@mantine/core/styles.css";
import {MantineProvider} from "@mantine/core";
import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root") as HTMLElement);
root.render(<React.StrictMode>
    <MantineProvider>
        <App/>
    </MantineProvider>
</React.StrictMode>);
