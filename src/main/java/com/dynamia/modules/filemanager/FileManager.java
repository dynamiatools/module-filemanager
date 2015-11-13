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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.North;
import org.zkoss.zul.West;

import com.dynamia.modules.filemanager.ui.DirectoryTree;

import tools.dynamia.actions.Action;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.viewers.table.TableView;

/**
 *
 * @author mario_2
 */
public class FileManager extends Div implements ActionEventBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5270252199212721490L;
	private Borderlayout layout;
	private ActionToolbar toolbar;
	private File rootDirectory;
	private DirectoryTree directoryTree;
	private TableView<FileInfo> tableFiles;
	private File currentDirectory;

	public FileManager() {

	}

	public FileManager(File directory) {
		this.rootDirectory = directory;
		this.currentDirectory = directory;
	}

	public FileManager(Path path) {
		this(path.toFile());
	}

	private void initLayout() {

		getChildren().clear();
		setVflex("1");
		setHflex("1");

		if (rootDirectory == null || !rootDirectory.exists()) {
			Label error = new Label("Root Directory is null or dont exists");
			error.setStyle("color:red");
			error.setParent(this);
			return;
		}

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
		directoryTree.addEventListener(Events.ON_SELECT, t -> loadFiles(directoryTree.getSelected()));
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
		return new ActionEvent(tableFiles.isMultiple() ? getSelecteds() : tableFiles.getSelected(), this);
	}

	public String getSelectedFilePath() {
		FileInfo fileInfo = tableFiles.getSelected();
		if (fileInfo != null) {
			String path = fileInfo.getFile().getPath();
			path = path.substring(path.indexOf(rootDirectory.getName()) + rootDirectory.getName().length() + 1);
			path = path.replace(" ", "%20");
			return path;
		} else {
			return "";
		}

	}

	private List<FileInfo> getSelecteds() {
		List<FileInfo> selecteds = new ArrayList<>();

		if (tableFiles.getSelectedCount() > 0) {
			for (Listitem item : tableFiles.getSelectedItems()) {
				selecteds.add((FileInfo) item.getValue());
			}
		}

		return selecteds;
	}

	private void loadFiles(File selectedDirectory) {
		if (selectedDirectory != null) {
			this.currentDirectory = selectedDirectory;
			File files[] = selectedDirectory.listFiles((FileFilter) pathname -> pathname.isFile());

			List<FileInfo> fileInfos = new ArrayList<>();
			if (files != null) {
				for (File file : files) {
					fileInfos.add(new FileInfo(file));
				}
			}
			tableFiles.setValue(fileInfos);
		}
	}

	private void loadActions() {
		List<Action> actions = new ArrayList<>();
		for (FileManagerAction action : Containers.get().findObjects(FileManagerAction.class)) {
			actions.add(action);
		}
		Collections.sort(actions, (o1, o2) -> {
			Double pos1 = o1.getPosition();
			Double pos2 = o2.getPosition();
			return pos1.compareTo(pos2);
		});

		for (Action action : actions) {
			toolbar.addAction(action);

		}
	}

	public void reload() {
		directoryTree.reload();
		updateUI();
	}

	public void setRootDirectory(File rootDirectory) {
		this.rootDirectory = rootDirectory;
		this.currentDirectory = rootDirectory;
		initLayout();
	}

	public void setRootDirectory(String rootPath) {
		setRootDirectory(new File(rootPath));
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

	public TableView<FileInfo> getTableFiles() {
		return tableFiles;
	}

	public DirectoryTree getDirectoryTree() {
		return directoryTree;
	}

}
