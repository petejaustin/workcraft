package org.workcraft.plugins.mpsat_verification.tasks;

import org.workcraft.plugins.mpsat_verification.utils.CompositionUtils;
import org.workcraft.plugins.mpsat_verification.utils.MpsatUtils;
import org.workcraft.plugins.pcomp.ComponentData;
import org.workcraft.plugins.pcomp.CompositionData;
import org.workcraft.plugins.pcomp.tasks.PcompOutput;
import org.workcraft.plugins.stg.StgModel;
import org.workcraft.tasks.ExportOutput;
import org.workcraft.traces.Solution;
import org.workcraft.traces.Trace;
import org.workcraft.utils.LogUtils;
import org.workcraft.workspace.WorkspaceEntry;

import java.util.*;

class OutputDeterminacyOutputInterpreter extends AbstractCompositionOutputInterpreter {

    OutputDeterminacyOutputInterpreter(WorkspaceEntry we, ExportOutput exportOutput,
            PcompOutput pcompOutput, MpsatOutput mpsatOutput, boolean interactive) {

        super(we, exportOutput, pcompOutput, mpsatOutput, interactive);
    }

    @Override
    public List<Solution> processSolutions(List<Solution> solutions) {
        StgModel compositionStg = getCompositionStg();
        CompositionData compositionData = getCompositionData();
        ComponentData devData = compositionData.getComponentData(0);
        ComponentData envData = compositionData.getComponentData(1);

        List<Solution> result = new LinkedList<>();
        for (Solution solution : solutions) {
            Solution compositionSolution = MpsatUtils.fixSolutionToggleEvents(compositionStg, solution);
            Trace compositionTrace = compositionSolution.getMainTrace();
            LogUtils.logMessage("Violation trace of the auto-composition: " + compositionTrace);

            Trace devTrace = CompositionUtils.projectTrace(compositionTrace, devData);
            Trace envTrace = CompositionUtils.projectTrace(compositionTrace, envData);
            LogUtils.logMessage("Projected pair of traces:\n    " + devTrace + "\n    " + envTrace);

            Set<Trace> compositionContinuations = compositionSolution.getContinuations();
            Map<String, Trace> devEnabledness = CompositionUtils.getEnabledness(compositionContinuations, devData);
            Map<String, Trace> envEnabledness = CompositionUtils.getEnabledness(compositionContinuations, envData);
            Set<String> nondeterministicEnabledSignals = new HashSet<>(devEnabledness.keySet());
            nondeterministicEnabledSignals.removeAll(envEnabledness.keySet());

            result.addAll(CompositionUtils.getExtendedViolatorSolutions(devTrace, nondeterministicEnabledSignals,
                    devEnabledness, "Non-deterministic enabling of signal"));
        }
        return result;
    }

}
