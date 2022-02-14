package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceTest {
    public static final int NUMBER_OF_INVOCATIONS = 2;
    public static final int QUANTITY9 = 9;
    public static final int QUANTITY3 = 3;
    public static final int ZERO_QUANTITY = 0;
    public static final int NEGATIVE_QUANTITY3 = -3;
    public static final String PRODUCT_ID = "3";

    private CartService httpSessionCartService;
    private ProductDao productDao;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private Product product;

    @Before
    public void setup() {
        httpSessionCartService = HttpSessionCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void getCartShouldSetAttributeWhenAttributeIsNull() {
        when(session.getAttribute(anyString())).thenReturn(null);

        Cart cart = httpSessionCartService.getCart(request);

        verify(session).getAttribute(anyString());
        verify(session).setAttribute(anyString(), any());
        assertNotNull(cart);
    }

    @Test
    public void getCartShouldGetAndReturnExistCartWhenAttributeNotNull() {
        Cart cart = new Cart();
        when(session.getAttribute(anyString())).thenReturn(cart);

        Cart resultCart = httpSessionCartService.getCart(request);

        verify(session, times(NUMBER_OF_INVOCATIONS)).getAttribute(anyString());
        assertNotNull(resultCart);
        assertEquals(cart, resultCart);
    }

    @Test(expected = IdNotFoundException.class)
    public void addProductShouldThrowIdNotFoundExceptionWhenProductIdIsNull() {
        httpSessionCartService.addProduct(request, null, QUANTITY9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductShouldThrowIllegalArgumentExceptionWhenQuantityIsNonPositive() {
        httpSessionCartService.addProduct(request, PRODUCT_ID, NEGATIVE_QUANTITY3);
    }

    @Test(expected = OutOfStockException.class)
    public void addProductShouldThrowOutOfStockExceptionWhenStockLessThanQuantity() {
        when(product.getId()).thenReturn(PRODUCT_ID);
        when(product.getStock()).thenReturn(ZERO_QUANTITY);
        productDao.save(product);

        httpSessionCartService.addProduct(request, PRODUCT_ID, QUANTITY3);
    }

    @Test
    public void addProductShouldAddProductToCartAndSetStockWhenAllParametersAreCorrect() {
        when(product.getId()).thenReturn(PRODUCT_ID);
        when(product.getStock()).thenReturn(QUANTITY3);
        productDao.save(product);

        httpSessionCartService.addProduct(request, PRODUCT_ID, QUANTITY3);

        verify(product).setStock(anyInt());
    }
}
