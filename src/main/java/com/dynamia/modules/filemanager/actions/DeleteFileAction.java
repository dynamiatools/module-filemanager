/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager.actions;


import java.util.List;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.ui.UIMessages;

/**
 *
 * @author mario_2
 */
@InstallAction
public class DeleteFileAction extends FileManagerAction {

	public DeleteFileAction() {
		setName("Delete Selected File");
		setImage("delete");
		setPosition(4);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		final FileManager mgr = (FileManager) evt.getSource();

		if (evt.getData() instanceof FileInfo) {
			final FileInfo fileInfo = (FileInfo) evt.getData();
			UIMessages.showQuestion("Are you sure to delete " + fileInfo.getName() + "?", () -> {
				fileInfo.getFile().delete();
				mgr.updateUI();
				UIMessages.showMessage(fileInfo.getName() + " deleted successfull");
			});
		} else if (evt.getData() instanceof List) {
			final List<FileInfo> files = (List<FileInfo>) evt.getData();
			if (!files.isEmpty()) {
				UIMessages.showQuestion("Are you sure to delete " + files.size() + " files ?", () -> {
					for (FileInfo file : files) {
						file.getFile().delete();
					}
					mgr.updateUI();
					UIMessages.showMessage(files.size() + " files deleted successfull");
				});
			}
		}
	}

}
