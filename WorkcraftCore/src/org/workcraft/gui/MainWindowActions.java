package org.workcraft.gui;

import org.workcraft.Framework;
import org.workcraft.Info;
import org.workcraft.exceptions.OperationCancelledException;
import org.workcraft.gui.actions.Action;
import org.workcraft.gui.editor.GraphEditorPanel;
import org.workcraft.gui.tools.GraphEditor;
import org.workcraft.plugins.builtin.settings.CommonEditorSettings;
import org.workcraft.plugins.builtin.settings.CommonVisualSettings;
import org.workcraft.utils.DesktopApi;
import org.workcraft.utils.FileUtils;
import org.workcraft.utils.LogUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;

public class MainWindowActions {

    private static GraphEditor getCurrentEditor() {
        final Framework framework = Framework.getInstance();
        final MainWindow mainWindow = framework.getMainWindow();
        return mainWindow.getCurrentEditor();
    }

    private static void repaintAndFocusCurrentEditor() {
        final GraphEditor editor = getCurrentEditor();
        if (editor != null) {
            editor.repaint();
            editor.requestFocus();
        }
    }

    public static final Action CREATE_WORK_ACTION = new Action("Create work...",
            KeyStroke.getKeyStroke(KeyEvent.VK_N, DesktopApi.getMenuKeyMask()),
            () -> {
                try {
                    Framework.getInstance().getMainWindow().createWork();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action OPEN_WORK_ACTION = new Action("Open work...",
            KeyStroke.getKeyStroke(KeyEvent.VK_O, DesktopApi.getMenuKeyMask()),
            () -> {
                try {
                    Framework.getInstance().getMainWindow().openWork();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action MERGE_WORK_ACTION = new Action("Merge work...",
            () -> {
                try {
                    Framework.getInstance().getMainWindow().mergeWork();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action SAVE_WORK_ACTION = new Action("Save work",
            KeyStroke.getKeyStroke(KeyEvent.VK_S, DesktopApi.getMenuKeyMask()),
            () -> {
                try {
                    Framework.getInstance().getMainWindow().saveWork();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action SAVE_WORK_AS_ACTION = new Action("Save work as...",
            () -> {
                try {
                    Framework.getInstance().getMainWindow().saveWorkAs();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action CLOSE_ACTIVE_EDITOR_ACTION = new Action("Close active work",
            KeyStroke.getKeyStroke(KeyEvent.VK_F4, DesktopApi.getMenuKeyMask()),
            () -> {
                try {
                    Framework.getInstance().getMainWindow().closeActiveEditor();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action CLOSE_ALL_EDITORS_ACTION = new Action("Close all works",
            () -> {
                try {
                    Framework.getInstance().getMainWindow().closeEditorWindows();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action SHUTDOWN_GUI_ACTION = new Action("Switch to console mode",
            () -> {
                try {
                    Framework.getInstance().shutdownGUI();
                } catch (OperationCancelledException e) { }
            });

    public static final Action EXIT_ACTION = new Action("Exit",
            KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK),
            () -> Framework.getInstance().shutdown());

    public static final Action IMPORT_ACTION = new Action("Import...",
            () -> Framework.getInstance().getMainWindow().importFrom());

    public static final Action EDIT_UNDO_ACTION = new Action("Undo",
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, DesktopApi.getMenuKeyMask()),
            () -> Framework.getInstance().getMainWindow().undo());

    public static final Action EDIT_REDO_ACTION = new Action("Redo",
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, DesktopApi.getMenuKeyMask() | ActionEvent.SHIFT_MASK),
            () -> Framework.getInstance().getMainWindow().redo());

    public static final Action EDIT_CUT_ACTION = new Action("Cut",
            KeyStroke.getKeyStroke(KeyEvent.VK_X, DesktopApi.getMenuKeyMask()),
            () -> Framework.getInstance().getMainWindow().cut());

    public static final Action EDIT_COPY_ACTION = new Action("Copy",
            KeyStroke.getKeyStroke(KeyEvent.VK_C, DesktopApi.getMenuKeyMask()),
            () -> Framework.getInstance().getMainWindow().copy());

    public static final Action EDIT_PASTE_ACTION = new Action("Paste",
            KeyStroke.getKeyStroke(KeyEvent.VK_V, DesktopApi.getMenuKeyMask()),
            () -> {
                GraphEditorPanel editor = Framework.getInstance().getMainWindow().getCurrentEditor();
                if (!editor.hasFocus()) {
                    editor.getWorkspaceEntry().setPastePosition(null);
                }
                Framework.getInstance().getMainWindow().paste();
            });

    public static final Action EDIT_DELETE_ACTION = new Action("Delete",
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
            () -> Framework.getInstance().getMainWindow().delete());

    public static final Action EDIT_SELECT_ALL_ACTION = new Action("Select all",
            KeyStroke.getKeyStroke(KeyEvent.VK_A, DesktopApi.getMenuKeyMask()),
            () -> Framework.getInstance().getMainWindow().selectAll());

    public static final Action EDIT_SELECT_INVERSE_ACTION = new Action("Inverse selection",
            KeyStroke.getKeyStroke(KeyEvent.VK_I, DesktopApi.getMenuKeyMask()),
            () -> Framework.getInstance().getMainWindow().selectInverse());

    public static final Action EDIT_SELECT_NONE_ACTION = new Action("Deselect",
            () -> Framework.getInstance().getMainWindow().selectNone());

    public static final Action EDIT_SETTINGS_ACTION = new Action("Preferences...",
            () -> Framework.getInstance().getMainWindow().editSettings());

    public static final Action VIEW_ZOOM_IN = new Action("Zoom in",
            KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.zoomIn();
                }
            });

    public static final Action VIEW_ZOOM_OUT = new Action("Zoom out",
            KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.zoomOut();
                }
            });

    public static final Action VIEW_ZOOM_DEFAULT = new Action("Default zoom",
            KeyStroke.getKeyStroke(KeyEvent.VK_0, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.zoomDefault();
                }
            });

    public static final Action VIEW_PAN_CENTER = new Action("Center selection",
            KeyStroke.getKeyStroke(KeyEvent.VK_T, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.panCenter();
                }
            });

    public static final Action VIEW_ZOOM_FIT = new Action("Fit selection to screen",
            KeyStroke.getKeyStroke(KeyEvent.VK_F, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.zoomFit();
                }
            });

    public static final Action VIEW_PAN_LEFT = new Action("Pan left",
            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.panLeft();
                }
            });

    public static final Action VIEW_PAN_UP = new Action("Pan up",
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.panUp();
                }
            });

    public static final Action VIEW_PAN_RIGHT = new Action("Pan right",
            KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.panRight();
                }
            });

    public static final Action VIEW_PAN_DOWN = new Action("Pan down",
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, DesktopApi.getMenuKeyMask()),
            () -> {
                final GraphEditor editor = getCurrentEditor();
                if (editor != null) {
                    editor.panDown();
                }
            });

    public static final Action TOGGLE_GRID = new Action("Toggle grid visibility",
            () -> {
                CommonEditorSettings.setGridVisibility(!CommonEditorSettings.getGridVisibility());
                repaintAndFocusCurrentEditor();
            });

    public static final Action TOGGLE_RULER = new Action("Toggle ruler visibility",
            () -> {
                CommonEditorSettings.setRulerVisibility(!CommonEditorSettings.getRulerVisibility());
                repaintAndFocusCurrentEditor();
            });

    public static final Action TOGGLE_NAME = new Action("Toggle name visibility",
            () -> {
                CommonVisualSettings.setNameVisibility(!CommonVisualSettings.getNameVisibility());
                repaintAndFocusCurrentEditor();
            });

    public static final Action TOGGLE_LABEL = new Action("Toggle label visibility",
            () -> {
                CommonVisualSettings.setLabelVisibility(!CommonVisualSettings.getLabelVisibility());
                repaintAndFocusCurrentEditor();
            });

    public static final Action RESET_GUI_ACTION = new Action("Reset UI layout",
            () -> Framework.getInstance().getMainWindow().resetLayout());

    public static final Action HELP_OVERVIEW_ACTION = new Action("Overview",
            () -> FileUtils.openExternally("overview/start.html", "Overview access error"));

    public static final Action HELP_CONTENTS_ACTION = new Action("Help contents",
            KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
            () -> FileUtils.openExternally("help/start.html", "Help access error"));

    public static final Action HELP_TUTORIALS_ACTION = new Action("Tutorials",
            () -> FileUtils.openExternally("tutorial/start.html", "Tutorials access error"));

    public static final Action HELP_BUGREPORT_ACTION = new Action("Report a bug at GitHub",
            () -> {
                try {
                    URI uri = new URI("https://github.com/tuura/workcraft/issues/new");
                    DesktopApi.browse(uri);
                } catch (URISyntaxException e) {
                    LogUtils.logError(e.getMessage());
                }
            });

    public static final Action HELP_EMAIL_ACTION = new Action("Contact developers by e-mail",
            () -> {
                try {
                    URI uri = new URI("mailto", Info.getEmail(), null);
                    DesktopApi.browse(uri);
                } catch (URISyntaxException e) {
                    LogUtils.logError(e.getMessage());
                }
            });

    public static final Action HELP_ABOUT_ACTION = new Action("About Workcraft",
            () -> new AboutDialog(Framework.getInstance().getMainWindow()).reveal());

}
