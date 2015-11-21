/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.ui;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.VirtualFileProvider;
import tools.dynamia.zk.crud.ui.ChildrenLoader;
import tools.dynamia.zk.crud.ui.EntityTreeModel;
import tools.dynamia.zk.crud.ui.EntityTreeNode;
import tools.dynamia.zk.crud.ui.LazyEntityTreeNode;

/**
 *
 * @author mario
 */
public class DirectoryTree extends Tree implements ChildrenLoader<FileInfo>, EventListener<Event> {

	private File selected;
	private EntityTreeModel<FileInfo> treeModel;

	private EntityTreeNode<FileInfo> rootNode;
	private boolean showHiddenFolders;
	private final File rootDirectory;

	public DirectoryTree(File rootDirectory) {
		this.rootDirectory = rootDirectory;

		init();
	}

	public DirectoryTree(Path rootPath) {
		this(rootPath.toFile());
	}

	public void reload() {
		initModel();
	}

	public void reloadSelectedDirectory() {
		EntityTreeNode<FileInfo> selectedNode = rootNode;
		if (getSelectedItem() != null) {
			selectedNode = getSelectedItem().getValue();
		}

		if (selectedNode == rootNode) {
			initModel();
		} else if (selectedNode instanceof LazyEntityTreeNode) {
			loadChildren((LazyEntityTreeNode<FileInfo>) selectedNode);
		}

	}

	private void init() {
		setHflex("1");
		setVflex("1");
		addEventListener(Events.ON_CLICK, this);
		setItemRenderer(new DirectoryTreeItemRenderer());

		setVflex("1");
		setHflex("1");

		initModel();
	}

	private void initModel() {
		FileInfo file = new FileInfo(rootDirectory);

		rootNode = new EntityTreeNode<FileInfo>(file);
		treeModel = new EntityTreeModel<FileInfo>(rootNode);
		for (EntityTreeNode<FileInfo> entityTreeNode : getSubdirectories(file)) {
			rootNode.addChild(entityTreeNode);
		}

		for (VirtualFileProvider virtualFileProvider : Containers.get().findObjects(VirtualFileProvider.class)) {

			virtualFileProvider.getVirtualFiles()
					.forEach(vf -> rootNode.addChild(new DirectoryTreeNode(new FileInfo(vf), this)));
		}

		setModel(treeModel);

	}

	private Collection<EntityTreeNode<FileInfo>> getSubdirectories(FileInfo file) {
		File[] subs = file.getFile().listFiles((FileFilter) pathname -> {
			if (pathname.isDirectory()) {
				if (!isShowHiddenFolders()) {
					return !pathname.isHidden() && !pathname.getName().startsWith(".");
				}
				return true;
			}
			return false;
		});

		List<EntityTreeNode<FileInfo>> subdirectories = new ArrayList<EntityTreeNode<FileInfo>>();
		if (subs != null) {
			for (File sub : subs) {
				subdirectories.add(new DirectoryTreeNode(new FileInfo(sub), this));
			}
		}

		Collections.sort(subdirectories, (o1, o2) -> o1.getData().getName().compareTo(o2.getData().getName()));

		return subdirectories;
	}

	@Override
	public void loadChildren(LazyEntityTreeNode<FileInfo> node) {
		node.getChildren().clear();
		// treeModel.fireEvent(TreeDataEvent.INTERVAL_REMOVED,
		// treeModel.getPath(node), node.getChildCount(), 0);
		setModel(treeModel);
		for (EntityTreeNode<FileInfo> treeNode : getSubdirectories(node.getData())) {
			node.addChild(treeNode);
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		Treeitem item = getSelectedItem();
		if (item != null) {
			DirectoryTreeNode node = item.getValue();
			setSelected(node.getData().getFile());
		}
	}

	public File getSelected() {
		return selected;
	}

	public void setSelected(File value) {
		this.selected = value;
		Events.postEvent(new Event(Events.ON_SELECT, this, value));
	}

	public boolean isShowHiddenFolders() {
		return showHiddenFolders;
	}

	public void setShowHiddenFolders(boolean showHiddenFolders) {
		this.showHiddenFolders = showHiddenFolders;
	}

}
