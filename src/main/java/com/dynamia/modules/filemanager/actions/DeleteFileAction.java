/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager.actions;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.Callback;

/**
 *
 * @author mario_2
 */
@InstallAction
public class DeleteFileAction extends FileManagerAction {

    public DeleteFileAction() {
        setName("Delete Selected File");
        setImage("delete");
        setIndex(1);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final FileInfo fileInfo = (FileInfo) evt.getData();
        final FileManager mgr = (FileManager) evt.getSource();
        if (fileInfo != null) {
            UIMessages.showQuestion("Are you sure to delete " + fileInfo.getName() + "?", new Callback() {

                @Override
                public void doSomething() {
                    fileInfo.getFile().delete();
                    mgr.updateUI();
                    UIMessages.showMessage(fileInfo.getName() + " deleted successfull");
                }
            });
        } else {
            UIMessages.showMessage("Select file to delete", MessageType.WARNING);
        }
    }

}
