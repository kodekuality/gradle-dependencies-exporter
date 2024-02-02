package gradle.dependencies.exporter.export;

import gradle.dependencies.exporter.dependencies.Dependency;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PomExporter {
    public void export (File file, Collection<Dependency> dependencies) {
        writeToFile(file, "<project>\n" +
                "  <dependencies>\n" +
                generateDependenciesXml(dependencies) +
                "  </dependencies>\n" +
                "</project>");
    }

    private String generateDependenciesXml(Collection<Dependency> dependencies) {
        return dependencies.stream().map(
                dependency -> "    <dependency>\n" +
                        "      <groupId>"+dependency.getGroup()+"</groupId>\n" +
                        "      <artifactId>"+dependency.getName()+"</artifactId>\n" +
                        "      <version>"+dependency.getVersion()+"</version>\n" +
                        "    </dependency>\n"
        ).collect(Collectors.joining());
    }

    private void writeToFile(File file, String content) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
