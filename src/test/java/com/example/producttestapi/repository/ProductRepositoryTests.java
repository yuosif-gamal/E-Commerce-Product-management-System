package com.example.producttestapi.repository;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.repos.CategoryRepo;
import com.example.producttestapi.repos.ProductRepo;
import com.example.producttestapi.repos.VoucherRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private VoucherRepo voucherRepo;

    @Test
    public void ProductRepo_FindAllProductsByCategoryID_ReturnAllProducts(){

        Category category1 = Category.builder().name("elec").build();
        Category category2 = Category.builder().name("samsung").build();

        categoryRepo.save(category1);
        categoryRepo.save(category2);


        Product product1 =Product.builder().name("samsung1").price(10000).categoryID(category1).build();
        Product product2 =Product.builder().name("samsung2").price(15000).categoryID(category1).build();
        Product product3 =Product.builder().name("samsung3").price(103000).categoryID(category2).build();

        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);

        //List<Product> products = productRepo.findAllByCategory(category1.getId());
        //List<Product> products1 = productRepo.findAllByCategory(category2.getId());

        //Assertions.assertThat(products.size()).isEqualTo(2);
        //Assertions.assertThat(products1.size()).isEqualTo(1);

    }

    @Test
    public void ProductRepo_FindAllProductsByVoucherCode_ReturnAllProductsByVoucherCode(){

        Voucher voucher1 = Voucher.builder().code("SALE").discount(BigDecimal.valueOf(10)).build();
        Voucher voucher2= Voucher.builder().code("SALE2").discount(BigDecimal.valueOf(10)).build();

        voucherRepo.save(voucher1);
        voucherRepo.save(voucher2);

        Product product1 =Product.builder().name("samsung1").price(10000).voucherCode(voucher1).build();
        Product product2 =Product.builder().name("samsung2").price(15000).voucherCode(voucher1).build();
        Product product3 =Product.builder().name("samsung3").price(103000).voucherCode(voucher2).build();

        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);

        List<Product> products = productRepo.findAllByVoucherID(voucher1.getId());
        List<Product> products1 = productRepo.findAllByVoucherID(voucher2.getId());

        Assertions.assertThat(products.size()).isEqualTo(2);
        Assertions.assertThat(products1.size()).isEqualTo(1);

    }
}
