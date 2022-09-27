package org.workcraft.plugins.circuit.interop;

import org.workcraft.Framework;
import org.workcraft.gui.dialogs.ModalDialog;
import org.workcraft.gui.properties.Properties;
import org.workcraft.gui.properties.PropertyDeclaration;
import org.workcraft.gui.properties.PropertyDescriptor;
import org.workcraft.gui.properties.PropertyEditorTable;
import org.workcraft.plugins.circuit.utils.VerilogUtils;
import org.workcraft.plugins.circuit.verilog.VerilogModule;
import org.workcraft.utils.DialogUtils;
import org.workcraft.utils.FileUtils;
import org.workcraft.utils.GuiUtils;
import org.workcraft.utils.SortUtils;
import org.workcraft.workspace.FileFilters;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;

public class ImportVerilogDialog extends ModalDialog<Collection<VerilogModule>> {

    private VerilogModule topModule;
    private File dir;
    private Map<VerilogModule, String> moduleToFileMap;

    class ModuleFileProperties implements Properties {
        private final List<PropertyDescriptor> properties = new LinkedList<>();

        @Override
        public Collection<PropertyDescriptor> getDescriptors() {
            return properties;
        }

        public void add(VerilogModule module, String name) {
            properties.add(new PropertyDeclaration<>(String.class, name,
                    value -> {
                        if (moduleToFileMap != null) {
                            if ((value == null) || value.isEmpty() || FileFilters.DOCUMENT_EXTENSION.equals(value)) {
                                DialogUtils.showError("File name cannot be empty.");
                            } else {
                                if (!FileFilters.isWorkPath(value)) {
                                    value += FileFilters.DOCUMENT_EXTENSION;
                                }
                                moduleToFileMap.put(module, value);
                            }
                        }
                    },
                    () -> moduleToFileMap == null ? null : moduleToFileMap.get(module)));
        }
    }

    static class ModuleComboBoxRenderer implements ListCellRenderer {
        private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        private final Map<VerilogModule, String> moduleToTextMap = new HashMap<>();

        ModuleComboBoxRenderer(Map<VerilogModule, String> moduleToTextMap) {
            this.moduleToTextMap.putAll(moduleToTextMap);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            String text = moduleToTextMap.get(value);
            return defaultRenderer.getListCellRendererComponent(
                    list, text, index, isSelected, cellHasFocus);
        }
    }

    public ImportVerilogDialog(Window owner, Collection<VerilogModule> modules) {
        super(owner, "Import hierarchical Verilog", modules);
    }

    @Override
    public JPanel createContentPanel() {
        JPanel result = super.createContentPanel();
        result.setLayout(GuiUtils.createBorderLayout());

        Collection<VerilogModule> modules = getUserData();
        Set<VerilogModule> topModules = new HashSet<>(VerilogUtils.getTopModules(modules));

        JComboBox<VerilogModule> topModuleCombo = new JComboBox<>();
        moduleToFileMap = VerilogUtils.getModuleToFileMap(modules);
        topModuleCombo.setRenderer(new ModuleComboBoxRenderer(createModuleToTextMap(modules, topModules)));

        PropertyEditorTable modulesTable = new PropertyEditorTable(
                "Verilog modules to import", "File name");

        Map<VerilogModule, ModuleFileProperties> moduleToPropertiesMap = createModuleToPropertyMap(modules);
        // First add action listener, then populate the ComboBox
        topModuleCombo.addActionListener(l -> {
            topModule = (VerilogModule) topModuleCombo.getSelectedItem();
            modulesTable.assign(moduleToPropertiesMap.get(topModule));
        });

        // Add sorted top modules, followed by sorted instantiated modules to the selection list
        List<VerilogModule> sortedTopModules = SortUtils.getSortedNatural(topModules, m -> m.name);
        sortedTopModules.forEach(topModuleCombo::addItem);
        Set<VerilogModule> instantiatedModules = new HashSet<>(modules);
        instantiatedModules.removeAll(topModules);
        List<VerilogModule> sortedInstantiatedModules = SortUtils.getSortedNatural(instantiatedModules, m -> m.name);
        sortedInstantiatedModules.forEach(topModuleCombo::addItem);

        JPanel topModulePanel = GuiUtils.createLabeledComponent(topModuleCombo,
                "<html>Top module (list starts with not instantiated modules in <b>bold</b>):</html>");

        result.add(topModulePanel, BorderLayout.NORTH);
        result.add(new JScrollPane(modulesTable), BorderLayout.CENTER);
        result.add(createDirectoryPanel(), BorderLayout.SOUTH);

        return result;
    }

    private Map<VerilogModule, String> createModuleToTextMap(Collection<VerilogModule> modules, Set<VerilogModule> topModules) {
        Map<VerilogModule, String> result = new HashMap<>();
        for (VerilogModule module : modules) {
            result.put(module, topModules.contains(module) ? getDecoratedUninstantiatedModuleName(module) : module.name);
        }
        return result;
    }

    private Map<VerilogModule, ModuleFileProperties> createModuleToPropertyMap(Collection<VerilogModule> modules) {
        Map<VerilogModule, ModuleFileProperties> result = new HashMap<>();
        Map<VerilogModule, Set<VerilogModule>> moduleToDescendantsMap = VerilogUtils.getModuleToDescendantsMap(modules);
        for (VerilogModule module : modules) {
            Set<VerilogModule> descendants = moduleToDescendantsMap.getOrDefault(module, new HashSet<>());
            ModuleFileProperties properties = new ModuleFileProperties();
            result.put(module, properties);
            properties.add(module, getDecoratedTopModuleName(module));
            for (VerilogModule descendantModule : SortUtils.getSortedNatural(descendants, m -> m.name)) {
                properties.add(descendantModule, descendantModule.name);
            }
        }
        return result;
    }

    private static String getDecoratedUninstantiatedModuleName(VerilogModule module) {
        return "<html><b>" + module.name + "</b></html>";
    }

    private static String getDecoratedTopModuleName(VerilogModule module) {
        return "<html><font size='-2' color='#888888'>[top]</font> " + module.name + "</html>";
    }

    private JPanel createDirectoryPanel() {
        dir = Framework.getInstance().getLastDirectory();
        JTextField dirText = new JTextField(dir.getPath());
        dirText.setEditable(false);
        JPanel dirPanel = GuiUtils.createLabeledComponent(dirText, "Save directory:");
        JButton dirSelectButton = new JButton("Browse...");
        dirPanel.add(dirSelectButton, BorderLayout.EAST);

        dirSelectButton.addActionListener(l -> {
            JFileChooser fc = DialogUtils.createFileOpener("Select save directory", false, null);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(dir);
            if (DialogUtils.showFileOpener(fc)) {
                dir = fc.getSelectedFile();
                String path = FileUtils.getFullPath(dir);
                dirText.setText(path);
            }
        });
        return dirPanel;
    }

    public VerilogModule getTopModule() {
        return topModule;
    }

    public Map<VerilogModule, String> getModuleFileNames() {
        return Collections.unmodifiableMap(moduleToFileMap);
    }

    public File getDirectory() {
        return dir;
    }

}
