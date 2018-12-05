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
 * @author Mario Serrano Leones
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
