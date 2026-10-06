package dev.portfolio.api;
import dev.portfolio.application.ReconciliationService;
import dev.portfolio.domain.ReconciliationRun;
import dev.portfolio.infrastructure.RunRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/api/reconciliations") public class ReconciliationController {
 private final ReconciliationService service;private final RunRepository repo;
 public ReconciliationController(ReconciliationService service,RunRepository repo){this.service=service;this.repo=repo;}
 public record Input(@NotBlank @Size(max=200000) String internalCsv,@NotBlank @Size(max=200000) String providerCsv){}
 @PostMapping public ReconciliationRun run(@Valid @RequestBody Input body){return service.run(body.internalCsv(),body.providerCsv());}
 @GetMapping("/{id}") public ReconciliationRun get(@PathVariable String id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 @GetMapping public List<ReconciliationRun> list(){return repo.findAll(org.springframework.data.domain.PageRequest.of(0,20,org.springframework.data.domain.Sort.by("createdAt").descending())).getContent();}
}
