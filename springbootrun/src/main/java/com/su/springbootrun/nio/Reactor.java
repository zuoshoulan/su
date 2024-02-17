package com.su.springbootrun.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;

    Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT); // case 1
//        SelectionKey sk = serverSocket.register(selector, 0); //case 2
        serverSocket.socket().bind(new InetSocketAddress(port));
        sk.attach(new Acceptor());




    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                int select = selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey) it.next());
                }
                System.out.println();
                selected.clear();
            }

        } catch (IOException e) {

        }

    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) k.attachment();
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Acceptor thread: " + Thread.currentThread().getName());
                SocketChannel c = serverSocket.accept();
                if (c != null) {
                    new Handler(selector, c);
                }
            } catch (IOException e) {

            }
        }
    }

    final class Handler implements Runnable {
        final SocketChannel socket;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(50);
        ByteBuffer output = ByteBuffer.allocate(50);

        static final int READING = 0, SENDING = 1;
        int state = READING;

        Handler(Selector sel, SocketChannel c) throws IOException {
            socket = c;
            c.configureBlocking(false);
            //Optional try first read now
            sk = socket.register(sel, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }

        boolean inputIsComplete() {
            return false;
        }

        boolean outputIsComplete() {
            return false;
        }

        void process() {

        }

        @Override
        public void run() {
            try {
                System.out.println("read write thread: " + Thread.currentThread().getName());
                if (state == READING) {
                    read();
                }
            } catch (IOException e) {

            }
        }

        void read() throws IOException {
            socket.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                //Normally also do first write now
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void write() throws IOException {
            socket.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(9999);
        new Thread(reactor).start();

    }
}
