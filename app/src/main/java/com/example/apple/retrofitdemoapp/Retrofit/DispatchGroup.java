package com.example.apple.retrofitdemoapp.Retrofit;

//HOW TO USE:
//Create new DispatchGroup object
//1. call group.enter() before every ASYNC method u want to wait for
//2. call group.leave() every time u ended work inside ASYNC the method callback
//3. call group.notify() and pass callback that u want to execute after all group methods were finished
public class DispatchGroup {

    private int count = 0;
    private Runnable runnable;

    public DispatchGroup() {
        super();
        count = 0;
    }

    public synchronized void enter() {
        count++;
    }

    public synchronized void leave() {
        count--;
        notifyGroup();
    }

    public void notify(Runnable r) {
        runnable = r;
        notifyGroup();
    }

    private void notifyGroup() {
        if (count <=0 && runnable != null) {
            runnable.run();
        }
    }
}