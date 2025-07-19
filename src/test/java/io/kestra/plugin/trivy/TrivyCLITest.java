package io.kestra.plugin.trivy;

import com.google.common.collect.ImmutableMap;
import io.kestra.core.junit.annotations.KestraTest;
import io.kestra.core.models.executions.LogEntry;
import io.kestra.core.models.property.Property;
import io.kestra.core.queues.QueueFactoryInterface;
import io.kestra.core.queues.QueueInterface;
import io.kestra.core.runners.RunContext;
import io.kestra.core.runners.RunContextFactory;
import io.kestra.core.utils.TestsUtils;
import io.kestra.plugin.trivy.cli.TrivyCLI;
import io.kestra.plugin.scripts.exec.scripts.models.ScriptOutput;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@KestraTest
public class TrivyCLITest {
    @Inject
    RunContextFactory runContextFactory;

    @Inject
    @Named(QueueFactoryInterface.WORKERTASKLOG_NAMED)
    private QueueInterface<LogEntry> logQueue;

    @Test
    void trivyImageScan() throws Exception {
        List<LogEntry> logs = new ArrayList<>();
        Flux<LogEntry> receive = TestsUtils.receive(logQueue, l -> logs.add(l.getLeft()));

        var trivyTask = TrivyCLI.builder()
            .id(TrivyCLI.class.getSimpleName())
            .type(TrivyCLI.class.getName())
            .commands(Property.ofValue(List.of(
                "trivy image --format table alpine:3.18"
            )))
            .build();

        RunContext runContext = TestsUtils.mockRunContext(runContextFactory, trivyTask, ImmutableMap.of());
        ScriptOutput run = trivyTask.run(runContext);

        assertThat(run.getExitCode(), is(0));

        TestsUtils.awaitLog(logs, log ->
            log.getMessage() != null &&
                log.getMessage().toLowerCase().contains("alpine")
        );
        receive.blockLast();

        assertThat(
            logs.stream().anyMatch(log ->
                log.getMessage() != null &&
                    log.getMessage().toLowerCase().contains("alpine")
            ),
            is(true)
        );
    }
}
