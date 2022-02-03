package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeast;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    public static final int INT30 = 30;
    public static final int INT70 = 70;
    public static final int INT80 = 80;
    public static final int INT100 = 100;
    public static final int INT120 = 120;
    public static final int INT170 = 170;
    public static final int INT1000 = 1000;
    public static final String USD = "USD";
    public static final String L0 = "L0";
    public static final String L1 = "L1";
    public static final String L5 = "L5";
    public static final String SGS = "sgs";
    public static final String SAMSUNG_GALAXY_S = "Samsung Galaxy S";
    public static final String NOKIA_3310 = "nokia3310";
    public static final String NOKIA_33101 = "Nokia 3310";
    public static final String SIEMENS_C_61 = "Siemens C61";
    public static final String SIMC_61 = "simc61";
    public static final String XPERIAXZ = "xperiaxz";
    public static final String SONY_XPERIA_XZ = "Sony Xperia XZ";
    public static final String PALMP = "palmp";
    public static final String PALM_PIXI = "Palm Pixi";
    public static final String IPHONE_6 = "iphone6";
    public static final String APPLE_I_PHONE_6 = "Apple iPhone 6";
    public static final String NOKIA_203310_JPG = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg";
    public static final String GALAXY_20_S_JPG = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    public static final String PALM_20_PIXI_JPG = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg";
    public static final String APPLE_20_I_PHONE_206_JPG = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg";
    public static final String SIEMENS_20_C_61_JPG = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg";
    public static final String SONY_20_XPERIA_20_XZ_JPG = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg";
    public static final int MIN_NUMBER_OF_INVOCATIONS = 1;
    public static final int NUMBER1 = 1;

    private ProductDao productDao;
    private static final Currency usd = Currency.getInstance(USD);

    @Mock
    private Product product;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException, IdNotFoundException {
        Product newProduct = new Product.Builder().withId(L1).withCode(SGS).withDescription(SAMSUNG_GALAXY_S).withPrice(new BigDecimal(INT100)).withCurrency(usd).withStock(INT100).withImageUrl(GALAXY_20_S_JPG).build();
        productDao.save(newProduct);
        assertEquals(newProduct, productDao.getProduct(L1));
    }

    @Test
    public void productsShouldEqualsWhenSavingProduct() throws ProductNotFoundException, IdNotFoundException {
        Product newProduct = new Product.Builder().withCode(NOKIA_3310).withDescription(NOKIA_33101).withPrice(new BigDecimal(INT70)).withCurrency(usd).withStock(INT100).withImageUrl(NOKIA_203310_JPG).build();
        productDao.save(newProduct);
        Product result = productDao.getProduct(newProduct.getId());
        assertEquals(newProduct, result);
    }

    @Test
    public void getCodeShouldEquals() throws ProductNotFoundException, IdNotFoundException {
        Product newProduct = new Product.Builder().withCode(NOKIA_3310).withDescription(NOKIA_33101).withPrice(new BigDecimal(INT70)).withCurrency(usd).withStock(INT100).withImageUrl(NOKIA_203310_JPG).build();
        productDao.save(newProduct);
        Product result = productDao.getProduct(newProduct.getId());
        assertEquals(NOKIA_3310, result.getCode());
    }

    @Test
    public void getDescriptionShouldNotEquals() throws ProductNotFoundException, IdNotFoundException {
        Product newProduct = new Product.Builder().withCode(NOKIA_3310).withDescription(NOKIA_33101).withPrice(new BigDecimal(INT70)).withCurrency(usd).withStock(INT100).withImageUrl(NOKIA_203310_JPG).build();
        productDao.save(newProduct);
        Product result = productDao.getProduct(newProduct.getId());
        assertNotEquals(SIEMENS_C_61, result.getDescription());
    }

    @Test
    public void shouldEqualsWhenComparingListSize() {
        int listSize = productDao.findProducts().size();
        Product newProduct = new Product.Builder().withCode(NOKIA_3310).withDescription(NOKIA_33101).withPrice(new BigDecimal(INT70)).withCurrency(usd).withStock(INT100).withImageUrl(NOKIA_203310_JPG).build();
        productDao.save(newProduct);
        assertEquals(listSize + NUMBER1, productDao.findProducts().size());
    }

    @Test
    public void getIdShouldReturnNotNull() {
        Product newProduct = new Product.Builder().withCode(NOKIA_3310).withDescription(NOKIA_33101).withPrice(new BigDecimal(INT70)).withCurrency(usd).withStock(INT100).withImageUrl(NOKIA_203310_JPG).build();
        assertNotNull(newProduct.getId());
    }

    @Test
    public void productsAndSizesShouldEqualsWhenSavingProductWithTheSameId() throws ProductNotFoundException, IdNotFoundException {
        productDao.save(new Product.Builder().withId(L1).withCode(SIMC_61).withDescription(SIEMENS_C_61).withPrice(new BigDecimal(INT80)).withCurrency(usd).withStock(INT30).withImageUrl(SIEMENS_20_C_61_JPG).build());
        int listSize = productDao.findProducts().size();
        Product product = new Product.Builder().withId(L1).withCode(XPERIAXZ).withDescription(SONY_XPERIA_XZ).withPrice(new BigDecimal(INT120)).withCurrency(usd).withStock(INT100).withImageUrl(SONY_20_XPERIA_20_XZ_JPG).build();
        productDao.save(product);
        assertEquals(listSize, productDao.findProducts().size());
        assertEquals(product, productDao.getProduct(L1));
    }

    @Test
    public void testSave() {
        MockitoAnnotations.initMocks(this);
        when(product.getId()).thenReturn(L1);
        productDao.save(product);
        productDao.getProduct(L1);
        verify(product, atLeast(MIN_NUMBER_OF_INVOCATIONS)).getId();
    }

    @Test
    public void testDeleteExistingProduct() throws ProductNotFoundException, IdNotFoundException {
        Product product = new Product.Builder().withId(L0).withCode(PALMP).withDescription(PALM_PIXI).withPrice(new BigDecimal(INT170)).withCurrency(usd).withStock(INT30).withImageUrl(PALM_20_PIXI_JPG).build();
        productDao.save(product);
        productDao.save(new Product.Builder().withId(L1).withCode(IPHONE_6).withDescription(APPLE_I_PHONE_6).withPrice(new BigDecimal(INT1000)).withCurrency(usd).withStock(INT30).withImageUrl(APPLE_20_I_PHONE_206_JPG).build());
        productDao.delete(L0);
        assertNotEquals(product, productDao.getProduct(L1));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNonExistingProduct() throws ProductNotFoundException, IdNotFoundException {
        productDao.delete(L5);
    }
}
