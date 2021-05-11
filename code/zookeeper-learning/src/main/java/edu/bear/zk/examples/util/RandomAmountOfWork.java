package edu.bear.zk.examples.util;

import java.util.Random;

public final class RandomAmountOfWork {

    private final Random random = new Random(System.currentTimeMillis());

    public int timeItWillTake() {
        return 5 + random.nextInt(5);  // sample work takes 5-10 seconds
    }

}
