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

package tools.dynamia.modules.filemanager.actions;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.modules.filemanager.Folder;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ui.Viewer;

import java.io.File;

import static tools.dynamia.viewers.ViewDescriptorBuilder.field;
import static tools.dynamia.viewers.ViewDescriptorBuilder.viewDescriptor;

@InstallAction
public class NewDirectoryAction extends FileManagerAction {

    public NewDirectoryAction() {
        setName("New Directory");
        setImage("add-folder");
        setPosition(1);
        setBackground("#008d4c");
        setColor(".color-white");

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final FileManager fileManager = (FileManager) evt.getSource();

        Folder form = new Folder();
        if (fileManager.getCurrentDirectory() != null) {
            form.setParent(fileManager.getCurrentDirectory().getName());
        }

        ViewDescriptor descriptor = viewDescriptor("form", Folder.class, true)
                .sortFields("parent", "name")
                .fields(
                        field("parent", "Parent", "label"))
                .layout("columns", "1")
                .build();

        final Viewer viewer = new Viewer(descriptor, form);
        viewer.setWidth("600px");

        final Window window = ZKUtil.createWindow("New Directory");

        window.appendChild(viewer);

        Button btn = new Button("Create Directory");
        btn.setStyle("float:right");
        window.appendChild(btn);
        btn.addEventListener(Events.ON_CLICK, event -> {
            Folder form1 = (Folder) viewer.getValue();
            File parent = fileManager.getCurrentDirectory().getFile();
            if (fileManager.getCurrentDirectory() != null) {
                if (fileManager.getCurrentDirectory().isReadOnly()) {
                    UIMessages.showMessage("Cannot create subdirectory because selected directory is read only", MessageType.WARNING);
                    return;
                }
            }
            if (form1.getName() != null && !form1.getName().isEmpty()) {

                File newdir = new File(parent, form1.getName());
                if (!newdir.exists()) {
                    newdir.mkdirs();
                    window.detach();
                    UIMessages.showMessage("Directory " + form1.getName() + " created successfully");

                    fileManager.reloadSelected();

                } else {
                    UIMessages.showMessage("Already exists  directory with name " + form1.getName(), MessageType.ERROR);
                }
            } else {
                UIMessages.showMessage("Enter directory name", MessageType.ERROR);
            }
        });

        window.doModal();

    }

}
