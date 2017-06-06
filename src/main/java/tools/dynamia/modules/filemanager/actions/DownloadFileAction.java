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
 * @author mario_2
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
