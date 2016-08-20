package org.sound.audio.hashextract;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.sound.audio.grouping.Frequency;

public class Hasher {

    public enum HASHER_TYPE {
        LINEAR, BASE_LN_EXPONENT, SQUARED, MD5HASH, CONCAT;
    }

    public interface HasherAlgorithm {
        public long doHash(Frequency... a);
    }

    private static Map<HASHER_TYPE, HasherAlgorithm> HASHERS;

    static {
        init();
    }

    public static long[] createHashes(Frequency[][] keyPoints, HASHER_TYPE hasherType) {
        long[] result = new long[keyPoints.length];
        for (int frame = 0; frame < keyPoints.length; frame++) {
            result[frame] = HASHERS.get(hasherType).doHash(keyPoints[frame]);
        }
        return result;
    }

    private static void init() {
        HASHERS = new HashMap<HASHER_TYPE, HasherAlgorithm>();

        HASHERS.put(HASHER_TYPE.LINEAR, new HasherAlgorithm() {

            @Override
            public long doHash(Frequency... a) {
                long accumulator = 0;
                for (int i = 0; i < a.length; i++) {
                    accumulator += a[i].frequency * Math.pow(1000, i);
                }
                return accumulator;
            }
        });
        HASHERS.put(HASHER_TYPE.BASE_LN_EXPONENT, new HasherAlgorithm() {

            @Override
            public long doHash(Frequency... a) {
                long accumulator = 0;
                for (int i = 0; i < a.length; i++) {
                    accumulator += a[i].frequency * Math.pow(2, 10 * i);
                }
                return accumulator;
            }
        });
        HASHERS.put(HASHER_TYPE.SQUARED, new HasherAlgorithm() {

            @Override
            public long doHash(Frequency... a) {
                long accumulator = 0;
                for (int i = 0; i < a.length; i++) {
                    accumulator += Math.pow(a[i].frequency, 2);
                }
                return accumulator;
            }
        });
        HASHERS.put(HASHER_TYPE.MD5HASH, new HasherAlgorithm() {

            @Override
            public long doHash(Frequency... a) {
                StringBuilder accumulator = new StringBuilder();
                for (int i = 0; i < a.length; i++) {
                    accumulator.append(a[i].frequency);
                }
                return accumulator.hashCode();
            }
        });
        HASHERS.put(HASHER_TYPE.CONCAT, new HasherAlgorithm() {

            @Override
            public long doHash(Frequency... a) {
                StringBuilder accumulator = new StringBuilder();
                for (int i = a.length - 1; i >= 0; i--) {
                    accumulator.append(a[i].frequency);
                }
                return Long.parseUnsignedLong(accumulator.toString());
            }
        });
    }

    public static void main(String[] args) {
        Frequency[][] frequencies = new Frequency[10][];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = new Frequency[5];
            for (int y = 0; y < frequencies[i].length; y++) {
                frequencies[i][y] = new Frequency(((int)(Math.random()*10000))%512, 1);
            }
            System.out.println(Arrays.toString(frequencies[i]));
        }
        for (HASHER_TYPE hasher : HASHER_TYPE.values()) {
            System.out.printf("Hasher %s :\n", hasher);
            System.out.println(Arrays.toString(createHashes(frequencies, hasher)));
        }
    }
}
