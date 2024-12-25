package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.common.entities.Brand;

@Service
@Transactional
public class BrandService {
	public static final int BRAND_PER_PAGE = 4;

	@Autowired
	private BrandRepository brandRepository;

	public List<Brand> listBrands() {
		return (List<Brand>) brandRepository.findAll();
	}

	public Page<Brand> listByPageBrands(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE);
		return brandRepository.findAll(pageable);
	}

	public Brand saveBrand(Brand brand) {
		Brand save = brandRepository.save(brand);
		return save;
	}

	public Brand getBrandById(Long id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand!!");
		}
	}

	public void delete(Long id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand!!");
		}
		brandRepository.deleteById(id);
	}

	public String checkUnique(Long id, String name) {
		boolean iscreatedNew = (id == null || id == 0);

		Brand brandByName = brandRepository.findByName(name);
		if (iscreatedNew) {
			if (brandByName != null)
				return "DuplicateName";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "DuplicateName";
			}
		}
		return "OK";
	}

}
