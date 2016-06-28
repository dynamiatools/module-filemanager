/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.actions;

import java.io.File;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

/**
 *
 * @author mario
 */
@InstallAction
public class NewTextFileAction extends FileManagerAction {

    public NewTextFileAction() {
        setName("New Text File");
        setImage("note");
        setPosition(5.0);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        FileManager manager = (FileManager) evt.getSource();
        ZKUtil.showInputDialog("Enter file name", String.class, e -> {
            String name = (String) e.getData();
            if (name != null && !name.isEmpty()) {
                File parent = manager.getDirectoryTree().getSelected();
                File newfile = new File(parent, name);
                newfile.createNewFile();
                UIMessages.showMessage(name + " file created successfull");
                manager.reloadSelected();
            }
        });
    }

}
