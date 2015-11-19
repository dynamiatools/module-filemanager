package com.dynamia.modules.filemanager.actions;

import java.io.File;
import java.util.List;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.IOUtils;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class PasteFileAction extends FileManagerAction {

	public PasteFileAction() {
		setName("Paste");
		setImage("paste");
		setPosition(5.3);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager mgr = (FileManager) evt.getSource();
		List<FileInfo> selectedFiles = (List<FileInfo>) mgr.getAttribute(CopyFileAction.COPY_FILES);
		boolean moveFiles = mgr.getAttribute(CopyFileAction.MOVE_MODE) == Boolean.TRUE;
		String mode = moveFiles ? "move" : "copy";

		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			FileInfo folder = mgr.getCurrentDirectory();
			if (folder == null) {
				UIMessages.showMessage("Select destination folder", MessageType.WARNING);
				return;
			}
			folder.getFile().setWritable(true, false);
			UIMessages.showQuestion("Are you sure to " + mode + " " + selectedFiles.size() + " file(s) to this location?", () -> {
				for (FileInfo fileInfo : selectedFiles) {
					try {

						IOUtils.copy(fileInfo.getFile(), new File(folder.getFile(), fileInfo.getName()));
						if (moveFiles) {
							fileInfo.getFile().delete();
						}
					} catch (Exception e) {
						UIMessages.showMessage("Error copying or moving files: " + e.getMessage(), MessageType.ERROR);
						e.printStackTrace();
					} finally {
						mgr.setAttribute(CopyFileAction.COPY_FILES, null);
						mgr.setAttribute(CopyFileAction.MOVE_MODE, null);
					}
					UIMessages.showMessage("File " + fileInfo.getName() + " paste succesfully");
				}
				mgr.reloadSelected();
			});

		} else {
			UIMessages.showMessage("No file to paste", MessageType.WARNING);
		}
	}

}
