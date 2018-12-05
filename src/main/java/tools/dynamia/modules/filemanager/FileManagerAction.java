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
package tools.dynamia.modules.filemanager;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.viewers.Indexable;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;

/**
 *
 * @author Mario Serrano Leones
 */
public abstract class FileManagerAction extends AbstractAction implements Indexable {

	private int index;
	private boolean menuSupported;

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer();
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isMenuSupported() {
		return menuSupported;
	}

	public void setMenuSupported(boolean menuSupported) {
		this.menuSupported = menuSupported;
	}

}
