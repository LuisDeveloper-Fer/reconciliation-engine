package dev.portfolio.application;
import dev.portfolio.domain.*;
import dev.portfolio.infrastructure.RunRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.json.JsonMapper;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.HexFormat;
@Service public class ReconciliationService {
 private final RunRepository repo;private final EntityManager em;private final TransactionTemplate tx;
 public ReconciliationService(RunRepository repo,EntityManager em,TransactionTemplate tx){this.repo=repo;this.em=em;this.tx=tx;}
 public ReconciliationRun run(String internal,String provider){
  var a=Reconciler.parse(internal);var b=Reconciler.parse(provider);
  String id;try{id=HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest((internal+"\u0000"+provider).getBytes(StandardCharsets.UTF_8)));}catch(NoSuchAlgorithmException e){throw new IllegalStateException(e);}
  var existing=repo.findById(id);if(existing.isPresent())return existing.get();
  var differences=Reconciler.compare(a,b);var run=new ReconciliationRun(id,a.size(),b.size(),differences.size(),JsonMapper.builder().build().writeValueAsString(differences));
  try{return tx.execute(status->{em.persist(run);em.flush();return run;});}catch(RuntimeException e){return repo.findById(id).orElseThrow(()->e);}
 }
}
