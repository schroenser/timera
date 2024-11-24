import "./App.css";
import {useEffect, useState} from "react";

function App() {
    let [content, setContent] = useState("");

    useEffect(() => {
        fetch("/api/v1/hello")
        .then(response => response.text())
        .then(text => setContent(text));
    }, [setContent]);

    return (
        <div className="App">
            <header className="App-header">
                <p>{content}</p>
            </header>
        </div>
    );
}

export default App;
