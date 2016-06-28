/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.dynamia.modules.filemanager;

import tools.dynamia.ui.icons.AbstractIconsProvider;
import tools.dynamia.ui.icons.InstallIcons;

/**
 *
 * @author mario
 */
@InstallIcons
public class FileManagerIcons extends AbstractIconsProvider{

    @Override
    public String getPrefix() {
        return "filemanager/icons";
    }

    @Override
    public String getExtension() {
        return "png";
    }
    
}
