package cn.netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));

        while(true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while(iter.hasNext()) {
                SelectionKey key = iter.next();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //1.向客户端发送大量数据
                StringBuilder sb = new StringBuilder();
                for (int i = 0;i < 30000000;i++) {
                    sb.append("a");
                }
                ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                while (buffer.hasRemaining()) {
                    //2.返回值代表实际写入的字节数
                    int write = sc.write(buffer);
                    System.out.println(write);
                }
            }
        }
    }
}