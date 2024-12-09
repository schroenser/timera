## Run with docker compose

`docker-compose.yml`:

```yaml
version: "3.8"
services:
    timera:
        image: schroenser/timera:latest
        ports:
            - 8080:8080
        restart: unless-stopped
        environment:
            JIRA_BASE_URL: <https://jira.example.com>
            JIRA_USER_TOKEN: <my_jira_user_token>
```

## Updating dependencies

1. Determine applicable dependency updates using the following commands:
    1. `mvn versions:display-plugin-updates` for plugins
    2. `mvn versions:display-dependency-updates`
