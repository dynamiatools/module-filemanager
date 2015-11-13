package com.dynamia.modules.filemanager.actions;



import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DeleteDirectoryAction extends FileManagerAction {

	public DeleteDirectoryAction() {
		setName("Delete Directory");
		setImage("delete-folder");
		setPosition(2);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager fileManager = (FileManager) evt.getSource();

		if (fileManager.getCurrentDirectory() != null) {
			UIMessages.showQuestion("Are you sure you want delete " + fileManager.getCurrentDirectory().getName() + " folder?",
					() -> {
						fileManager.getCurrentDirectory().delete();
						fileManager.reload();							
						UIMessages.showMessage("Directory deleted successfull");

					});
		}

	}

}
