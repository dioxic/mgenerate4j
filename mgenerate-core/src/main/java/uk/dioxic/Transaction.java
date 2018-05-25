package uk.dioxic;


import uk.dioxic.mgenerate.annotation.Pojo;
import uk.dioxic.mgenerate.annotation.PojoProperty;

import java.time.LocalDateTime;
import java.util.List;

@Pojo
public class Transaction {
    @PojoProperty Long txnSeqNum;
    @PojoProperty Integer accIdr;
    @PojoProperty Integer idr;
    @PojoProperty LocalDateTime postDt;
    @PojoProperty String srcId;
    @PojoProperty String typeId;
    @PojoProperty Integer amtType;
    @PojoProperty Integer chqNo;
    @PojoProperty Integer drAutLmtAmt;
    @PojoProperty List<String> narrative;
    @PojoProperty Integer extNarCt;
    @PojoProperty List<String> extNarrative;
    @PojoProperty String eodInd;
    @PojoProperty(subDoc = true) Balances balances;
    @PojoProperty Integer strEntCde;
    @PojoProperty Long entSeqNum;
    @PojoProperty String merchant;
    @PojoProperty Double txnAmt;
    @PojoProperty LocalDateTime txnDtTim;

}
