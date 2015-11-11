/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager.actions;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.tools.integration.sterotypes.Component;
import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;

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
