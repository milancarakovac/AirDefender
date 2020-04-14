package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
    public void compress(String dirPath) {
        final Path sourceDir = Paths.get(dirPath);
        DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy");
        Date date = new Date();
        String zipFileName = "zipped files\\backup_" + dateFormat.format(date) + ".zip";
        try (final var outputStream = new ZipOutputStream(new FileOutputStream(zipFileName))){
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    try {
                        Path targetFile = sourceDir.relativize(file);
                        if(targetFile != null && !targetFile.toFile().isDirectory() && targetFile.toFile().getName().endsWith(".txt")) {
                            outputStream.putNextEntry(new ZipEntry(targetFile.toFile().toString()));
                            byte[] bytes = Files.readAllBytes(targetFile);
                            outputStream.write(bytes, 0, bytes.length);
                            outputStream.closeEntry();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't put entry", ex);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Can't create zip file", ex);
        }
    }
}