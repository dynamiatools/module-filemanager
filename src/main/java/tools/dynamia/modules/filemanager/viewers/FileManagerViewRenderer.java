package tools.dynamia.modules.filemanager.viewers;

import tools.dynamia.io.FileInfo;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.viewers.View;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.viewers.ViewRenderer;
import tools.dynamia.viewers.util.Viewers;

public class FileManagerViewRenderer implements ViewRenderer<FileInfo> {

	@Override
	public View<FileInfo> render(ViewDescriptor descriptor, FileInfo value) {
		FileManager fileManager = new FileManager();
		Viewers.setupView(fileManager, descriptor.getParams());
		
		fileManager.setValue(value);

		
		return fileManager;
	}

}
