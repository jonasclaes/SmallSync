package be.jonasclaes.smallsync.sync;

import java.io.File;

public abstract class SyncClient {
    private File sourceFolder = null;
    private File destinationFolder = null;
    private boolean overwrite = false;

    public SyncClient(String sourceFolder, String destinationFolder) {
        this.sourceFolder = new File(sourceFolder);
        this.destinationFolder = new File(destinationFolder);
    }

    public SyncClient(String sourceFolder, String destinationFolder, boolean overwrite) {
        this.sourceFolder = new File(sourceFolder);
        this.destinationFolder = new File(destinationFolder);
        this.overwrite = overwrite;
    }

    public File getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = new File(sourceFolder);
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = new File(destinationFolder);
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public abstract void sync();
}
