/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.ui;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.Window;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.actions.ActionLoader;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.actions.editor.TextEditorAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.addons.Aceditor;
import tools.dynamia.zk.util.ZKUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mario Serrano Leones
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
    private Aceditor contentbox;
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

        contentbox = new Aceditor();

        contentbox.setMode(findMode());
        contentbox.setHeight("100%");
        contentbox.setWidth("100%");

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
        String ext = StringUtils.getFilenameExtension(file.getName());

        if (file.getName().equalsIgnoreCase("dockerfile")) {
            mode = "dockerfile";
        } else if (file.getName().equalsIgnoreCase(".gitignore")) {
            mode = "gitignore";
        } else {

            switch (ext) {
                case "js":
                    mode = "javascript";
                    break;
                case "css":
                    mode = "css";
                    break;
                case "html":
                    mode = "html";
                    break;
                case "java":
                    mode = "java";
                    break;
                case "groovy":
                    mode = "groovy";
                    break;
                case "svg":
                    mode = "xml";
                    break;
                case "xml":
                    mode = "xml";
                    break;
                case "ts":
                    mode = "typescript";
                    break;
                case "vm":
                    mode = "velocity";
                    break;
                case "yaml":
                case "yml":
                    mode = "yaml";
                    break;
                case "json":
                    mode = "json";
                    break;
                case "kt":
                    mode = "kotlin";
                    break;
                case "ini":
                    mode = "ini";
                    break;
                case "rb":
                    mode = "ruby";
                    break;
                case "go":
                    mode = "golang";
                    break;
                default:
                    mode = "text";
                    break;


            }
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

    public Aceditor getContentbox() {
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
