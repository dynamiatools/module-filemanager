package com.dynamia.modules.filemanager.actions;

import java.io.File;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.modules.filemanager.Folder;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.viewers.ViewDescriptor;
import static tools.dynamia.viewers.ViewDescriptorBuilder.*;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ui.Viewer;

@InstallAction
public class NewDirectoryAction extends FileManagerAction {

	public NewDirectoryAction() {
		setName("New Directory");
		setImage("add-folder");
		setPosition(1);

	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		final FileManager fileManager = (FileManager) evt.getSource();

		Folder form = new Folder();
		if (fileManager.getCurrentDirectory() != null) {
			form.setParent(fileManager.getCurrentDirectory().getName());
		}

		ViewDescriptor descriptor = viewDescriptor("form", Folder.class, true)
				.sortFields("name", "parent", "root")
				.fields(
						field("parent", "Parent", "label"))
				.layout("columns", "1")
				.build();

		final Viewer viewer = new Viewer(descriptor, form);
		viewer.setWidth("600px");

		final Window window = ZKUtil.createWindow("New Directory");

		window.appendChild(viewer);

		Button btn = new Button("Create Directory");
		btn.setStyle("float:right");
		window.appendChild(btn);
		btn.addEventListener(Events.ON_CLICK, event -> {
			Folder form1 = (Folder) viewer.getValue();
			File parent = fileManager.getRootDirectory();
			if (!form1.isRoot() && fileManager.getCurrentDirectory() != null) {
				if (fileManager.getCurrentDirectory().isReadOnly()) {
					UIMessages.showMessage("Cannot create subdirectory because selected directory is read only", MessageType.WARNING);
					return;
				}
				parent = fileManager.getCurrentDirectory().getFile();
			}
			if (form1.getName() != null && !form1.getName().isEmpty()) {

				File newdir = new File(parent, form1.getName());
				if (!newdir.exists()) {
					newdir.mkdirs();
					window.detach();
					UIMessages.showMessage("Directory " + form1.getName() + " created successfully");
					fileManager.reloadSelected();
				} else {
					UIMessages.showMessage("Already exists  directory with name " + form1.getName(), MessageType.ERROR);
				}
			} else {
				UIMessages.showMessage("Enter directory name", MessageType.ERROR);
			}
		});

		window.doModal();

	}

}
