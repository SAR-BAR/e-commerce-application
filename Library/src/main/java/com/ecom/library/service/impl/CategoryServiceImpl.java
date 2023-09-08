package com.ecom.library.service.impl;

import com.ecom.library.dto.CategoryDto;
import com.ecom.library.model.Category;
import com.ecom.library.repository.CategoryRepository;
import com.ecom.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*  ----------------------------------Category Service Implementation----------------------------------------    */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /*  ----------------------------------Save Category----------------------------------------------------    */
    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return categoryRepository.save(categorySave);
    }

    /*  ----------------------------------Update category----------------------------------------------------    */
    @Override
    public Category update(Category category) {
        Category categoryUpdate = categoryRepository.getById(category.getId());
        categoryUpdate.setName(category.getName());
        return categoryRepository.save(categoryUpdate);
    }

    /*  ----------------------------------Find all active categories----------------------------------------------------    */
    @Override
    public List<Category> findAllByActivatedTrue() {

        return categoryRepository.findAllByActivatedTrue();
    }

    /*  ----------------------------------Find all categories----------------------------------------------------    */
    @Override
    public List<Category> findALl() {

        return categoryRepository.findAll();
    }

    /*  ----------------------------------Find category by id----------------------------------------------------    */
    @Override
    public Optional<Category> findById(Long id) {

        return categoryRepository.findById(id);
    }

    /*  ----------------------------------Delete category----------------------------------------------------    */
    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.getById(id);
        //set it inactive
        category.setActivated(false);
        //enable delete mode
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    /*  ----------------------------------Enable the category----------------------------------------------------    */
    @Override
    public void enableById(Long id) {
        Category category = categoryRepository.getById(id);
        //set it in active mode
        category.setActivated(true);
        //disable the deleted mode
        category.setDeleted(false);
        categoryRepository.save(category);
    }

    /*  ----------------------------------Get category size----------------------------------------------------    */
    @Override
    public List<CategoryDto> getCategoriesAndSize() {
        List<CategoryDto> categories = categoryRepository.getCategoriesAndSize();
        return categories;
    }
}
