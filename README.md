# ScriptExecutor

![Java CI](https://github.com/jack4545/ScriptExecutor/workflows/Java%20CI/badge.svg)

A Minecraft Spigot Server plugin that adds the ability to execute commands/scripts on the native system. 
Supported Minecraft versions: **1.12.2-1.16.+**

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
- [X] Automation!
- [ ] Wiki
- [ ] More advanced permissions
- [ ] Performance Optimizations
 
### Technology
This plugin is 100% written in Kotlin, an up-and-coming language that can compile into Java JVM byte code.

### How to Build
Clone the repository and run one of the following commands: 
- Linux/Unix: `./gradlew build`
- Windows: `gradlew.bat build`

### How to Download
Prebuilt jar files are available on github in the releases section [here](https://github.com/w4545/ScriptExecutor/releases).

### How to Install

Place the produced jar file into the plugins folder and run the server. This will generate all files - including a config file - 
into a newly created ScriptExecutor directory found in the plugins folder.
