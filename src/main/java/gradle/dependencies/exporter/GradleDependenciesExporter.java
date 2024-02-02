package gradle.dependencies.exporter;

import gradle.dependencies.exporter.dependencies.Dependency;
import gradle.dependencies.exporter.dependencies.DependencyExtractor;
import gradle.dependencies.exporter.export.PomExporter;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.Path;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class GradleDependenciesExporter implements Plugin<Project> {
    private final DependencyExtractor dependencyExtractor = new DependencyExtractor();
    private final PomExporter pomExporter = new PomExporter();

    @Override
    public void apply(Project project) {
        GradleDependenciesExtension extension = project.getExtensions().create("exportDependencies", GradleDependenciesExtension.class);

        project.getTasks().register("exportDependencies", task -> {
            task.setGroup("dependencies");
            task.setDescription("Generates a pom XML with all dependencies from all configurations");
            File targetDir = new File(
                    new File(project.getRootDir(), extension.getDestinationDir()),
                    project.getGroup() + File.separator + project.getName()
            );
            targetDir.mkdirs();

            Collection<Dependency> dependencies = dependencyExtractor.extract(project);
            File exportedPomFile = new File(targetDir, "pom.xml");
            pomExporter.export(exportedPomFile, dependencies);
            task.getOutputs().file(exportedPomFile);
        });
    }
}
