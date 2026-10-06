package dev.portfolio.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ReconciliationRun {
  @Id public String id;
  public Instant createdAt;
  public int internalRows;
  public int providerRows;
  public int discrepancyCount;

  @Column(length = 500000)
  public String reportJson;

  protected ReconciliationRun() {}

  public ReconciliationRun(
      String id, int internalRows, int providerRows, int discrepancyCount, String json) {
    this.id = id;
    this.internalRows = internalRows;
    this.providerRows = providerRows;
    this.discrepancyCount = discrepancyCount;
    reportJson = json;
    createdAt = Instant.now();
  }
}
