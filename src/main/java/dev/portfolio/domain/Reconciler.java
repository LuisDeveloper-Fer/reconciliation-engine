package dev.portfolio.domain;

import java.math.BigDecimal;
import java.util.*;

public final class Reconciler {
  private Reconciler() {}

  public record Row(String reference, BigDecimal amount, String currency) {}

  public record Difference(String reference, String rule, String explanation) {}

  public static List<Row> parse(String csv) {
    if (csv == null || csv.length() > 200000)
      throw new IllegalArgumentException("CSV exceeds 200 KB limit");
    var lines = csv.strip().split("\\R");
    if (lines.length == 0 || !lines[0].equals("reference,amount,currency"))
      throw new IllegalArgumentException("Expected header reference,amount,currency");
    if (lines.length > 1001) throw new IllegalArgumentException("Maximum 1000 rows per report");
    var rows = new ArrayList<Row>();
    for (int i = 1; i < lines.length; i++) {
      var fields = lines[i].split(",", -1);
      if (fields.length != 3
          || !fields[0].matches("[A-Za-z0-9_-]{1,64}")
          || !fields[1].matches("[0-9]{1,9}([.][0-9]{1,2})?")
          || !fields[2].matches("PEN|USD|EUR"))
        throw new IllegalArgumentException("Invalid CSV row " + (i + 1));
      rows.add(new Row(fields[0], new BigDecimal(fields[1]), fields[2]));
    }
    return rows;
  }

  public static List<Difference> compare(List<Row> internal, List<Row> provider) {
    var left = group(internal);
    var right = group(provider);
    var keys = new TreeSet<String>();
    keys.addAll(left.keySet());
    keys.addAll(right.keySet());
    var result = new ArrayList<Difference>();
    for (var key : keys) {
      var a = left.getOrDefault(key, List.of());
      var b = right.getOrDefault(key, List.of());
      if (a.size() > 1 || b.size() > 1) {
        result.add(
            new Difference(
                key,
                "DUPLICATE",
                "Internal count="
                    + a.size()
                    + ", provider count="
                    + b.size()
                    + "; ambiguous match"));
        continue;
      }
      if (a.isEmpty()) {
        result.add(new Difference(key, "MISSING_INTERNAL", "Provider reference absent internally"));
        continue;
      }
      if (b.isEmpty()) {
        result.add(
            new Difference(key, "MISSING_PROVIDER", "Internal reference absent from provider"));
        continue;
      }
      if (!a.getFirst().currency().equals(b.getFirst().currency()))
        result.add(
            new Difference(key, "CURRENCY_MISMATCH", "Currencies differ; no conversion applied"));
      else if (a.getFirst().amount().compareTo(b.getFirst().amount()) != 0)
        result.add(
            new Difference(
                key,
                "AMOUNT_MISMATCH",
                "Internal=" + a.getFirst().amount() + ", provider=" + b.getFirst().amount()));
    }
    return List.copyOf(result);
  }

  private static Map<String, List<Row>> group(List<Row> rows) {
    var map = new HashMap<String, List<Row>>();
    rows.forEach(r -> map.computeIfAbsent(r.reference(), k -> new ArrayList<>()).add(r));
    return map;
  }
}
