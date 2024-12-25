package com.shopme.admin.product.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtility;
import com.shopme.common.entities.Product;
import com.shopme.common.entities.ProductImage;

public class ProductSaveHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

	static void saveUploadedImage(MultipartFile mainImage, MultipartFile[] extraImage, Product saveProduct)
			throws IOException {
		if (!mainImage.isEmpty()) {
			String mainImageName = StringUtils.cleanPath(mainImage.getOriginalFilename());
			String uploadDir = "../product-images/" + saveProduct.getId();
			FileUploadUtility.cleanDir(uploadDir);
			FileUploadUtility.saveFile(uploadDir, mainImageName, mainImage);
		}
		if (extraImage.length > 0) {
			String uploadDir = "../product-images/" + saveProduct.getId() + "/extras";
			for (MultipartFile file : extraImage) {
				if (file.isEmpty())
					continue;
				String extraImageName = StringUtils.cleanPath(file.getOriginalFilename());
				FileUploadUtility.saveFile(uploadDir, extraImageName, file);
			}
		}
	}

	static void setExtraImageName(MultipartFile[] extraImage, Product product) {
		if (extraImage.length > 0) {
			for (MultipartFile file : extraImage) {
				if (!file.isEmpty()) {
					String extraImageName = StringUtils.cleanPath(file.getOriginalFilename());
					if (!product.containsImageName(extraImageName))
						product.addExtraImage(extraImageName);
				}
			}
		}
	}

	static void setMainImageName(MultipartFile mainImage, Product product) {
		if (!mainImage.isEmpty()) {
			String mainImageName = StringUtils.cleanPath(mainImage.getOriginalFilename());
			product.setMainImage(mainImageName);
		}
	}

	static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues,
			Product product) {
		if (detailNames == null || detailNames.length == 0)
			return;

		for (int i = 0; i < detailNames.length; i++) {
			Long id = Long.parseLong(detailIDs[i]);
			String name = detailNames[i];
			String value = detailValues[i];
			if (id != 0) {
				product.addDetails(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetails(name, value);
			}
		}
	}

	static void setExistingExtraImage(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();
		for (int i = 0; i < imageIDs.length; i++) {
			Long id = Long.parseLong(imageIDs[i]);
			String name = imageNames[i];
			images.add(new ProductImage(id, name, product));
		}
		product.setImages(images);
	}

	static void deleteExtraImagesWeredExistInFolder(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirpath = Paths.get(extraImageDir);
		try {
			Files.list(dirpath).forEach(file -> {
				String fileName = file.toFile().getName();
				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted Extra Image :" + fileName);
					} catch (Exception e) {
						LOGGER.error("Could not delete Extra Image :" + fileName);
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("Could not List Directory :" + dirpath);
		}
	}
}
