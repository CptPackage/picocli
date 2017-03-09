# picoCLI - a mighty tiny Command Line Interpreter

A CLI framework in a single file, designed to be easy to include in your application _in source form_:
allowing you to parse command line arguments without dragging in an external dependency.

How it works: annotate your class and picoCLI initializes it from the command line arguments,
converting the input to strongly typed data. Any parameter prefix style works,
with special support for POSIX-style short groupable options.
Generates online "usage" help. Can be configured to make parameter matching case-insensitive,
and supports abbreviated options when unambiguous. Works with Java 5 or higher.

##Example

Annotate fields with the command line parameter names and description.

```java
import picocli.CommandLine.Parameter;
import java.io.File;

public class MyApplication {
    @Parameter(names = { "--failfast", "-f" }, description = "Stop on the first failure.")
    private boolean failFast = true;

    @Parameter(names = { "--in", "-i" }, description = "Specifies the input file.", required = true)
    private File inputFile;

    @Parameter(names = { "--out", "-o" }, description = "Specifies the output file.", required = true)
    private File outputFile;

    @Parameter(names = { "--verbose", "-v" }, description = "Be verbose.")
    private boolean verbose = false;
    ...
}
```

Then invoke `CommandLine.parse` with the command line parameter and an object you want to initialize.

```java
MyApplication app = new MyApplication();
CommandLine.parse(app, "--in", "path/to/inputFile", "--out", "path/to/outputFile");
assert app.inputFile != null;
```

