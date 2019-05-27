/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.ui;

/*-
 * #%L
 * Dynamia Modules - FileManager
 * %%
 * Copyright (C) 2017 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import tools.dynamia.integration.Containers;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.VirtualFileProvider;
import tools.dynamia.zk.crud.ui.*;
import tools.dynamia.zk.viewers.tree.TreeViewNode;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.*;

/**
 * Tree component for directory listing
 * @author Mario Serrano Leones
 */
public class DirectoryTree extends Tree implements ChildrenLoader<FileInfo>, EventListener<Event> {

    private File selected;
    private EntityTreeModel<FileInfo> treeModel;

    private RootTreeNode<FileInfo> rootNode;
    private DirectoryTreeNode rootDirectoryNode;
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

        rootNode = new RootTreeNode<>(file);
        rootDirectoryNode = new DirectoryTreeNode(file, this);

        rootNode.addChild(rootDirectoryNode);

        treeModel = new EntityTreeModel<>(rootNode);

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

        Collections.sort(subdirectories, Comparator.comparing(o -> o.getData().getName().toLowerCase()));

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

    public void open(File directory) {
        if (rootDirectory.equals(directory)) {
            rootNode.open();
        } else {
            DirectoryTreeNode startNode = rootDirectoryNode;
            if (!treeModel.isSelectionEmpty()) {
                startNode = (DirectoryTreeNode) treeModel.getSelection().stream().findFirst().orElse(null);
                if (startNode.getParent() != null && !startNode.getParent().equals(rootNode)) {
                    startNode = (DirectoryTreeNode) startNode.getParent();
                }
            }

            DirectoryTreeNode node = findNode(startNode, directory);
            if (node != null) {
                if (node.getParent() != null) {
                    treeModel.addOpenObject(node.getParent());
                }
                treeModel.addOpenObject(node);
                treeModel.setSelection(Collections.singletonList(node));
                selected = directory;
            }
        }
    }

    /**
     * Find a directory node recurvise starting from parent node or root node
     *
     * @param parentNode
     * @param directory
     * @return
     */
    public DirectoryTreeNode findNode(DirectoryTreeNode parentNode, File directory) {
        if (parentNode.getData().getFile().equals(directory)) {
            return parentNode;
        } else {
            if (!parentNode.isOpen()) {
                loadChildren(parentNode);
            }
            //Find direct children
            for (TreeViewNode<FileInfo> child : parentNode.getChildren()) {
                if (child.getData().getFile().equals(directory)) {
                    return (DirectoryTreeNode) child;
                }
            }

            //Find Tree
            for (TreeViewNode<FileInfo> child : parentNode.getChildren()) {
                DirectoryTreeNode node = findNode((DirectoryTreeNode) child, directory);
                if (node != null) {
                    return node;
                }
            }
        }
        return null;
    }

    public DirectoryTreeNode getRootDirectoryNode() {
        return rootDirectoryNode;
    }

    public boolean isShowHiddenFolders() {
        return showHiddenFolders;
    }

    public void setShowHiddenFolders(boolean showHiddenFolders) {
        this.showHiddenFolders = showHiddenFolders;
    }

}
