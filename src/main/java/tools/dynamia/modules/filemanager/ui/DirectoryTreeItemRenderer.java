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
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconsTheme;

/**
 * @author Mario Serrano Leones
 */
public class DirectoryTreeItemRenderer implements TreeitemRenderer<DirectoryTreeNode>, EventListener<Event> {

    @Override
    public void render(Treeitem item, DirectoryTreeNode data, int index) throws Exception {
        if (data != null) {
            item.setValue(data);
            item.setLabel(data.getData().getName());
            item.setTooltiptext(data.getData().getDescription());
            item.addEventListener(Events.ON_OPEN, this);
            item.addEventListener(Events.ON_CLOSE, this);


            setupIcon(item);
        }
    }

    @Override
    public void onEvent(Event event) throws Exception {
        Treeitem item = (Treeitem) event.getTarget();
        DirectoryTreeNode node = item.getValue();
        if (item.isOpen()) {
            node.load();
        } else {
            node.getChildren().clear();
        }

        setupIcon(item);
    }

    private void setupIcon(Treeitem item) {

        DirectoryTreeNode node = item.getValue();

        if (item.isOpen()) {
            item.setImage(IconsTheme.get().getIcon("folder-open").getRealPath(IconSize.SMALL));
        } else {
            item.setImage(IconsTheme.get().getIcon("folder").getRealPath(IconSize.SMALL));
        }

        if (!node.getData().getFile().canRead() || !node.getData().getFile().canWrite()) {
            if (item.isOpen()) {
                item.setImage(IconsTheme.get().getIcon("folder-red-open").getRealPath(IconSize.SMALL));
            } else {
                item.setImage(IconsTheme.get().getIcon("folder-red").getRealPath(IconSize.SMALL));
            }
        }
    }

}
