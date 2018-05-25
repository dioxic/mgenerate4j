package uk.dioxic;

import uk.dioxic.mgenerate.annotation.Pojo;
import uk.dioxic.mgenerate.annotation.PojoProperty;

@Pojo
public class Balances {
    @PojoProperty Double postTxnBal;
    @PojoProperty Double preTxnBal;
}