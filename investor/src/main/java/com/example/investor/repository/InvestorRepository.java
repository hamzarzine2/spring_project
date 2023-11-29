package com.example.investor.repository;

import com.example.investor.model.Investor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends CrudRepository<Investor, String> {


}

