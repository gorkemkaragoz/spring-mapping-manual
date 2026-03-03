package com.gorkem.manuelmap.repo;

import com.gorkem.manuelmap.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
