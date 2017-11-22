package model
class DachenModel {
    boolean testEnv = true
    boolean library = true
    String pom_version = "0.1.0"

    @Override
    public String toString() {
        return "DachenModel{   " +
                "testEnv=" + testEnv +
                ", library=" + library +
                ", pom_version='" + pom_version + '\'' +
                '}';
    }
}