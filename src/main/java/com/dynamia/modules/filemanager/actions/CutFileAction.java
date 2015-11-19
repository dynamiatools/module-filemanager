package com.dynamia.modules.filemanager.actions;

import java.util.List;

import com.dynamia.modules.filemanager.FileManager;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;

@InstallAction
public class CutFileAction extends CopyFileAction {

	public CutFileAction() {
		setName("Cut File");
		setImage("cut");
		setPosition(5.2);
	}

	@Override
	protected void afterCopy(ActionEvent evt, FileManager mgr, List<FileInfo> selectedFiles) {
		mgr.setAttribute(MOVE_MODE, Boolean.TRUE);
	}

}
