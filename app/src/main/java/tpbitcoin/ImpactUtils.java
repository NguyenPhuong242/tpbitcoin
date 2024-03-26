package tpbitcoin;

import java.math.BigInteger;

public class ImpactUtils {

    public static final int AVERAGE_BLOCK_TIME_SECONDS = 600;

    /**
     * computes the expected time (in seconds) for mining a block
     * @param hashrate: miner hashing capacity (hash/s)
     * @param difficultyAsInteger: block difficulty, as BigInteger (256 bits integer)
     * @return expected time in seconds before finding a correct block
     */
    // TODO
    public static long expectedMiningTime(long hashrate, BigInteger difficultyAsInteger){
        difficultyAsInteger = difficultyAsInteger.divide(BigInteger.valueOf(hashrate));
        return difficultyAsInteger.longValue();
    }

    /**
     * Compute the total hashrate of the network given current difficulty level
     * @param difficultyAsInteger: difficulty level as 256bits integer
     * @return hashrate of the network in GH/s
     */
    // TODO
    public static long  globalHashRate(BigInteger difficultyAsInteger){
        difficultyAsInteger = difficultyAsInteger.multiply(BigInteger.valueOf(2).pow(32));
        return difficultyAsInteger.longValue();
    }

    /**
     * Compute the total energy consumption of the network
     * assuming each miner has the same hashrate, and consume the same power
     * @param minerHashrate: the hashrate of each miner, in GH/s
     * @param minerPower: the power consumption of each miner, in Watts
     * @param networkHashrate : the global hashrate of the network
     * @param duration : in second
     * @return energy consumed during duration, in kWh
     */
    // TODO
    public static long globalEnergyConsumption(long minerHashrate, long minerPower, long networkHashrate, long duration){
        long energyConsumed = (minerPower * networkHashrate * duration) / (minerHashrate * 3600);
        return energyConsumed;
    }

    private static double calculateMiningProbability(BigInteger difficultyAsInteger) {
        BigInteger maxHashes = BigInteger.valueOf(2).pow(256);
        return difficultyAsInteger.doubleValue() / maxHashes.doubleValue();
    }

    public static double calculateNetworkHashrate(BigInteger difficultyAsInteger) {
        BigInteger hashrate = BigInteger.valueOf(2).pow(256).divide(difficultyAsInteger.multiply(BigInteger.valueOf(AVERAGE_BLOCK_TIME_SECONDS)));
        return hashrate.doubleValue();
    }

    public static long calculateEnergyConsumptionLast24h(long c, long p) {
        long powerInKW = (long) (p / 1000.0);
        long hashPerDay = c * 1_000_000_000 * 24 * 60 * 60;
        return hashPerDay * powerInKW / 1_000;
    }


}
