package com.example.gateway.data;

import com.example.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "authentication")
public interface AuthenticationProxy {

  @PostMapping("/authentication/connect")
  String connect(@RequestBody Credentials credentials);

  @PostMapping("/authentication/verify")
  String verify(@RequestBody String token);

  @PutMapping("/authentication/{username}")
  String updateCredentials(@RequestBody String token, @PathVariable String username);

  @PostMapping("/authentication/{pseudo}")
  void createCredentials(@PathVariable String pseudo, @RequestBody Credentials credentials);

  @PutMapping("/authentication/{pseudo}")
  void updateCredentials(@PathVariable String pseudo, @RequestBody Credentials credentials);

  @DeleteMapping("/authentication/{pseudo}")
  void deleteCredentials(@PathVariable String pseudo);

}

