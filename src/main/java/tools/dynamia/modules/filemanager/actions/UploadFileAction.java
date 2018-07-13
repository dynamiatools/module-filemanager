/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.actions;

import org.zkoss.util.media.Media;
import org.zkoss.zul.Fileupload;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author mario_2
 */
@InstallAction
public class UploadFileAction extends FileManagerAction {

    public UploadFileAction() {
        setName("Upload File");
        setImage("up");
        setPosition(3);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final FileManager mgr = (FileManager) evt.getSource();

        if (mgr.getCurrentDirectory() != null) {

            if (mgr.getCurrentDirectory().isReadOnly()) {
                UIMessages.showMessage("Selected directory is read only, cannot upload file", MessageType.WARNING);
                return;
            }

            Fileupload.get(5, t -> {
                Media medias[] = t.getMedias();
                for (Media media : medias) {
                    Path target = mgr.getCurrentDirectory().getFile().toPath().resolve(media.getName());

                    try {
                        Files.copy(media.getStreamData(), target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IllegalStateException e) {
                        Files.copy(new ByteArrayInputStream(media.getStringData().getBytes()), target, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                mgr.updateUI();
                UIMessages.showMessage("File(s) uploaded successfull");
            });

        } else {
            UIMessages.showMessage("Select destination directory", MessageType.WARNING);
        }
    }

}
