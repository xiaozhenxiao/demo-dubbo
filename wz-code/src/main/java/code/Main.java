package code;

public class Main {
    static Object lock = new Object();
    static int counter = 0;

    public static void main(String[] args) {
        Producer producer = new Producer();
        Producer producer1 = new Producer();
        Producer producer2 = new Producer();
        Consumer consumer = new Consumer();
        Consumer consumer1 = new Consumer();
        Consumer consumer2 = new Consumer();
        producer.start();
        producer1.start();
        producer2.start();
        consumer.start();
        consumer1.start();
        consumer2.start();
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (counter == 100) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    counter++;
                    System.out.println("Producer counter ++");
                    lock.notifyAll();
                }
            }
        }
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (counter == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    counter--;
                    System.out.println("Consumer counter --");
                    lock.notifyAll();
                }
            }
        }
    }

    public static Node revert(Node head) {
        if (head == null) {
            return head;
        }

        Node pre = null;
        Node next;
        while (head.next != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }

        head.next = pre;

        return head;
    }
}


class Node {
    int val;
    Node next;
}


