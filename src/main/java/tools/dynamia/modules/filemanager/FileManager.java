/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.East;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.North;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.West;

import tools.dynamia.actions.Action;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.filemanager.ui.DirectoryTree;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.viewers.View;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.actions.MenuitemActionRenderer;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.form.FormView;
import tools.dynamia.zk.viewers.table.TableView;
import tools.dynamia.zk.viewers.ui.Viewer;

/**
 *
 * @author mario_2
 */
public class FileManager extends Div implements ActionEventBuilder, View<FileInfo> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5270252199212721490L;
	private Borderlayout layout;
	private ActionToolbar toolbar;
	private File rootDirectory;
	private DirectoryTree directoryTree;
	private TableView<FileInfo> tableFiles;
	private FileInfo currentDirectory;
	private View parentView;
	private ViewDescriptor viewDescriptor;
	private FileInfo value;

	public FileManager() {

	}

	public FileManager(File directory) {
		this.rootDirectory = directory;
		this.currentDirectory = new FileInfo(directory);
	}

	public FileManager(Path path) {
		this(path.toFile());
	}

	@Override
	public FileInfo getValue() {

		try {
			value = getSelectedFiles().get(0);
		} catch (Exception e) {
			value = null;
		}
		return value;
	}

	@Override
	public void setValue(FileInfo value) {
		if (this.value != value) {
			this.value = value;
			Events.postEvent(new Event(FormView.ON_VALUE_CHANGED, this, this.value));

			if (value != null) {
				currentDirectory = new FileInfo(value.getFile().getParentFile());

				if (rootDirectory == null) {
					rootDirectory = currentDirectory.getFile();
					initLayout();
				}
			}
		}

	}

	@Override
	public void setViewDescriptor(ViewDescriptor viewDescriptor) {
		this.viewDescriptor = viewDescriptor;

	}

	@Override
	public ViewDescriptor getViewDescriptor() {
		return viewDescriptor;
	}

	@Override
	public View getParentView() {
		return parentView;
	}

	@Override
	public void setParentView(View view) {
		this.parentView = view;
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
		layout.appendChild(new East());
		layout.setVflex("1");
		layout.setHflex("1");

		appendChild(layout);

		toolbar = new ActionToolbar(this);
		layout.getNorth().appendChild(toolbar);

		directoryTree = new DirectoryTree(rootDirectory);
		directoryTree.addEventListener(Events.ON_SELECT,

				t -> {

					File selectedDir = directoryTree.getSelected();
					if (selectedDir != null) {
						this.currentDirectory = new FileInfo(selectedDir);
						loadFiles(selectedDir);
					}
				});
		layout.getWest().appendChild(directoryTree);
		layout.getWest().setWidth("20%");
		layout.getWest().setSplittable(true);
		layout.getWest().setCollapsible(true);
		layout.getWest().setTitle("Directories");

		tableFiles = (TableView<FileInfo>) Viewers.getView(FileInfo.class, "table", null);
		tableFiles.setEmptyMessage("No files found!");
		tableFiles.setMold("default");
		tableFiles.setContextMenu(createContextMenu());
		
		layout.getCenter().appendChild(tableFiles);

		layout.getEast().setTitle("Preview");
		layout.getEast().setWidth("20%");
		layout.getEast().setCollapsible(true);
		layout.getEast().setOpen(true);
		layout.getEast().setAutoscroll(true);
		layout.getEast().setSplittable(true);
		loadActions();
		loadPreview();

	}

	private Menupopup createContextMenu() {
		Menupopup popup = new Menupopup();
		appendChild(popup);

		return popup;
	}

	private void loadPreview() {
		tableFiles.addEventListener(Events.ON_SELECT, e -> {
			layout.getEast().getChildren().clear();
			FileInfo selected = getSelectedFile();
			if (selected != null) {
				showPreview(selected);
			}

		});

	}

	private void showPreview(FileInfo selected) {
		Vlayout preview = new Vlayout();

		if (selected.isImage()) {
			try {
				Image image = new Image();
				image.setContent(new AImage(selected.getFile()));
				image.setWidth("100%");
				preview.appendChild(image);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Containers.get().findObjects(FileManagerPreviewExtension.class).forEach(ext -> {
			Object view = ext.getView(selected);
			if (view != null && view instanceof Component) {
				preview.appendChild((Component) view);
			}
		});

		Viewer viewer = new Viewer("form", FileInfo.class, selected);
		FormView form = (FormView) viewer.getView();
		form.setReadOnly(true);
		preview.appendChild(form);

		layout.getEast().appendChild(preview);

	}

	@Override
	public void setParent(Component parent) {
		initLayout();
		super.setParent(parent); // To change body of generated methods, choose
		// Tools | Templates.
	}

	@Override
	public ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
		return new ActionEvent(tableFiles.isMultiple() ? getSelectedFiles() : tableFiles.getSelected(), this);
	}

	public String getSelectedFilePath() {
		FileInfo fileInfo = tableFiles.getSelected();
		if (fileInfo != null) {
			String path = fileInfo.getFile().getPath();
			path = path.substring(path.indexOf(rootDirectory.getName()) + rootDirectory.getName().length() + 1);

			return path;
		} else {
			return "";
		}

	}

	public FileInfo getSelectedFile() {
		FileInfo fileInfo = tableFiles.getSelected();
		return fileInfo;
	}

	public List<FileInfo> getSelectedFiles() {
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

			List<FileInfo> fileInfos = new ArrayList<>();
			File files[] = selectedDirectory
					.listFiles((FileFilter) pathname -> pathname.isFile() || pathname.isDirectory());
			if (files != null) {
				for (File file : files) {
					fileInfos.add(new FileInfo(file));
				}
			}
			Collections.sort(fileInfos, (f1, f2) -> {
				int order = f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
				if (f1.isDirectory() && !f2.isDirectory()) {
					order = -1;
				} else if (!f1.isDirectory() && f2.isDirectory()) {
					order = 1;
				}

				return order;
			});
			tableFiles.setValue(fileInfos);
		}
	}

	private void loadActions() {
		List<FileManagerAction> actions = new ArrayList<>();

		for (FileManagerAction action : Containers.get().findObjects(FileManagerAction.class)) {
			actions.add(action);

		}

		Collections.sort(actions, (o1, o2) -> {
			Double pos1 = o1.getPosition();
			Double pos2 = o2.getPosition();
			return pos1.compareTo(pos2);
		});

		Menupopup contextMenu = tableFiles.getContextMenu();
		contextMenu.getChildren().clear();
		MenuitemActionRenderer menuRenderer = new MenuitemActionRenderer();
		for (FileManagerAction action : actions) {
			toolbar.addAction(action);

			if (action.isMenuSupported()) {
				Menuitem item = menuRenderer.render(action, this);
				contextMenu.appendChild(item);
			}

		}
	}

	public void reload() {
		directoryTree.reload();
		updateUI();
	}

	public void setRootDirectory(File rootDirectory) {
		this.rootDirectory = rootDirectory;
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

	public FileInfo getCurrentDirectory() {
		return currentDirectory;
	}

	public void updateUI() {
		if (currentDirectory != null) {
			loadFiles(getCurrentDirectory().getFile());
		} else {
			loadFiles(rootDirectory);

		}
	}

	public TableView<FileInfo> getTableFiles() {
		return tableFiles;
	}

	public DirectoryTree getDirectoryTree() {
		return directoryTree;
	}

}
