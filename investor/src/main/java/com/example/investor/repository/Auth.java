package com.example.investor.repository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@Repository
@FeignClient(name = "auth")
public interface Auth {


  @GetMapping("/videos/{hash}")
  Video readOne(@PathVariable String hash);


}
