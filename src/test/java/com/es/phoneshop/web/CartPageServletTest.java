package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    public static final String PRODUCT_ID = "9";
    public static final int PRODUCT_STOCK = 2;
    public static final int PRODUCT_QUANTITY = 1;
    public static final int WANTED_NUMBER_OF_INVOCATIONS_3 = 3;
    public static final int WANTED_NUMBER_OF_INVOCATIONS_4 = 4;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ServletConfig config;

    @Mock
    private HttpSession session;

    @Mock
    Product product;

    private static final String CART_LIST = "cartList";
    private static final String TOTAL_PRICE = "totalPrice";
    public static final String REMOVE_PARAMETER = "remove";
    public static final String ADD_PARAMETER = "add";
    public static final String CART_ATTRIBUTE = "sessionCart";

    private CartPageServlet servlet = new CartPageServlet();
    private CartService cartService;
    private ProductDao productDao;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        cartService = HttpSessionCartServiceImpl.getInstance();
        productDao = ArrayListProductDao.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(CART_ATTRIBUTE)).thenReturn(new Cart());

        when(product.getId()).thenReturn(PRODUCT_ID);
        when(product.getStock()).thenReturn(PRODUCT_STOCK);
        when(product.getPrice()).thenReturn(new BigDecimal(100));
    }

    @Test
    public void doGetShouldAlwaysForward() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq(CART_LIST), any());
        verify(request).setAttribute(eq(TOTAL_PRICE), any());
    }

    @Test
    public void doPostShouldRemoveProductWhenIdIsExist() throws ServletException, IOException {
        when(request.getParameter(REMOVE_PARAMETER)).thenReturn(PRODUCT_ID);
        productDao.save(product);
        cartService.addProduct(request, PRODUCT_ID, PRODUCT_QUANTITY);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request, times(WANTED_NUMBER_OF_INVOCATIONS_3)).getParameter(any());
    }

    @Test
    public void doPostShouldAddProductWhenIdIsExist() throws ServletException, IOException {
        when(request.getParameter(ADD_PARAMETER)).thenReturn(PRODUCT_ID);
        productDao.save(product);
        cartService.addProduct(request, PRODUCT_ID, PRODUCT_QUANTITY);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request, times(WANTED_NUMBER_OF_INVOCATIONS_4)).getParameter(any());
    }

    @Test
    public void doPostShouldAddProductWhenIdIsNotExist() throws ServletException, IOException {
        when(request.getParameter(ADD_PARAMETER)).thenReturn(PRODUCT_ID);
        productDao.save(product);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request, times(WANTED_NUMBER_OF_INVOCATIONS_3)).getParameter(any());
    }
}
