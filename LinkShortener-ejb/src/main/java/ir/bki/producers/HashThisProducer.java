package ir.bki.producers;

import org.hashids.Hashids;

import javax.ejb.Singleton;

@Singleton
public class HashThisProducer {
    public static final String SALT = "d3a19437d39d5fe6103678b58d2a09d70f3b11f12cd1635ad16a19b4a3c13b1a";
    private static final Hashids instance = new Hashids(SALT, 3);

    public Hashids getHashThisInstance() {
        return instance;
    }
}
