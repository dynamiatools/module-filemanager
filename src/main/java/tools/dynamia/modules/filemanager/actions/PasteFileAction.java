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
import java.util.List;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class PasteFileAction extends FileManagerAction {

	public PasteFileAction() {
		setName("Paste");
		setImage("paste");
		setPosition(5.3);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		List<FileInfo> selectedFiles = (List<FileInfo>) mgr.getAttribute(CopyFileAction.COPY_FILES);
		boolean moveFiles = mgr.getAttribute(CopyFileAction.MOVE_MODE) == Boolean.TRUE;
		String mode = moveFiles ? "move" : "copy";

		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			FileInfo folder = mgr.getCurrentDirectory();
			if (folder == null) {
				UIMessages.showMessage("Select destination folder", MessageType.WARNING);
				return;
			}
			folder.getFile().setWritable(true, false);
			UIMessages.showQuestion("Are you sure to " + mode + " " + selectedFiles.size() + " file(s) to this location?", () -> {
				for (FileInfo fileInfo : selectedFiles) {
					try {

						IOUtils.copy(fileInfo.getFile(), new File(folder.getFile(), fileInfo.getName()));
						if (moveFiles) {
							fileInfo.getFile().delete();
						}
					} catch (Exception e) {
						UIMessages.showMessage("Error copying or moving files: " + e.getMessage(), MessageType.ERROR);
						e.printStackTrace();
					} finally {
						mgr.setAttribute(CopyFileAction.COPY_FILES, null);
						mgr.setAttribute(CopyFileAction.MOVE_MODE, null);
					}
					UIMessages.showMessage("File " + fileInfo.getName() + " paste succesfully");
				}
				mgr.reloadSelected();
			});

		} else {
			UIMessages.showMessage("No file to paste", MessageType.WARNING);
		}
	}

}
