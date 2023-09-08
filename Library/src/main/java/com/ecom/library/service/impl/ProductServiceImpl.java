package com.ecom.library.service.impl;

import com.ecom.library.dto.ProductDto;
import com.ecom.library.model.Product;
import com.ecom.library.repository.ProductRepository;
import com.ecom.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/*  ----------------------------------Product Service Implementation----------------------------------------    */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    private final ImageUpload imageUpload;

    /*  ----------------------------------Find all products----------------------------------------------------    */
    @Override
    public List<Product> findAll() {

        return productRepository.findAll();
    }

    /*  --------------------------------------Transfer Data------------------------------------------------    */
    @Override
    public List<ProductDto> products() {

        return transferData(productRepository.getAllProduct());
    }

    /*  ----------------------------------Get all products----------------------------------------------------    */
    @Override
    public List<ProductDto> allProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    /*  ----------------------------------Save Product----------------------------------------------------    */
    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        //Create new product
        Product product = new Product();
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                //set image for product
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            //Set details received from Dto object
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            //enable the product
            product.set_deleted(false);
            product.set_activated(true);
            //save the product
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*  ----------------------------------Update Product----------------------------------------------------    */
    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try {
            //First retrieve product from repository
            Product productUpdate = productRepository.getById(productDto.getId());
            //For product image
            if (imageProduct == null) {
                productUpdate.setImage(productUpdate.getImage());
            } else {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
            //Update other details
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setId(productUpdate.getId());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            //Save Product
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*  ----------------------------------Enable product----------------------------------------------------    */
    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    /*  ----------------------------------Delete product----------------------------------------------------    */
    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    /*  ----------------------------------Return product by id----------------------------------------------------    */
    @Override
    public ProductDto getById(Long id) {
        //Create Dto object to transfer
        ProductDto productDto = new ProductDto();
        //Retrieve product from repository
        Product product = productRepository.getById(id);
        //Populate Dto object
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());
        //Return Dto
        return productDto;
    }

    /*  ----------------------------------Find product by id----------------------------------------------------    */
    @Override
    public Product findById(Long id) {

        return productRepository.findById(id).get();
    }

    /*  ----------------------------------Return random products list----------------------------------------------------    */
    @Override
    public List<ProductDto> randomProduct() {

        return transferData(productRepository.randomProduct());
    }

    /*  ----------------------------------Search product by keyword/ page no.----------------------------------------------------    */
    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameOrDescription(keyword);
        //Placed all found products in List of Dto to transfer
        List<ProductDto> productDtoList = transferData(products);

        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    /*  ----------------------------------Get all products for page number----------------------------------------------------    */
    @Override
    public Page<ProductDto> getAllProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoLists = this.allProduct();
        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
        return productDtoPage;
    }

    /*  ----------------------------------Get all products for customer by page no----------------------------------------------------    */
    @Override
    public Page<ProductDto> getAllProductsForCustomer(int pageNo) {

        return null;
    }

    /*  ----------------------------------Find by category----------------------------------------------------    */
    @Override
    public List<ProductDto> findAllByCategory(String category) {
        return transferData(productRepository.findAllByCategory(category));
    }

    /*  ----------------------------------Filters----------------------------------------------------    */
    @Override
    public List<ProductDto> filterHighProducts() {
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        return transferData(productRepository.filterLowerProducts());
    }

    @Override
    public List<ProductDto> listViewProducts() {
        return transferData(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return transferData(productRepository.getProductByCategoryId(id));
    }

    /*  ----------------------------------Search product by keyword----------------------------------------------------    */
    @Override
    public List<ProductDto> searchProducts(String keyword) {
        return transferData(productRepository.searchProducts(keyword));
    }

    /*  ----------------------------------Accept order----------------------------------------------------    */
    private PageImpl toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return (PageImpl) Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList);
    }


    private List<ProductDto> transferData(List<Product> products) {
        //Create Dto object
        List<ProductDto> productDtos = new ArrayList<>();
        //For every item in given list
        for (Product product : products) {
            //Prepare Dto for every item
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtos.add(productDto);
        }
        return productDtos;
    }
}
