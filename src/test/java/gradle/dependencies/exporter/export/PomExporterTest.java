package gradle.dependencies.exporter.export;

import gradle.dependencies.exporter.dependencies.Dependency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xmlunit.matchers.CompareMatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PomExporterTest {
    private PomExporter underTest = new PomExporter();

    @TempDir
    File testProjectDir;

    @Test
    void export() {
        File file = new File(testProjectDir, "test-pom.xml");

        underTest.export(file, Arrays.asList(
                new Dependency("group1", "name1", "v1"),
                new Dependency("group1", "name2", "v1")
        ));


        assertThat(readContentsOf(file), CompareMatcher.isIdenticalTo("<project>\n" +
                "  <dependencies>\n" +
                "    <dependency>\n" +
                "      <groupId>group1</groupId>\n" +
                "      <artifactId>name1</artifactId>\n" +
                "      <version>v1</version>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "      <groupId>group1</groupId>\n" +
                "      <artifactId>name2</artifactId>\n" +
                "      <version>v1</version>\n" +
                "    </dependency>\n" +
                "  </dependencies>\n" +
                "</project>"));
    }

    private String readContentsOf(File file) {
        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = String.format("%n");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
// delete the last new line separator
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();

            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}