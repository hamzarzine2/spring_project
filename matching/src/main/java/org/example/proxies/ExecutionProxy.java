package org.example.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
@FeignClient(name = "Execution")
public interface ExecutionProxy {

  @PostMapping("/execute/{ticker}/{seller}/{buyer}")
  void execute(@PathVariable String ticker, @PathVariable String seller, @PathVariable String buyer);
}
