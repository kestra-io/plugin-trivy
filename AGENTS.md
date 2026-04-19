# Kestra Trivy Plugin

## What

- Provides plugin components under `io.kestra.plugin.trivy.cli`.
- Includes classes such as `TrivyCLI`.

## Why

- What user problem does this solve? Teams need to run Trivy security scans through the CLI from orchestrated workflows instead of relying on manual console work, ad hoc scripts, or disconnected schedulers.
- Why would a team adopt this plugin in a workflow? It keeps Trivy steps in the same Kestra flow as upstream preparation, approvals, retries, notifications, and downstream systems.
- What operational/business outcome does it enable? It reduces manual handoffs and fragmented tooling while improving reliability, traceability, and delivery speed for processes that depend on Trivy.

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
