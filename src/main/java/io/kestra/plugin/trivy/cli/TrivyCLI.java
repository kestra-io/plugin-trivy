package io.kestra.plugin.trivy.cli;

import io.kestra.core.exceptions.IllegalVariableEvaluationException;
import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.property.Property;
import io.kestra.core.models.tasks.RunnableTask;
import io.kestra.core.models.tasks.runners.TargetOS;
import io.kestra.core.runners.RunContext;
import io.kestra.plugin.scripts.exec.AbstractExecScript;
import io.kestra.plugin.scripts.exec.scripts.models.DockerOptions;
import io.kestra.plugin.scripts.exec.scripts.models.ScriptOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
@Schema(
    title = "Execute Trivy CLI commands in Docker",
    description = "Runs the provided Trivy CLI commands via the script runner. Defaults to Docker image `aquasec/trivy:latest` when no image override is set; ensure credentials and volume mounts needed by Trivy are available in the container."
)
@Plugin(
    examples = {
        @Example(
            full = true,
            title = "Scan an image using Trivy CLI.",
            code = """
                id: trivy_cli_image
                namespace: company.team

                tasks:
                  - id: trivy_image
                    type: io.kestra.plugin.trivy.cli.TrivyCLI
                    commands:
                      - trivy image --format json nginx:latest
                """
        ),
        @Example(
            full = true,
            title = "Scan IaC with Trivy CLI.",
            code = """
                id: trivy_cli_iac
                namespace: company.team

                tasks:
                  - id: trivy_iac
                    type: io.kestra.plugin.trivy.cli.TrivyCLI
                    commands:
                      - trivy config --format table /workspace/infrastructure
                """
        )
    }
)
public class TrivyCLI extends AbstractExecScript implements RunnableTask<ScriptOutput> {
    private static final String DEFAULT_IMAGE = "aquasec/trivy:latest";

    @Builder.Default
    protected Property<String> containerImage = Property.ofValue(DEFAULT_IMAGE);

    @Schema(
        title = "Trivy CLI commands to execute",
        description = "Commands are executed in order by the task runner; provide full Trivy subcommands such as `trivy image ...` or `trivy config ...`."
    )
    @NotNull
    protected Property<List<String>> commands;

    @Override
    protected DockerOptions injectDefaults(RunContext runContext, DockerOptions original) throws IllegalVariableEvaluationException {
        var builder = original.toBuilder();
        if (original.getImage() == null) {
            builder.image(runContext.render(this.getContainerImage()).as(String.class).orElse(null));
        }
        return builder.build();
    }

    @Override
    public ScriptOutput run(RunContext runContext) throws Exception {
        TargetOS os = runContext.render(this.targetOS).as(TargetOS.class).orElse(null);

        return this.commands(runContext)
            .withInterpreter(this.interpreter)
            .withBeforeCommands(beforeCommands)
            .withBeforeCommandsWithOptions(true)
            .withCommands(commands)
            .withTargetOS(os)
            .run();
    }
}
