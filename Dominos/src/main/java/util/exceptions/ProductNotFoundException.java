package util.exceptions;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException() {
        super("Product not found!");
    }
}
