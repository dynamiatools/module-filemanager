/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager;

import com.dynamia.tools.viewers.Indexable;
import com.dynamia.tools.web.actions.AbstractAction;
import com.dynamia.tools.web.actions.ActionRenderer;
import com.dynamia.tools.web.crud.actions.renderers.ToolbarbuttonActionRenderer;

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
