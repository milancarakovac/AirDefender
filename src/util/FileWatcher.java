package util;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWatcher extends  Thread{

    private Path affectedFile;
    private Path path;
    private String fileToWatch;
    private boolean isThereChange;
    private WatchEvent.Kind<Path> eventToTrack;

    public FileWatcher(Path path, String file, WatchEvent.Kind<Path> eventToTrack){
        this.path = path;
        this.eventToTrack = eventToTrack;
        fileToWatch = file;
    }

    @Override
    public void run() {
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't get watch service", ex);
        }
        WatchKey key = null;
        try {
            path.register(watchService,eventToTrack);
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (fileToWatch.length() > 0) {
                        if (((Path) event.context()).endsWith(fileToWatch) && event.kind() == eventToTrack) {
                            isThereChange = true;
                        }
                    }else if(event.kind() == eventToTrack) {
                        isThereChange = true;
                        affectedFile = (Path) event.context();
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't get watch event", ex);
        }

    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getFileToWatch() {
        return fileToWatch;
    }

    public void setFileToWatch(String fileToWatch) {
        this.fileToWatch = fileToWatch;
    }

    public boolean isThereChange() {
        boolean tmp = isThereChange;
        isThereChange = false;
        return tmp;
    }

    public Path getAffectedFile() {
        return Paths.get(path.toString() + "/" + affectedFile.toString());
    }

    public void setAffectedFile(Path affectedFile) {
        this.affectedFile = affectedFile;
    }
}