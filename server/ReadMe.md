# server

## Project Setup

The build tool for this project is Gradle.

### eclipse

1. Create a new empty workspace
2. Import the project using the option _Existing Gradle project_
3. Start the application the first time by right-clicking on `AuthenticationServiceApplication` and choose _Debug As / Java Application_  
   The first run will not result in a running application. But it will generate the Debug configuration
4. Open the Debug configuration and under _Arguments_ / _Program arguments_ add the following two arguments:

```
server
config.yml
```

In the same window under _VM arguments_ add the following argument:

```
-Ddw.tokenSecret=DEBUGDEBUGDEBUGDEBUGDEBUGDEBUGDEBUG
```

`-Ddw.tokenSecret` will override the default config value of _tokenSecret_ which is to short in the example config. This is on purpose so the server fails to start if a user chooses an insecure (short) value for this parameter.

### Visual Studio Code

The `launch.json` in the _server_ folder already contains all neccessary configurations.

## Build

To build a fat jar of the project the [shadow](https://github.com/johnrengelman/shadow) plugin is used. It is configured in the _build.gradle_ under _ shadowJar_.

To build the jar run the `shadowJar` Gradle task. The result will be placed in `build/libs`.

## Run

To run the fat jar two arguments have to be provided. The first is _server_ to let dropwizard know it is running as a server and the second is the path to the config. If the config is placed in the same folder as the jar the full command would look like this: `java -jar authentication-service.jar server config.yml`.
