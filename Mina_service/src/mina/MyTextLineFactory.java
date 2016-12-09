package mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyTextLineFactory implements ProtocolCodecFactory {
	private MyTextLinCumulativeDecoder mDecoder;
	private MyTextLineEncoder mEncoder;
	
	public MyTextLineFactory(){
		mDecoder=new MyTextLinCumulativeDecoder();
		mEncoder=new MyTextLineEncoder();
	}
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return mDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return mEncoder;
	}

}
