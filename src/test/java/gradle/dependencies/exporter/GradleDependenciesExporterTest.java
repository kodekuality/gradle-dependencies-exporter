package gradle.dependencies.exporter;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GradleDependenciesExporterTest {
    @TempDir
    File testProjectDir;
    private File settingsFile;
    private File buildFile;

    @BeforeEach
    public void setUp() throws Exception {
        settingsFile = new File(testProjectDir, "settings.gradle");
        buildFile = new File(testProjectDir, "build.gradle");
    }

    @Test
    public void noExtensionProvided() throws IOException {
        writeFile(settingsFile, "rootProject.name = 'hello-world'");
        writeFile(buildFile, "plugins { id 'gradle.dependencies.exporter'\nid 'java' }\n" +
                "repositories {\n" +
                "  mavenCentral()\n" +
                "}\n" +
                "dependencies {" +
                "  implementation 'junit:junit:4.12'" +
                "}");

        // Run the Gradle build
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .withDebug(true)
                .withArguments(
                        "exportDependencies"
                )
                .build();

        System.out.println(testProjectDir);
        System.out.println(result.getOutput());
        System.out.println(result.task(":exportDependencies").getOutcome());
    }


    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}