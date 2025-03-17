## Maven commands

* `atlas-mvn clean` cleans the `target` folder
* `atlas-mvn package` packages the application.
  If the product is running, the plugin is automatically refreshed.
* `atlas-mvn sortpom:sort` sorts the pom file automatically.
* `atlas-mvn versions:display-plugin-updates` shows available version updates for the maven plugins.
* `atlas-mvn versions:display-dependency-updates` shows available version updates for the maven dependencies.

## Atlas commands

* `atlas-run` installs this plugin into the product and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-help` prints description for all commands in the SDK

Full documentation: https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK
