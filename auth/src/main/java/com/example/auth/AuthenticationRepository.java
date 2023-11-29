package com.example.auth;



import com.example.auth.models.SafeCredentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends CrudRepository<SafeCredentials, String> {
}
