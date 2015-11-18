package com.dynamia.modules.filemanager.actions;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.ui.MessageType;
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

		FileInfo currentDir = fileManager.getCurrentDirectory();
		if (currentDir != null) {

			if (currentDir.isReadOnly()) {
				UIMessages.showMessage("Directory " + currentDir.getName() + " is read only, cannot be deleted.", MessageType.WARNING);
				return;
			}

			UIMessages.showQuestion("Are you sure you want delete " + fileManager.getCurrentDirectory().getName() + " folder?",
					() -> {
						fileManager.getCurrentDirectory().getFile().delete();
						fileManager.reload();
						UIMessages.showMessage("Directory deleted successfull");

					});
		} else {
			UIMessages.showMessage("No directory selected", MessageType.ERROR);
		}

	}

}
