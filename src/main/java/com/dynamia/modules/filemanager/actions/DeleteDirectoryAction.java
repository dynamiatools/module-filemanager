package com.dynamia.modules.filemanager.actions;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.Callback;

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
					new Callback() {

						@Override
						public void doSomething() {
							fileManager.getCurrentDirectory().delete();
							fileManager.reload();							
							UIMessages.showMessage("Directory deleted successfull");

						}
					});
		}

	}

}
