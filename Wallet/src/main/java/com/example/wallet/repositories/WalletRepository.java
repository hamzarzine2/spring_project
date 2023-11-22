package com.example.wallet.repositories;

import com.example.wallet.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Position, String>{
}
