package tools.dynamia.modules.filemanager.viewers;

import tools.dynamia.integration.sterotypes.Component;
import tools.dynamia.viewers.ViewRenderer;
import tools.dynamia.viewers.ViewType;

@Component
public class FileManagerViewType implements ViewType {

	@Override
	public String getName() {
		return "filemanager";
	}

	@Override
	public ViewRenderer getViewRenderer() {
		return new FileManagerViewRenderer();
	}

}
