package gradle.dependencies.exporter.dependencies;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;

import java.util.*;

public class DependencyExtractor {
    public Collection<Dependency> extract(Project project) {
        Set<Dependency> result = new HashSet<>();
        project.getConfigurations().stream().filter(Objects::nonNull)
                .filter(Configuration::isCanBeResolved)
                .forEach(config -> {
                    Optional.ofNullable(config.getResolvedConfiguration())
                            .flatMap(it -> Optional.of(it.getResolvedArtifacts()))
                            .ifPresent(artifacts -> {
                                artifacts.forEach(artifact -> {
                                    ComponentIdentifier componentIdentifier = artifact.getId().getComponentIdentifier();
                                    if (componentIdentifier instanceof ModuleComponentIdentifier) {
                                        ModuleComponentIdentifier module = (ModuleComponentIdentifier) componentIdentifier;
                                        result.add(new Dependency(
                                                module.getGroup(),
                                                module.getModule(),
                                                module.getVersion()
                                        ));
                                    }
                                });
                            });
                });

        return result;
    }
}
