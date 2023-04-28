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
import java.util.List;

import org.zkoss.image.AImage;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.ui.icons.InstallIcons;
import tools.dynamia.zk.util.ZKUtil;

@InstallIcons
public class ViewImageAction extends FileManagerAction {

	public ViewImageAction() {
		setName("View Image");
		setImage("photos");
		setPosition(3.2);
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
			if (isImage(target)) {

				try {
					Image image = new Image();
					image.setContent(new AImage(target.getFile()));

					Window window = new Window();
					window.setVflex("1");
					window.setHflex("1");
					window.setContentStyle("overflow: auto");

					window.appendChild(image);

					ZKUtil.showDialog(target.getName(), window, "70%", "70%");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			UIMessages.showMessage("Select image to view", MessageType.ERROR);
		}

	}

	public static boolean isImage(FileInfo target) {
		String ext = target.getExtension();
		return "jpg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext);
	}
}
