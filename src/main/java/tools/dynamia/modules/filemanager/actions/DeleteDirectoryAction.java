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

/*
 * Copyright (C) 2023 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia / South America
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DeleteDirectoryAction extends FileManagerAction {

    public DeleteDirectoryAction() {
        setName("Delete Directory");
        setImage("delete-folder");
        setPosition(2);
        setBackground(".btn-danger");
        setColor(".color-white");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final FileManager fileManager = (FileManager) evt.getSource();

        FileInfo currentDir = fileManager.getCurrentDirectory();
        if (currentDir != null) {

            if (currentDir.isReadOnly()) {
                UIMessages.showMessage("Directory " + currentDir.getName() + " is read only, cannot be deleted.", MessageType.WARNING);
                return;
            }

            UIMessages.showQuestion("Are you sure you want delete \"" + fileManager.getCurrentDirectory().getName() + "\" folder?",
                    () -> {
                        IOUtils.deleteDirectory(currentDir.getFile());
                        fileManager.reload();
                        UIMessages.showMessage("Directory deleted successfull");

                    });
        } else {
            UIMessages.showMessage("No directory selected", MessageType.ERROR);
        }

    }

}
