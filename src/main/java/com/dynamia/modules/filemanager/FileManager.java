/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import com.dynamia.modules.filemanager.ui.DirectoryTree;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.viewers.util.Viewers;
import com.dynamia.tools.viewers.zk.table.TableView;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.ActionEventBuilder;
import com.dynamia.tools.web.ui.ActionToolbar;

/**
 *
 * @author mario_2
 */
public class FileManager extends Window implements ActionEventBuilder {

	private Borderlayout layout;
	private ActionToolbar toolbar;
	private File rootDirectory;
	private DirectoryTree directoryTree;
	private TableView<FileInfo> tableFiles;
	private File currentDirectory;

	public FileManager(File directory) {
		this.rootDirectory = directory;
		this.currentDirectory = directory;
	}

	public FileManager(Path path) {
		this(path.toFile());
	}

	private void initLayout() {
		setVflex("1");
		setHflex("1");
		layout = new Borderlayout();
		layout.appendChild(new North());
		layout.appendChild(new Center());
		layout.appendChild(new West());
		layout.setVflex("1");
		layout.setHflex("1");

		appendChild(layout);

		toolbar = new ActionToolbar(this);
		layout.getNorth().appendChild(toolbar);

		directoryTree = new DirectoryTree(rootDirectory);
		directoryTree.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event t) throws Exception {
				loadFiles(directoryTree.getSelected());
			}

		});
		layout.getWest().appendChild(directoryTree);
		layout.getWest().setWidth("25%");
		layout.getWest().setTitle("Directories");

		tableFiles = (TableView<FileInfo>) Viewers.getView(FileInfo.class, "table", null);
		tableFiles.setEmptyMessage("No files found!");
		layout.getCenter().appendChild(tableFiles);
		loadActions();

	}

	@Override
	public void setParent(Component parent) {
		initLayout();
		super.setParent(parent); // To change body of generated methods, choose
									// Tools | Templates.
	}

	@Override
	public ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
		return new ActionEvent(tableFiles.getSelected(), this);
	}

	private void loadFiles(File selectedDirectory) {
		if (selectedDirectory != null) {
			this.currentDirectory = selectedDirectory;
			File files[] = selectedDirectory.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});

			List<FileInfo> fileInfos = new ArrayList<>();
			for (File file : files) {
				fileInfos.add(new FileInfo(file));
			}
			tableFiles.setValue(fileInfos);
		}
	}

	private void loadActions() {
		for (FileManagerAction action : Containers.get().findObjects(FileManagerAction.class)) {
			toolbar.addAction(action);
		}
	}

	public void reload() {
		directoryTree.reload();
		updateUI();
	}

	public void reloadSelected() {
		directoryTree.reloadSelectedDirectory();
		updateUI();
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}

	public void updateUI() {
		loadFiles(getCurrentDirectory());
	}

}
