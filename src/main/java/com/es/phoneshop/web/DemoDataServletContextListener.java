package com.es.phoneshop.web;


import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistoryItem;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DemoDataServletContextListener implements ServletContextListener {
    public static final String INSERT_DEMO_DATA = "insertDemoData";
    public static final String TRUE = "true";
    public static final String FAILED = "Failed to insert demo data";

    private ProductDao productDao;

    public DemoDataServletContextListener() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (StringUtils.compare(servletContextEvent.getServletContext().getInitParameter(INSERT_DEMO_DATA), TRUE) == 0) {
            List<Product> sampleProducts = getSampleProducts();
            for (Product product : sampleProducts) {
                try {
                    productDao.save(product);
                } catch (ProductNotFoundException e) {
                    throw new RuntimeException(FAILED, e);
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        products.add(new Product.Builder().withCode("sgs").withDescription("Samsung Galaxy S").withPrice(new BigDecimal(100)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg").withPriceHistory(makePair("09.01.2019", new BigDecimal(125)), makePair("09.01.2020", new BigDecimal(115))).build());
        products.add(new Product.Builder().withCode("sgs2").withDescription("Samsung Galaxy S II").withPrice(new BigDecimal(200)).withCurrency(usd).withStock(0).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg").withPriceHistory(makePair("27.03.2020", new BigDecimal(170)), makePair("28.05.2020", new BigDecimal(190)), makePair("28.10.2020", new BigDecimal(200))).build());
        products.add(new Product.Builder().withCode("sgs3").withDescription("Samsung Galaxy S III").withPrice(new BigDecimal(300)).withCurrency(usd).withStock(5).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg").withPriceHistory(makePair("20.01.2019", new BigDecimal(320)), makePair("09.04.2020", new BigDecimal(310)), makePair("30.09.2020", new BigDecimal(330))).build());
        products.add(new Product.Builder().withCode("iphone").withDescription("Apple iPhone").withPrice(new BigDecimal(200)).withCurrency(usd).withStock(10).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg").withPriceHistory(makePair("04.09.2020", new BigDecimal(150)), makePair("05.11.2020", new BigDecimal(180))).build());
        products.add(new Product.Builder().withCode("iphone6").withDescription("Apple iPhone 6").withPrice(new BigDecimal(1000)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg").withPriceHistory(makePair("03.02.2020", new BigDecimal(900)), makePair("02.12.2021", new BigDecimal(950))).build());
        products.add(new Product.Builder().withCode("htces4g").withDescription("HTC EVO Shift 4G").withPrice(new BigDecimal(320)).withCurrency(usd).withStock(3).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg").withPriceHistory(makePair("01.01.2020", new BigDecimal(350)), makePair("08.06.2021", new BigDecimal(360)), makePair("01.01.2022", new BigDecimal(380))).build());
        products.add(new Product.Builder().withCode("sec901").withDescription("Sony Ericsson C901").withPrice(new BigDecimal(420)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg").withPriceHistory(makePair("04.02.2019", new BigDecimal(410)), makePair("04.03.2019", new BigDecimal(405))).build());
        products.add(new Product.Builder().withCode("xperiaxz").withDescription("Sony Xperia XZ").withPrice(new BigDecimal(100)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg").withPriceHistory(makePair("17.02.2018", new BigDecimal(50)), makePair("17.04.2019", new BigDecimal(65))).build());
        products.add(new Product.Builder().withCode("nokia3310").withDescription("Nokia 3310").withPrice(new BigDecimal(70)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg").withPriceHistory(makePair("18.04.2020", new BigDecimal(90)), makePair("18.06.2020", new BigDecimal(80)), makePair("24.10.2020", new BigDecimal(100))).build());
        products.add(new Product.Builder().withCode("palmp").withDescription("Palm Pixi").withPrice(new BigDecimal(170)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg").withPriceHistory(makePair("23.07.2020", new BigDecimal(180)), makePair("09.12.2021", new BigDecimal(200))).build());
        products.add(new Product.Builder().withCode("simc56").withDescription("Siemens C56").withPrice(new BigDecimal(70)).withCurrency(usd).withStock(20).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg").withPriceHistory(makePair("20.03.2019", new BigDecimal(50)), makePair("25.08.2020", new BigDecimal(90))).build());
        products.add(new Product.Builder().withCode("simc61").withDescription("Siemens C61").withPrice(new BigDecimal(80)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg").withPriceHistory(makePair("13.03.2020", new BigDecimal(90)), makePair("24.09.2020", new BigDecimal(85)), makePair("09.01.2021", new BigDecimal(100))).build());
        products.add(new Product.Builder().withCode("simsxg75").withDescription("Siemens SXG75").withPrice(new BigDecimal(150)).withCurrency(usd).withStock(40).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg").withPriceHistory(makePair("14.09.2020", new BigDecimal(120)), makePair("15.03.2021", new BigDecimal(135))).build());
        return products;
    }

    private PriceHistoryItem makePair(String date, BigDecimal price) {
        return new PriceHistoryItem(date, price);
    }
}
