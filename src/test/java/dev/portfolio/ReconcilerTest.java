package dev.portfolio;
import dev.portfolio.domain.Reconciler;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
class ReconcilerTest {
 @Test void explainsEveryDiscrepancyAndDoesNotRoundAmounts(){
  var a=Reconciler.parse("reference,amount,currency\nA,10.00,PEN\nB,1,USD\nD,3,EUR\nD,3,EUR\nE,1,PEN");
  var b=Reconciler.parse("reference,amount,currency\nA,10.01,PEN\nC,1,USD\nD,3,EUR\nE,1,USD");
  assertThat(Reconciler.compare(a,b)).extracting(Reconciler.Difference::rule).containsExactly("AMOUNT_MISMATCH","MISSING_PROVIDER","MISSING_INTERNAL","DUPLICATE","CURRENCY_MISMATCH");
 }
 @Test void exactDecimalValueMatches(){assertThat(Reconciler.compare(Reconciler.parse("reference,amount,currency\nA,10.0,PEN"),Reconciler.parse("reference,amount,currency\nA,10.00,PEN"))).isEmpty();}
 @Test void rejectsMalformedReports(){assertThatThrownBy(()->Reconciler.parse("reference,amount,currency\nA,-1,PEN")).isInstanceOf(IllegalArgumentException.class);}
}
