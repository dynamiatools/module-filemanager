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
package tools.dynamia.modules.filemanager.actions.editor;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.aspectj.util.FileUtil;
import org.zkoss.io.FileWriter;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.modules.filemanager.ui.TextEditor;
import tools.dynamia.ui.UIMessages;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
public class SaveFileEditorAction extends TextEditorAction {

    public SaveFileEditorAction() {
        setName("Save");
        setImage("save");
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        TextEditor textEditor = (TextEditor) evt.getSource();
        String content = (String) evt.getData();
        write(content, textEditor.getFile());
    }

    private void write(String content, File file) {
        try {
            Files.write(file.toPath(), content.getBytes("UTF-8"));
            
            String lines[] = content.split("\\r?\\n");
            //Files.write(file.toPath(),Arrays.asList(lines), Charset.forName("UTF-8"));

            UIMessages.showMessage("Changes saved");
        } catch (IOException ex) {
            Logger.getLogger(SaveFileEditorAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
