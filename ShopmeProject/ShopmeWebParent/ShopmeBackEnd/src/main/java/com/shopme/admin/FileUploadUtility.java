package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtility {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtility.class);

	public static void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try (InputStream inputStream = file.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("could not save file:" + fileName, e);
		}
	}

	public static void cleanDir(String dir) {
		Path dirpath = Paths.get(dir);
		try {
			Files.list(dirpath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						LOGGER.error("could not delete file :" + file);
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("could not list Directory :" + dirpath);
		}
	}
	
	public static void removeDir(String dir) {
		Path dirpath = Paths.get(dir);
		try (Stream<Path> walk = Files.walk(dirpath)) {
			walk.sorted(Comparator.reverseOrder()).forEach(FileUploadUtility::deleteDirectory);
        } catch (Exception e) {
			LOGGER.error("could not list Directory :" + dirpath);
		}
	}
	
	public static void deleteDirectory(Path path) {
	      try {
	          Files.delete(path);
	      } catch (IOException e) {
	    	  LOGGER.error("Unable to delete this path : %s%n%s", path, e);
	      }
	  }
}
