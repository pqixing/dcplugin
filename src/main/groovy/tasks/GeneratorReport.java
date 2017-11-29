package tasks;

import org.gradle.api.tasks.diagnostics.DependencyReportTask;

import java.io.File;

/**
 * Created by pqixing on 17-11-29.
 */

public class GeneratorReport extends DependencyReportTask {
    public GeneratorReport(){
        setGroup("android");
    }

    @Override
    public void setOutputFile(File outputFile) {
        super.setOutputFile(outputFile);
    }
}
