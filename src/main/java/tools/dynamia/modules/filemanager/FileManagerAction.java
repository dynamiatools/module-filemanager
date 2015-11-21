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
 * @author mario_2
 */
public abstract class FileManagerAction extends AbstractAction implements Indexable {

    private int index;

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

}
