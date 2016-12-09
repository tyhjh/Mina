package mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextLinCumulativeDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		int starPosition=in.position();
		while(in.hasRemaining()){
			byte b=in.get();
			if(b=='\n'){
				int currentPosition=in.position();
				int limit=in.limit();
				in.position(starPosition);
				in.limit(currentPosition);
				IoBuffer buf=in.slice();
				byte[] dest=new byte[buf.limit()];
				buf.get(dest);
				String str=new String(dest);
				out.write(str);
				in.position(currentPosition);
				in.limit(limit);
				return true;
			}
		}
		in.position(starPosition);
		return false;
	}

}
