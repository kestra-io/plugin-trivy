# Kestra Trivy Plugin

## What

- Provides plugin components under `io.kestra.plugin.trivy.cli`.
- Includes classes such as `TrivyCLI`.

## Why

- This plugin integrates Kestra with Trivy.
- It provides tasks that execute arbitrary Trivy CLI commands.

## How

### Architecture

Single-module plugin. Source packages under `io.kestra.plugin`:

- `trivy`

### Key Plugin Classes

- `io.kestra.plugin.trivy.cli.TrivyCLI`

### Project Structure

```
plugin-trivy/
├── src/main/java/io/kestra/plugin/trivy/cli/
├── src/test/java/io/kestra/plugin/trivy/cli/
├── build.gradle
└── README.md
```

## References

- https://kestra.io/docs/plugin-developer-guide
- https://kestra.io/docs/plugin-developer-guide/contribution-guidelines
