package thigk.ntu63131236.nguyenquocthai_ktgiuaki.services;

import java.util.ArrayList;
import java.util.List;

import thigk.ntu63131236.nguyenquocthai_ktgiuaki.models.Product;

public class ProductService {
    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product("P001", "Máy giặt", 100.0,""));
        products.add(new Product("P002", "Cơm", 200.0,""));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    

    public void addProduct(Product product) {
        products.add(product);
    }
}
