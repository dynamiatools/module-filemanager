package com.dynamia.modules.filemanager.actions;

import tools.dynamia.actions.ActionEvent;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;

import tools.dynamia.actions.InstallAction;

@InstallAction
public class MultilSelectAction extends FileManagerAction {

	public MultilSelectAction() {
		setName("Multi Select Files");
		setImage("check");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		FileManager fileManager = (FileManager) evt.getSource();
		fileManager.getTableFiles().setMultiple(!fileManager.getTableFiles().isMultiple());
	}

}
