package com.nqt.ListObjectPagination.services;


import org.springframework.stereotype.Service;

import com.nqt.ListObjectPagination.models.SinhVien;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public interface SinhvienService {
	public Page<SinhVien> findPaginated(Pageable pageable);
}