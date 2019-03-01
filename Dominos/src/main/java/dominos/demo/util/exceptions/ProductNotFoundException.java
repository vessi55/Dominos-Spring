package dominos.demo.util.exceptions;

import dominos.demo.util.exceptions.BaseException;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException() {
        super("Product not found!");
    }
}
