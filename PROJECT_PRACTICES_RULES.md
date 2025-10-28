# Project Practices Rules - MN4 CLI Exemplar

This document outlines the project practices and conventions used in the mn4-cli-exemplar project, serving as a template for similar Micronaut CLI applications.

## 1. Maven Build File Practices

### 1.1 Project Structure and Parent
- **Use Micronaut Platform Parent**: Always inherit from `io.micronaut.platform:micronaut-parent` to ensure consistent dependency management and plugin versions
- **Version Alignment**: Keep `micronaut.version` property aligned with the parent version (currently 4.8.2)
- **Packaging Flexibility**: Use `${packaging}` property for flexible packaging (defaults to `jar`)

```xml
<parent>
  <groupId>io.micronaut.platform</groupId>
  <artifactId>micronaut-parent</artifactId>
  <version>4.8.2</version>
</parent>

<properties>
  <packaging>jar</packaging>
  <micronaut.version>4.8.2</micronaut.version>
</properties>
```

### 1.2 Java Version Configuration
- **Target Java 21**: Use Java 21 as the target version for modern CLI applications
- **Consistent Version Properties**: Define `jdk.version`, `source.version`, and `release.version` consistently

```xml
<properties>
  <jdk.version>21</jdk.version>
  <source.version>21</source.version>
  <release.version>21</release.version>
</properties>
```

### 1.3 Dependency Management
- **Core Micronaut Dependencies**: Include essential Micronaut modules:
  - `micronaut-picocli` for CLI functionality
  - `micronaut-inject` for dependency injection
  - `micronaut-serde-jackson` for JSON serialization
  - `micronaut-validation` for input validation
- **Runtime Dependencies**: Use `runtime` scope for logging and YAML processing
- **Provided Dependencies**: Use `provided` scope for Lombok to avoid runtime inclusion
- **Custom Libraries**: Include project-specific libraries (e.g., `endeavour` for outcome handling)

### 1.4 Annotation Processing Configuration
- **Comprehensive Processor Paths**: Configure all necessary annotation processors:
  - Lombok for code generation
  - Micronaut Inject Java for DI
  - Picocli Codegen for CLI generation
  - Micronaut Graal for native compilation
  - Micronaut Serde Processor for serialization
  - Micronaut Validation Processor for validation
- **Processor Exclusions**: Exclude conflicting dependencies in processor paths
- **Processing Configuration**: Set Micronaut processing group and module properties

```xml
<annotationProcessorPaths combine.self="override">
  <path>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
  </path>
  <!-- Additional processors... -->
</annotationProcessorPaths>
<compilerArgs>
  <arg>-Amicronaut.processing.group=org.saltations.mn4</arg>
  <arg>-Amicronaut.processing.module=mn4-cli-exemplar</arg>
</compilerArgs>
```

### 1.5 Plugin Configuration
- **Micronaut Maven Plugin**: Use for application packaging and execution
- **Maven Enforcer Plugin**: Include but disable by default (set phase to `none`)
- **Compiler Plugin**: Configure with annotation processors and compiler arguments

## 2. Java Library Usage Practices

### 2.1 Core Framework Libraries
- **Micronaut Framework**: Use Micronaut 4.x for dependency injection, configuration, and HTTP client capabilities
- **Picocli Integration**: Leverage `micronaut-picocli` for seamless CLI command definition and execution
- **Validation**: Use `micronaut-validation` with Jakarta Validation API for input validation
- **Serialization**: Use `micronaut-serde-jackson` for JSON processing

### 2.2 Utility Libraries
- **Lombok**: Use for reducing boilerplate code with `@Slf4j`, `@Getter`, etc.
- **Logback**: Use for logging with SLF4J API
- **SnakeYAML**: Use for YAML configuration processing (pin to safe version 2.3+)
- **JUnit 5**: Use for testing with Micronaut Test integration

### 2.3 Custom Libraries
- **Endeavour Framework**: Use for consistent outcome handling with `Outcome<T>` and `FailureType` patterns
- **HTTP Client**: Use Micronaut HTTP client with JDK implementation for external API calls

### 2.4 Library Version Management
- **BOM Inheritance**: Rely on Micronaut parent for version management
- **Explicit Versions**: Only specify versions for libraries not managed by the parent (e.g., SnakeYAML)
- **Security Considerations**: Pin vulnerable dependencies to safe versions

## 3. Command and Subcommand Coding Practices

### 3.1 Command Structure
- **Root Command**: Implement `Callable<Outcome<Integer>>` for consistent return handling
- **Subcommands**: Define as separate classes implementing the same interface
- **Command Registration**: Register subcommands in the root command's `@Command` annotation

