package org.workcraft.plugins.mpsat_verification.commands;

import org.workcraft.Framework;
import org.workcraft.commands.ScriptableDataCommand;
import org.workcraft.gui.MainWindow;
import org.workcraft.interop.Format;
import org.workcraft.plugins.mpsat_verification.tasks.RefinementTask;
import org.workcraft.plugins.mpsat_verification.tasks.VerificationChainResultHandlingMonitor;
import org.workcraft.plugins.mpsat_verification.utils.MpsatUtils;
import org.workcraft.plugins.stg.Stg;
import org.workcraft.plugins.stg.StgModel;
import org.workcraft.plugins.stg.interop.StgFormat;
import org.workcraft.tasks.ProgressMonitor;
import org.workcraft.tasks.TaskManager;
import org.workcraft.utils.WorkUtils;
import org.workcraft.utils.WorkspaceUtils;
import org.workcraft.workspace.ModelEntry;
import org.workcraft.workspace.WorkspaceEntry;

import javax.swing.*;
import java.io.File;

public class RefinementVerificationCommand extends org.workcraft.commands.AbstractVerificationCommand
        implements ScriptableDataCommand<Boolean, File> {

    @Override
    public String getDisplayName() {
        return "Refinement [MPSat]...";
    }

    @Override
    public boolean isApplicableTo(WorkspaceEntry we) {
        return WorkspaceUtils.isApplicable(we, StgModel.class);
    }

    @Override
    public Position getPosition() {
        return Position.BOTTOM_MIDDLE;
    }

    @Override
    public void run(WorkspaceEntry we) {
        MainWindow mainWindow = Framework.getInstance().getMainWindow();
        Format format = StgFormat.getInstance();
        JFileChooser fc = mainWindow.createOpenDialog("Select specification STG file", false, true, format);
        if (fc.showDialog(mainWindow, "OK") == JFileChooser.APPROVE_OPTION) {
            File data = fc.getSelectedFile();
            VerificationChainResultHandlingMonitor monitor = new VerificationChainResultHandlingMonitor(we, true);
            run(we, data, monitor);
        }
    }

    @Override
    public void run(WorkspaceEntry we, File data, ProgressMonitor monitor) {
        // Clone implementation STG as its internal signals will need to be converted to dummies
        ModelEntry me = WorkUtils.cloneModel(we.getModelEntry());
        Stg stg = WorkspaceUtils.getAs(me, Stg.class);

        TaskManager manager = Framework.getInstance().getTaskManager();
        RefinementTask task = new RefinementTask(we, stg, data, getAllowConcurrencyReduction(), getAssumeInputReceptiveness());
        String description = MpsatUtils.getToolchainDescription(we.getTitle());
        manager.queue(task, description, monitor);
    }

    public boolean getAllowConcurrencyReduction() {
        return false;
    }

    public boolean getAssumeInputReceptiveness() {
        return false;
    }

    @Override
    public File deserialiseData(String data) {
        return Framework.getInstance().getFileByAbsoluteOrRelativePath(data);
    }

    @Override
    public Boolean execute(WorkspaceEntry we, File data) {
        VerificationChainResultHandlingMonitor monitor = new VerificationChainResultHandlingMonitor(we, false);
        run(we, data, monitor);
        return monitor.waitForHandledResult();
    }

}
