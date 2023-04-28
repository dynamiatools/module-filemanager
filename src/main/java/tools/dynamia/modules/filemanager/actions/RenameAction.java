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

import java.io.File;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.VirtualFile;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class RenameAction extends FileManagerAction {

	public RenameAction() {
		setName("Rename");
		setImage("fa-text-width");
		setPosition(4.9);
		setMenuSupported(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		FileInfo selected = mgr.getSelectedFile();

		if (selected != null) {
			if (selected.getFile() instanceof VirtualFile) {
				UIMessages.showMessage("Cannot rename selected file beacuse is virtual", MessageType.WARNING);
			} else {

				ZKUtil.showInputDialog("Enter new name", String.class, selected.getName(), e -> {
					String newname = (String) e.getData();
					if (newname != null && newname.isEmpty()) {
						UIMessages.showMessage("Enter valid name", MessageType.ERROR);
					} else {
						File dest = new File(selected.getFile().getParentFile(), newname);
						if (dest.exists()) {
							UIMessages.showMessage("Cannot rename, already file with same name", MessageType.WARNING);
						} else {
							selected.getFile().renameTo(dest);
							UIMessages.showMessage("File renamed succesfully");
							mgr.reloadSelected();
						}
					}
				});

			}
		} else {
			UIMessages.showMessage("Seleccted file or directory to rename", MessageType.WARNING);
		}
	}

}
