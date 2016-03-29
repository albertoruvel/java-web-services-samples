/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.samples.pooling;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author MACARENA
 */
public class SampleThreadPool extends ThreadPoolExecutor{
    private static final int POOL_SIZE = 10; 
    private boolean isPaused; 
    private ReentrantLock pauseLock = new ReentrantLock(); 
    private Condition unpaused = pauseLock.newCondition(); 
    
    public SampleThreadPool(){
        super(POOL_SIZE, //core pool size
              POOL_SIZE, //maximum pool size
              0L, //keep alive time for idle thread
              TimeUnit.SECONDS, //Time unit for keep alive settings
              new LinkedBlockingQueue<Runnable>(POOL_SIZE) //work queue
                ); 
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try{
            while(isPaused)
                unpaused.await();
        }catch(InterruptedException ex){
            t.interrupt();
        }finally{
            pauseLock.unlock();
        }
        
    }
    
    
    public void resume(){
        pauseLock.lock();
        try{
            isPaused = false; 
            unpaused.signalAll();
        }finally{
            pauseLock.unlock();
        }
    }
    
    
}