```java
@Command(name = "mn4-cli-exemplar", 
        description = "Exemplar application of preferred best practices",
        mixinStandardHelpOptions = true,
        subcommands = {
            TZCommand.class,
            IPCommand.class
        })
public class Mn4CliExemplarCommand implements Callable<Outcome<Integer>> {
    // Implementation
}
```

### 3.2 Application Context Management
- **Context Lifecycle**: Use try-with-resources for proper ApplicationContext management
- **Bean Creation**: Use `ctx.createBean()` for command instantiation
- **Factory Integration**: Use `MicronautFactory` for Picocli integration

```java
try (ApplicationContext ctx = ApplicationContext.run()) {
    var root = ctx.createBean(Mn4CliExemplarCommand.class);
    var factory = new MicronautFactory(ctx);
    var cmdLine = new CommandLine(root, factory);
    // Execute commands
}
```

### 3.3 Custom Execution Strategy
- **Outcome-Based Execution**: Implement custom `IExecutionStrategy` for consistent outcome handling
- **Validation Integration**: Integrate Micronaut validation with command execution
- **Error Handling**: Use `Outcome<T>` pattern for consistent success/failure handling
- **Help Request Handling**: Properly handle help requests before validation

```java
@Singleton
public class RunAllStopOnError implements IExecutionStrategy {
    // Custom execution logic with validation and outcome handling
}
```

### 3.4 Command Implementation Patterns
- **Outcome Return**: All commands must return `Outcome<Integer>` for consistent error handling
- **Logging**: Use `@Slf4j` for logging in command classes
- **Validation**: Use Jakarta Validation annotations for input validation
- **Help Options**: Include `mixinStandardHelpOptions = true` for automatic help generation

```java
@Slf4j
@Command(name = "tz", description = "Timezone query command")
public class TZCommand implements Callable<Outcome<Integer>> {
    @Override
    public Outcome<Integer> call() {
        return Outcomes.succeed(0);
    }
}
```

### 3.5 Failure Handling
- **Failure Types**: Define custom `FailureType` enums for different failure scenarios
- **Outcome Creation**: Use `Outcomes.succeed()` and `Outcomes.fail()` for consistent outcome creation
- **Error Propagation**: Let the execution strategy handle error logging and exit codes

```java
@Getter
public enum ExemplarFailureType implements FailureType {
    GENERIC("generic-failure", "A generic failure occurred"),
    GENERIC_EXCEPTION("generic-exception-failure", "An exception occurred: {0}");
}
```

### 3.6 Testing Practices
- **Integration Testing**: Use `PicocliRunner` for command testing
- **Context Configuration**: Use appropriate environments (CLI, TEST) for testing
- **Output Verification**: Capture and verify command output in tests
- **Micronaut Test**: Leverage Micronaut Test framework for integration testing

```java
@Test
public void testWithCommandLineOption() throws Exception {
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
        String[] args = new String[] { "-v" };
        Outcome<Integer> outcome = PicocliRunner.call(Mn4CliExemplarCommand.class, ctx, args);
        // Assertions
    }
}
```

### 3.7 Configuration Management
- **YAML Configuration**: Use `application.yml` for application configuration
- **HTTP Client Configuration**: Configure external service endpoints and timeouts
- **Environment-Specific**: Use Micronaut environments for different deployment scenarios

```yaml
micronaut:
  application:
    name: mn4-cli-exemplar
  http:
    services:
      worldtimeapi:
        url: "https://worldtimeapi.org/api"
        client-configuration:
          read-timeout: 10s
```

## 4. General Best Practices

### 4.1 Code Organization
- **Package Structure**: Use clear package hierarchy (e.g., `org.saltations.mn4`)
- **Separation of Concerns**: Separate commands, services, and configuration
- **Dependency Injection**: Leverage Micronaut's DI for loose coupling

### 4.2 Error Handling
- **Consistent Patterns**: Use `Outcome<T>` pattern throughout the application
- **No Logging in Catch Blocks**: Delegate logging responsibility to callers [[memory:193440]]
- **Graceful Degradation**: Handle validation failures gracefully

### 4.3 Documentation
- **Command Descriptions**: Provide clear descriptions for all commands and options
- **Code Comments**: Document complex logic and custom execution strategies
- **README**: Include setup and usage instructions

This document serves as a comprehensive guide for maintaining consistency and best practices in Micronaut CLI applications following the patterns established in the mn4-cli-exemplar project.
