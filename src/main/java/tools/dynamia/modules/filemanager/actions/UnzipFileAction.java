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

import java.io.IOException;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class UnzipFileAction extends FileManagerAction {

	public UnzipFileAction() {
		setName("Unzip File");
		setImage("zip");
		setPosition(6);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		FileInfo fileInfo = mgr.getValue();
		if (fileInfo != null && fileInfo.getExtension().equalsIgnoreCase("zip")) {
			UIMessages.showQuestion("Are you sure you want unzip this file " + fileInfo.getName() + "?", () -> {
				try {
					IOUtils.unzipFile(fileInfo.getFile(), fileInfo.getFile().getParentFile());
					UIMessages.showMessage("Done");
					mgr.reloadSelected();
				} catch (IOException e) {
					UIMessages.showMessage("Error unziping file: " + e.getMessage(), MessageType.ERROR);
					e.printStackTrace();
				}
			});
		} else {
			UIMessages.showMessage("Select a Zip file", MessageType.WARNING);
		}
	}

}
