package gradle.dependencies.exporter;

public class GradleDependenciesExtension {
    private String destinationDir = "build/dependencies";

    public String getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
    }
}
