package tpbitcoin;

import org.bitcoinj.core.Sha256Hash;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class HashRateEstimator {
    private final int duration; // duration of each experience, in milliseconds
    private final int numberOfTries; // number of experience

    /**
     * Create a new object for estimating the number of SHA256 hashs the host can
     * perform per second
     ** 
     * @param duration:     (milli-seconds) duration of each experiment
     * @param numberOfTries : number of experiments to run
     */
    public HashRateEstimator(int duration, int numberOfTries) {
        this.duration = duration;
        this.numberOfTries = numberOfTries;
    }

    /**
     * @return : return the hashrate (hash/second)
     */
    public double estimate() {
        byte[] bytes;
        MessageDigest md = Sha256Hash.newDigest();

        long totalHashes = 0;
        long totalTime = duration * numberOfTries;

        for(int i = 0; i < numberOfTries;i++){
            bytes = new byte[64];
            long startTime = System.currentTimeMillis();
            long endTime = startTime + duration;

            while(System.currentTimeMillis() < endTime){
                md.update(bytes);
                md.digest();
                totalHashes++;
            }
        }

        return (double)totalHashes / (totalTime / 1000.0);

    }

}
