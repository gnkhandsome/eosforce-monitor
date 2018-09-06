package client.crypto.model.chain;

import client.crypto.model.types.EosType;
import client.crypto.util.BitUtils;
import client.crypto.util.HexUtils;
import com.google.gson.annotations.Expose;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by swapnibble on 2018-03-19.
 */

public class TransactionFeeHeader implements EosType.Packer {
    //@Expose
    private String expiration;

    //@Expose
    private int ref_block_num = 0; // uint16_t

    //@Expose
    private long ref_block_prefix= 0;// uint32_t

    //@Expose
    private long max_net_usage_words; // fc::unsigned_int

    //@Expose
    private long max_cpu_usage_ms;    // fc::unsigned_int

    //@Expose
    private long delay_sec;     // fc::unsigned_int

    //@Expose
    private String fee;

    public TransactionFeeHeader(){
    }

    public TransactionFeeHeader(TransactionFeeHeader other){
        this.expiration = other.expiration;
        this.ref_block_num = other.ref_block_num;
        this.ref_block_prefix = other.ref_block_prefix;
        this.max_net_usage_words = other.max_net_usage_words;
        this.max_cpu_usage_ms = other.max_cpu_usage_ms;
        this.delay_sec = other.delay_sec;
        this.fee = other.fee;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setReferenceBlock( String refBlockIdAsSha256 ) {
        ref_block_num = new BigInteger( 1, HexUtils.toBytes(refBlockIdAsSha256.substring(0,8))).intValue();

        ref_block_prefix = //new BigInteger( 1, HexUtils.toBytesReversed( refBlockIdAsSha256.substring(16,24))).longValue();
                BitUtils.uint32ToLong(HexUtils.toBytes(refBlockIdAsSha256.substring(16,24)), 0); // BitUtils treats bytes in little endian.
                                                                                                        // so, no need to reverse bytes.
    }

    public void setReferenceBlock( int ref_block_num, long  ref_block_prefix) {
        this.ref_block_num = ref_block_num;

        this.ref_block_prefix = ref_block_prefix;
    }

    public int getRefBlockNum() {
        return ref_block_num;
    }
    public long getRefBlockPrefix() {
        return ref_block_prefix;
    }


    private Date getExpirationAsDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse( dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public void putNetUsageWords(long netUsage) {
        this.max_net_usage_words = netUsage;
    }

    public void putKcpuUsage(long kCpuUsage) {
        this.max_cpu_usage_ms = kCpuUsage;
    }

    @Override
    public void pack(EosType.Writer writer) {
        //System.out.println("expireDate: " + ((int)(getExpirationAsDate(expiration).getTime() / 1000)));
        writer.putIntLE( (int)(getExpirationAsDate(expiration).getTime() / 1000) ); // ms -> sec

        writer.putShortLE( (short)(ref_block_num  & 0xFFFF) );  // uint16
        writer.putIntLE( (int)( ref_block_prefix & 0xFFFFFFFF) );// uint32

        // fc::unsigned_int
        writer.putVariableUInt(max_net_usage_words);
        writer.putVariableUInt( max_cpu_usage_ms);
        writer.putVariableUInt( delay_sec);
    }

    public int getRef_block_num() {
        return ref_block_num;
    }

    public void setRef_block_num(int ref_block_num) {
        this.ref_block_num = ref_block_num;
    }

    public long getRef_block_prefix() {
        return ref_block_prefix;
    }

    public void setRef_block_prefix(long ref_block_prefix) {
        this.ref_block_prefix = ref_block_prefix;
    }

    public long getMax_net_usage_words() {
        return max_net_usage_words;
    }

    public void setMax_net_usage_words(long max_net_usage_words) {
        this.max_net_usage_words = max_net_usage_words;
    }

    public long getMax_cpu_usage_ms() {
        return max_cpu_usage_ms;
    }

    public void setMax_cpu_usage_ms(long max_cpu_usage_ms) {
        this.max_cpu_usage_ms = max_cpu_usage_ms;
    }

    public long getDelay_sec() {
        return delay_sec;
    }

    public void setDelay_sec(long delay_sec) {
        this.delay_sec = delay_sec;
    }
}