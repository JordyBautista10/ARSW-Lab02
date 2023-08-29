/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private LinkedList<Integer> primes;
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        this.primes  = new LinkedList<>();

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, primes);
            synchronized (primes) {
                pft[i] = elem;
            }

        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1,primes);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        for(int i = 0;i < NTHREADS;i++ ) {
            try {
                pft[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println(pft[0].getPrimes().size());
        System.out.println(pft[1].getPrimes().size());
        System.out.println(pft[2].getPrimes().size());
        System.out.println("----------------------------------------------------------------");
        System.out.println(pft[0].getPrimes().get(0));
        System.out.println(pft[1].getPrimes().get(1));
        System.out.println(pft[2].getPrimes().get(2));
    }
    
}
