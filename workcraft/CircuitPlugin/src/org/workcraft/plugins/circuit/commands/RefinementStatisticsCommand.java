package org.workcraft.plugins.circuit.commands;

import org.workcraft.Framework;
import org.workcraft.commands.AbstractStatisticsCommand;
import org.workcraft.gui.properties.PropertyHelper;
import org.workcraft.plugins.circuit.Circuit;
import org.workcraft.plugins.circuit.refinement.RefinementDependencyGraph;
import org.workcraft.utils.FileUtils;
import org.workcraft.utils.SortUtils;
import org.workcraft.utils.TextUtils;
import org.workcraft.utils.WorkspaceUtils;
import org.workcraft.workspace.ModelEntry;
import org.workcraft.workspace.WorkspaceEntry;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RefinementStatisticsCommand extends AbstractStatisticsCommand {

    private static final String FILE_INDENT = "    ";

    @Override
    public String getDisplayName() {
        return "Refinement analysis";
    }

    @Override
    public boolean isApplicableTo(WorkspaceEntry we) {
        return WorkspaceUtils.isApplicable(we, Circuit.class);
    }

    @Override
    public String getStatistics(WorkspaceEntry we) {
        File topCircuitFile = Framework.getInstance().getWorkspace().getFile(we);

        Circuit circuit = WorkspaceUtils.getAs(we, Circuit.class);
        File topEnvFile = circuit == null ? null : circuit.getEnvironmentFile();

        RefinementDependencyGraph rdg = new RefinementDependencyGraph(we);
        Set<File> stgFiles = rdg.getStgFiles();
        stgFiles.remove(topCircuitFile);

        Set<File> circuitFiles = rdg.getCircuitFiles();
        circuitFiles.remove(topCircuitFile);

        Set<File> extraEnvFiles = getEnvFiles(rdg);
        extraEnvFiles.removeAll(stgFiles);
        extraEnvFiles.removeAll(circuitFiles);
        extraEnvFiles.remove(topEnvFile);
        extraEnvFiles.remove(topCircuitFile);

        return "Refinement analysis:\n"
                + getFileName("top-level Circuit", topCircuitFile)
                + getFileName("top-level environment", topEnvFile)
                + getFileNameList("Circuit dependencies", circuitFiles)
                + getFileNameList("STG dependencies", stgFiles)
                + getFileNameList("additional STG environments", extraEnvFiles);
    }

    private Set<File> getEnvFiles(RefinementDependencyGraph rdg) {
        Set<File> result = new HashSet<>();
        for (File circuitFile : rdg.getCircuitFiles()) {
            ModelEntry me = rdg.getModelEntry(circuitFile);
            Circuit circuit = WorkspaceUtils.getAs(me, Circuit.class);
            if (circuit != null) {
                File envFile = circuit.getEnvironmentFile();
                if (envFile != null) {
                    result.add(envFile);
                }
            }
        }
        return result;
    }
    private String getFileName(String title, File file) {
        if (file == null) {
            return PropertyHelper.BULLET_PREFIX + "No " + title + "\n";
        }
        return  PropertyHelper.BULLET_PREFIX + TextUtils.makeFirstCapital(title) + ":"
                + "\n" + FILE_INDENT + FileUtils.getFullPath(file) + "\n";
    }

    private String getFileNameList(String title, Collection<File> files) {
        if ((files == null) || files.isEmpty()) {
            return PropertyHelper.BULLET_PREFIX + "No " + title + "\n";
        }
        if (files.size() == 1) {
            File file = files.iterator().next();
            return PropertyHelper.BULLET_PREFIX + TextUtils.makeFirstCapital(title) + ":"
                    + "\n" + FILE_INDENT + FileUtils.getFullPath(file) + "\n";
        }
        return PropertyHelper.BULLET_PREFIX + files.size() + " " + title + ":\n" + FILE_INDENT + files.stream()
                .map(FileUtils::getFullPath)
                .sorted(SortUtils::compareNatural)
                .collect(Collectors.joining("\n" + FILE_INDENT)) + "\n";
    }

}
