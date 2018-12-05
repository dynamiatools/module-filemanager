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

import java.util.List;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.VirtualFile;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class CopyFileAction extends FileManagerAction {

	public static final String COPY_FILES = "COPY_FILES";
	public static final String MOVE_MODE = "MOVE_MODE";

	public CopyFileAction() {
		setName("Copy");
		setImage("copy");
		setPosition(5.1);
		setMenuSupported(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		List<FileInfo> selectedFiles = mgr.getSelectedFiles();
		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			for (FileInfo fileInfo : selectedFiles) {
				if (fileInfo.getFile() instanceof VirtualFile) {
					UIMessages.showMessage("Cannot copy file " + fileInfo.getName() + " because is a virtual file", MessageType.ERROR);
					return;
				}
			}
			mgr.setAttribute(COPY_FILES, selectedFiles);
			UIMessages.showMessage("Select destination directory and click Paste");
			afterCopy(evt, mgr, selectedFiles);
		} else {
			UIMessages.showMessage("No file selected", MessageType.WARNING);
		}
	}

	protected void afterCopy(ActionEvent evt, FileManager mgr, List<FileInfo> selectedFiles) {
		// TODO Auto-generated method stub

	}

}
