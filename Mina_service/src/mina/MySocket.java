package mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MySocket {
	public static void main(String[] args){
		try {
			NioSocketAcceptor acceptor=new NioSocketAcceptor();
			acceptor.setHandler(new MySeverHandler());
		
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextLineFactory()));
			////设置服务器空闲状态̬
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
			acceptor.bind(new InetSocketAddress(9897));
			acceptor.getManagedSessions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
