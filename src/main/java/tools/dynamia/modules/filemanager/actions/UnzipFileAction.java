package tools.dynamia.modules.filemanager.actions;

import java.io.IOException;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.IOUtils;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class UnzipFileAction extends FileManagerAction {

	public UnzipFileAction() {
		setName("Unzip File");
		setImage("zip");
		setPosition(6);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		FileInfo fileInfo = mgr.getValue();
		if (fileInfo != null && fileInfo.getExtension().equalsIgnoreCase("zip")) {
			UIMessages.showQuestion("Are you sure you want unzip this file " + fileInfo.getName() + "?", () -> {
				try {
					IOUtils.unzipFile(fileInfo.getFile(), fileInfo.getFile().getParentFile());
					UIMessages.showMessage("Done");
					mgr.reloadSelected();
				} catch (IOException e) {
					UIMessages.showMessage("Error unziping file: " + e.getMessage(), MessageType.ERROR);
					e.printStackTrace();
				}
			});
		} else {
			UIMessages.showMessage("Select a Zip file", MessageType.WARNING);
		}
	}

}
