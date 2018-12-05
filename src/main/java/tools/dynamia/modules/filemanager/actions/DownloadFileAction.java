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


import java.io.FileNotFoundException;
import java.util.List;

import org.zkoss.zul.Filedownload;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
public class DownloadFileAction extends FileManagerAction {

	public DownloadFileAction() {
		setName("Dowload File");
		setImage("down");
		setPosition(3.1);
		setMenuSupported(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		FileInfo target = null;
		if (evt.getData() instanceof FileInfo) {
			target = (FileInfo) evt.getData();
		} else if (evt.getData() instanceof List) {
			List list = (List) evt.getData();
			if (!list.isEmpty()) {
				target = (FileInfo) list.get(0);
			}
		}

		if (target != null) {
			try {
				Filedownload.save(target.getFile(), null);
				UIMessages.showMessage("File " + target.getName() + " dowloaded successfully");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			UIMessages.showMessage("Select file to downloa", MessageType.ERROR);
		}
	}

}
