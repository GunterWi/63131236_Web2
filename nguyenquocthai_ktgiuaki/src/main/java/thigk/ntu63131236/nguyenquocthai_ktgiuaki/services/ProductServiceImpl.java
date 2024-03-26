package thigk.ntu63131236.nguyenquocthai_ktgiuaki.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import thigk.ntu63131236.nguyenquocthai_ktgiuaki.models.Product;

@Service
public class ProductServiceImpl implements ProductService {
     private final List<Product> products = new ArrayList<>();

    public ProductServiceImpl() {
        products.add(new Product("P001", "Máy giặt", 100.0,""));
        products.add(new Product("P002", "Cơm", 200.0,""));
        products.add(new Product("P003", "Cơm", 200.0,""));
        products.add(new Product("P004", "Cơm", 200.0,""));
        products.add(new Product("P005", "Cơm", 200.0,""));
        products.add(new Product("P006", "Cơm", 200.0,""));
        products.add(new Product("P007", "Cơm", 200.0,""));
        products.add(new Product("P008", "Cơm", 200.0,""));
        products.add(new Product("P009", "Cơm", 200.0,""));
        products.add(new Product("P0010", "Cơm", 200.0,""));
        products.add(new Product("P0011", "Cơm", 200.0,""));
        products.add(new Product("P0012", "Cơm", 200.0,""));
        products.add(new Product("P0013", "Cơm", 200.0,""));
        products.add(new Product("P0014", "Cơm", 200.0,""));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductByCode(String code) {
        return products.stream()
                .filter(product -> product.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null); 
    }
    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public Page<Product> findPaginated(Pageable pageable) {
       	int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;

        if (products.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }
        Page<Product> sinhvienPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());
        return sinhvienPage;
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return findPaginated(pageable);
        }
        List<Product> filteredProducts = products.stream()
            .filter(product -> product.getCode().equalsIgnoreCase(keyword) || product.getName().contains(keyword))
                                                                        .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;

        if (filteredProducts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, filteredProducts.size());
            list = filteredProducts.subList(startItem, toIndex);
        }    
        return new PageImpl<>(list, pageable, filteredProducts.size());
    }
}
