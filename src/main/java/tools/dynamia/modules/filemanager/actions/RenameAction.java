package tools.dynamia.modules.filemanager.actions;

import java.io.File;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.VirtualFile;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class RenameAction extends FileManagerAction {

	public RenameAction() {
		setName("Rename");
		setImage("fa-text-width");
		setPosition(4.9);
		setMenuSupported(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		FileInfo selected = mgr.getSelectedFile();

		if (selected != null) {
			if (selected.getFile() instanceof VirtualFile) {
				UIMessages.showMessage("Cannot rename selected file beacuse is virtual", MessageType.WARNING);
			} else {

				ZKUtil.showInputDialog("Enter new name", String.class, selected.getName(), e -> {
					String newname = (String) e.getData();
					if (newname != null && newname.isEmpty()) {
						UIMessages.showMessage("Enter valid name", MessageType.ERROR);
					} else {
						File dest = new File(selected.getFile().getParentFile(), newname);
						if (dest.exists()) {
							UIMessages.showMessage("Cannot rename, already file with same name", MessageType.WARNING);
						} else {
							selected.getFile().renameTo(dest);
							UIMessages.showMessage("File renamed succesfully");
							mgr.reloadSelected();
						}
					}
				});

			}
		} else {
			UIMessages.showMessage("Seleccted file or directory to rename", MessageType.WARNING);
		}
	}

}
