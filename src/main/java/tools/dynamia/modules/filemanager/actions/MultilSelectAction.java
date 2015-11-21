package tools.dynamia.modules.filemanager.actions;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;

@InstallAction
public class MultilSelectAction extends FileManagerAction {

	public MultilSelectAction() {
		setName("Multi Select Files");
		setImage("multicheck");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		FileManager fileManager = (FileManager) evt.getSource();
		fileManager.getTableFiles().setMultiple(!fileManager.getTableFiles().isMultiple());
	}

}
