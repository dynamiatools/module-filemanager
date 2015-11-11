/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager.actions;

import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.modules.filemanager.FileManagerAction;
import com.dynamia.tools.integration.sterotypes.Component;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.ui.UIMessages;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Fileupload;

/**
 *
 * @author mario_2
 */
@InstallAction
public class UploadFileAction extends FileManagerAction {

    public UploadFileAction() {
        setName("Upload File");
        setImage("add");
        setPosition(3);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final FileManager mgr = (FileManager) evt.getSource();
        Fileupload.get(5,new EventListener<UploadEvent>() {

            @Override
            public void onEvent(UploadEvent t) throws Exception {
                Media medias[] = t.getMedias();
                for (Media media : medias) {
                    Path target = mgr.getCurrentDirectory().toPath().resolve(media.getName());
                    Files.copy(media.getStreamData(), target, StandardCopyOption.REPLACE_EXISTING);
                }
                mgr.updateUI();
                UIMessages.showMessage("File(s) uploaded successfull");
            }
        });
    }

}
