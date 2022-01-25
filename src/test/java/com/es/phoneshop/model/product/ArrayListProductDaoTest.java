package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private Currency usd;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
        usd = Currency.getInstance("USD");
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        int listSize = productDao.findProducts().size();
        assertEquals(product, productDao.getProduct((long) listSize - 1));
    }

    @Test
    public void testSaveNewId() throws ProductNotFoundException {
        int listSize = productDao.findProducts().size();
        Product product = new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg");
        productDao.save(product);
        assertTrue(product.getId() > 0);
        assertEquals(listSize + 1, productDao.findProducts().size());
        Product result = productDao.getProduct((long) listSize);
        assertEquals(product, result);
        assertEquals("nokia3310", result.getCode());
        assertNotEquals("Siemens C61", result.getDescription());
    }

    @Test
    public void testSaveUsedId() throws ProductNotFoundException {
        productDao.save(new Product(1L, "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        int listSize = productDao.findProducts().size();
        Product product = new Product(1L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg");
        productDao.save(product);
        assertTrue(product.getId() > 0);
        assertEquals(listSize, productDao.findProducts().size());
        Product result = productDao.getProduct(1L);
        assertEquals(product, result);
        assertNotEquals(30, result.getStock());
        assertEquals(usd, result.getCurrency());
    }

    @Test
    public void testDeleteExistingProduct() throws ProductNotFoundException {
        Product product = new Product(0L, "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg");
        productDao.save(product);
        productDao.save(new Product(1L, "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.delete(0L);
        assertNotEquals(product, productDao.getProduct(0L));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNonExistingProduct() throws ProductNotFoundException {
        productDao.delete(-5L);
    }
}
