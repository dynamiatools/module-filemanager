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

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.zk.viewers.table.TableView;

@InstallAction
public class MultilSelectAction extends FileManagerAction {

    public MultilSelectAction() {
        setName("Multi Select Files");
        setImage("multicheck");

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        FileManager fileManager = (FileManager) evt.getSource();
        TableView<FileInfo> table = fileManager.getTableFiles();
        table.setMultiple(!table.isMultiple());
        table.setCheckmark(!table.isCheckmark());

        if (table.isCheckmark()) {
            table.getHeader("icon").setWidth("60px");
        } else {
            fileManager.reset();
        }
    }

}
