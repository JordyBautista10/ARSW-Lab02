package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PrimeFinderThread extends Thread {


    int a, b;

    private List<Integer> primes;

    public PrimeFinderThread(int a, int b, LinkedList<Integer> primes) {
        super();
        this.primes = primes;
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long duration = 5000;

        synchronized (primes) {
            for (int i = a; i < b; i++) {
                if (isPrime(i)) {
                    primes.add(i);
                    System.out.println(i);
                    if (System.currentTimeMillis() - startTime >= duration) {
                        try {
                            primes.wait();
                            Scanner scanner = new Scanner(System.in);
                            scanner.nextLine();
                            //scanner.close();
                            primes.notify();
                            startTime  = System.currentTimeMillis();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n % 2 != 0;
            for (int i = 3; ans && i * i <= n; i += 2) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

}
