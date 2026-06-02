# How to use the Trivy plugin

Run Trivy security scans — container images, filesystems, configs, and more — from Kestra flows inside a container.

## Common properties

`containerImage` defaults to `ghcr.io/aquasecurity/trivy`. Pin a specific version by appending a tag. `taskRunner` controls where the container runs — defaults to Docker.

## Tasks

`cli.TrivyCLI` runs one or more Trivy CLI commands set in `commands` (e.g. `trivy image alpine:latest`, `trivy config ./`, `trivy fs --exit-code 1 .`). Use `beforeCommands` for setup steps such as pulling config files. Pass supporting files via `inputFiles` or pull them from [namespace files](https://kestra.io/docs/concepts/namespace-files). Pass registry credentials or other secrets as environment variables via `env` — store sensitive values in [secrets](https://kestra.io/docs/concepts/secret).
