package thigk.ntu63131236.nguyenquocthai_ktgiuaki.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import thigk.ntu63131236.nguyenquocthai_ktgiuaki.models.Product;

@Service
public interface ProductService {
   public Page<Product> findPaginated(Pageable pageable);
   public Page<Product> searchProducts(String keyword, Pageable pageable);
}
