package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemDeleteServletTest {
    public static final String PRODUCT_ID = "9";
    public static final int PRODUCT_STOCK = 1;
    public static final int WANTED_NUMBER_OF_INVOCATIONS = 2;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig config;

    @Mock
    private Product product;

    @Mock
    private HttpSession session;

    private CartItemDeleteServlet servlet = new CartItemDeleteServlet();
    private CartService cartService;
    private ProductDao productDao;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        cartService = HttpSessionCartServiceImpl.getInstance();
        productDao = ArrayListProductDao.getInstance();
        when(product.getId()).thenReturn(PRODUCT_ID);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(anyString())).thenReturn(PRODUCT_ID);
        when(product.getStock()).thenReturn(PRODUCT_STOCK);
    }

    @Test
    public void doPostShouldDeleteProductWhenIdExists() throws IOException {
        productDao.save(product);
        cartService.addProduct(request, product.getId(), PRODUCT_STOCK);

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
        verify(request, times(WANTED_NUMBER_OF_INVOCATIONS)).getParameter(anyString());
    }

    @Test(expected = ProductNotFoundException.class)
    public void doPostShouldThrowProductNotFoundExceptionWhenIdIsNotFound() throws IOException {
        when(request.getParameter(anyString())).thenReturn(PRODUCT_ID);

        servlet.doPost(request, response);
    }

    @Test(expected = IdNotFoundException.class)
    public void doPostShouldThrowIdNotFoundExceptionWhenIdIsIncorrect() throws IOException {
        when(request.getParameter(anyString())).thenReturn(StringUtils.EMPTY);

        servlet.doPost(request, response);
    }
}
