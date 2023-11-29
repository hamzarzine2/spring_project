package com.example.investor.repository;

import com.example.investor.model.UnSafeCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient("auth")
public interface AuthProxy {

  @PostMapping("/authentication/{username}")
  ResponseEntity<Void> createCredential(@PathVariable String username, @RequestBody UnSafeCredentials unSafeCredentials);
  @GetMapping("/authentication/{username}")
  ResponseEntity<Void> deleteCredential(@PathVariable String username);

}
