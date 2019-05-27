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
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.UIMessages;

import java.util.List;

/**
 * @author Mario Serrano Leones
 */
@InstallAction
public class DeleteFileAction extends FileManagerAction {

    public DeleteFileAction() {
        setName("Delete");
        setImage("delete");
        setPosition(100);
        setMenuSupported(true);
        setBackground(".btn-danger");
        setColor(".color-white");
        setAttribute("separator", true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        final FileManager mgr = (FileManager) evt.getSource();

        if (evt.getData() instanceof FileInfo) {
            final FileInfo fileInfo = (FileInfo) evt.getData();
            UIMessages.showQuestion("Are you sure to delete " + fileInfo.getName() + "?", () -> {
                fileInfo.delete();
                mgr.updateUI();
                UIMessages.showMessage(fileInfo.getName() + " deleted successfull");
            });
        } else if (evt.getData() instanceof List) {
            final List<FileInfo> files = (List<FileInfo>) evt.getData();
            if (!files.isEmpty()) {
                UIMessages.showQuestion("Are you sure to delete " + files.size() + " files ?", () -> {
                    for (FileInfo file : files) {
                        file.delete();
                    }
                    mgr.updateUI();
                    UIMessages.showMessage(files.size() + " files deleted successfull");
                });
            }
        }
    }

}
