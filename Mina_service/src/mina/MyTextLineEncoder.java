package mina;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MyTextLineEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession arg0) throws Exception {
		
	}

	
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		String s=null;
		if(message instanceof String){
			s=(String) message;
		}
		if(s!=null){
			CharsetEncoder charsetEncoder=(CharsetEncoder) session.getAttribute("encoder");
			if(charsetEncoder==null){
				charsetEncoder=Charset.defaultCharset().newEncoder();
				session.setAttribute("encoder", charsetEncoder);
			}
			IoBuffer ioBuffer=IoBuffer.allocate(s.length());
			ioBuffer.setAutoExpand(true);
			ioBuffer.putString(s,charsetEncoder);
			ioBuffer.flip();
			out.write(ioBuffer);
		}
	}

}
