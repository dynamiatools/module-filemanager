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
package tools.dynamia.modules.filemanager.actions;

/*-
 * #%L
 * Dynamia Modules - FileManager
 * %%
 * Copyright (C) 2017 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.modules.filemanager.ui.TextEditor;
import tools.dynamia.navigation.Page;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

import java.io.File;

/**
 * @author Mario Serrano Leones
 */
@InstallAction
public class EditTextFileAction extends FileManagerAction {

    public EditTextFileAction() {
        setName("Edit Text File");
        setImage("edit-page");
        setPosition(5.01);
        setMenuSupported(true);
        setBackground(".btn-primary");
        setColor(".color-white");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        FileManager manager = (FileManager) evt.getSource();

        if (evt.getData() instanceof FileInfo) {
            File editfile = ((FileInfo) evt.getData()).getFile();
            if (!ImageUtil.isImage(editfile)) {
                TextEditor textEditor = new TextEditor(editfile);
                textEditor.setFileManager(manager);
                textEditor.setBorder(false);
                textEditor.setVflex("1");
                textEditor.setClosable(false);
                textEditor.setMaximizable(false);
                Page editorPage = new ComponentPage(editfile.getAbsolutePath(), editfile.getName(), textEditor);
                editorPage.setAlwaysAllowed(true);
                ZKNavigationManager.getInstance().setCurrentPage(editorPage);
            } else {
                UIMessages.showMessage("Cannot edit selected file", MessageType.ERROR);
            }
        } else {
            UIMessages.showMessage("Select one file to edit", MessageType.WARNING);
        }
    }

}
