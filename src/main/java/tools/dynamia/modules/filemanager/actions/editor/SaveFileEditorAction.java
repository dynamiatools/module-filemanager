/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.actions.editor;

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
 * @author mario
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
