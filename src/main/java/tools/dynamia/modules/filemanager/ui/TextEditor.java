/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.ui;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.actions.ActionLoader;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.actions.editor.TextEditorAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.util.ZKUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mario
 */
public class TextEditor extends Window implements ActionEventBuilder {

    static {
        ComponentAliasIndex.getInstance().add(TextEditor.class);
        BindingComponentIndex.getInstance().put("value", TextEditor.class);

    }

    /**
     *
     */
    private static final long serialVersionUID = -907661981883080140L;
    private ActionToolbar toolbar;
    private Borderlayout layout;
    private File file;
    private Textbox contentbox;
    private FileManager fileManager;

    public TextEditor(File file) {
        this.file = file;
        init();
        initActions();
        readFile();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void setValue(String value) {
        contentbox.setValue(value);
    }

    public String getValue() {
        return contentbox.getValue();
    }

    private void init() {
        setClosable(true);
        setMaximizable(true);
        setSclass("text-editor");

        toolbar = new ActionToolbar(this);
        toolbar.setSclass("text-editor-toolbar");

        layout = new Borderlayout();
        layout.appendChild(new Center());
        layout.appendChild(new North());

        layout.getNorth().appendChild(toolbar);

        contentbox = new Textbox();
        contentbox.setMultiline(true);
        String mode = findMode();
        contentbox.setClientDataAttribute("ace-code-editor", "{theme:'ace/theme/eclipse', mode:'ace/mode/" + mode + "'}");
        contentbox.setWidth("100%");
        contentbox.setHeight("100%");

        layout.getCenter().appendChild(contentbox);
        layout.setWidth("100%");
        layout.setHeight("100%");

        appendChild(layout);

        addEventListener(Events.ON_CLOSE, e -> {
            e.stopPropagation();
            UIMessages.showQuestion("Are you sure to close this editor?", () -> {
                detach();
                if (fileManager != null) {
                    fileManager.reloadSelected();
                }
            });
        });
    }

    private String findMode() {
        String mode = "html";
        if (file.getName().endsWith(".js")) {
            mode = "javascript";
        } else if (file.getName().endsWith(".css")) {
            mode = "css";
        }
        return mode;
    }

    @Override
    public ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
        return new ActionEvent(contentbox.getValue(), this);
    }

    private void readFile() {
        try {
            String content = Files.readAllLines(file.toPath(), Charset.forName("UTF-8")).stream()
                    .collect(Collectors.joining("\n"));
            contentbox.setValue(content);
        } catch (IOException ex) {
            UIMessages.showMessage("Error reading file: " + file.getName() + ". " + ex.getMessage(), MessageType.ERROR);
            ex.getMessage();
        }
    }

    private void initActions() {
        ActionLoader<TextEditorAction> loader = new ActionLoader<>(TextEditorAction.class);
        loader.load().forEach(a -> toolbar.addAction(a));
    }

    public File getFile() {
        return file;
    }

    public Textbox getContentbox() {
        return contentbox;
    }

    public void show() {
        setTitle("TextEditor - " + file.getName());
        setWidth("90%");
        setHeight("90%");
        setPage(ZKUtil.getFirstPage());
        doModal();
    }
}
