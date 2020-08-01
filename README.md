# ScriptExecutor

A Minecraft Spigot Server plugin that adds the ability to execute commands/scripts on the native system. 

### Features
 * Specify commands
 * Working directory
 * Generate log files
 * Wrap output of process to player/console
 * Permissions (Basic)  
 * Advanced configurations:
   * Specify multiple configurations
   * Infers configurations based on context (Player/Console)
   * Configuration hierarchy allows easier and quicker setup

### Coming Soon
 * Automation!
 * More advanced permissions
 
### Technology
This plugin is 100% written in Kotlin, an up-and-coming language that can compile into Java JVM byte code.

### How to Build
```shell script
./gradlew build
``` 

### How to Install

Place the produced jar file into the plugins folder and run the server.