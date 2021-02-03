import ir.bki.linkshortener.util.BaseConversion;
import org.junit.Test;


public class LsTest {

    @Test
    public void testD(){
        BaseConversion baseConversion = new BaseConversion();
        System.err.println(baseConversion.encode(1));
        System.err.println(baseConversion.encode(2));
        System.err.println(baseConversion.encode(10));
        System.err.println(baseConversion.encode(100));
        System.err.println(baseConversion.encode(1000L));
        System.err.println(baseConversion.encode(10005050L));
        System.err.println(baseConversion.decode("P8VW"));
    }
}
