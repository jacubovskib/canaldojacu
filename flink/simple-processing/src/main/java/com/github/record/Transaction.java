package com.github.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
      timezone = "UTC"
  )
  public Instant created_dt;
  public Long id;
  public Long customerId;
  public double amount;
  public boolean isSuspicious;

  @Override
  public String toString() {
    return "Transaction{" +
        "created_dt=" + created_dt +
        ", id=" + id +
        ", customerId=" + customerId +
        ", amount=" + amount +
        ", isSuspicious=" + isSuspicious +
        '}';
  }
}
