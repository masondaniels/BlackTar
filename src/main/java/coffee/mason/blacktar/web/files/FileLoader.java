package coffee.mason.blacktar.web.files;

import org.teavm.interop.AsyncCallback;

import coffee.mason.blacktar.util.AjaxUtil;

public abstract class FileLoader {

	private String[] paths;
	private int toLoad; // files to load
	private int filesLoaded; // files loaded

	private String[] content; // content of files

	public FileLoader(String... paths) {
		this.paths = paths;
		this.toLoad = paths.length;
		content = new String[paths.length];
		loadPaths();
	}

	private void loadPaths() {
		for (int i = 0; i < paths.length; i++) {

			final int z = i;
			AjaxUtil.get(paths[i], new AsyncCallback<String>() {

				@Override
				public void error(Throwable e) {
					System.err.println("Error while loading file:\n");
					e.printStackTrace();
				}

				@Override
				public void complete(String result) {
					content[z] = result;
					filesLoaded++;
					checkIfComplete();
				}
			});
		}
	}

	private void checkIfComplete() {
		if (isComplete()) {
			onComplete(content);
		}

	}

	public abstract void onComplete(String[] content);

	private boolean isComplete() {
		return filesLoaded == toLoad;
	}

}
