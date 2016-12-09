# Socket之Mina服务器

标签（空格分隔）： Java

---

最近想搞一下后台，所以socket是肯定要看的，之前也看过一点点Mina,感觉还可以，所以现在来做一下，当然是做聊天服务器啦。

到现在发现做软件先想好，把文档写一下还是不错的，这样也挺轻松的。

> 项目地址：https://github.com/tyhjh/Mina

## Mina服务器端关键代码
### 导入jar包：
>* mina-core-2.0.16.jar
>* slf4j-api-1.7.21.jar
>* slf4j-nop-1.7.21.jar
>* org.json.jar

### 开启服务：
```java
NioSocketAcceptor acceptor=new NioSocketAcceptor();
			acceptor.setHandler(new MySeverHandler());
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextLineFactory()));
			////设置服务器空闲状态̬
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
			acceptor.bind(new InetSocketAddress(9897));
			acceptor.getManagedSessions();
```
### 监听消息类
可以自己设定逻辑，重点在于,客户端第一次建立连接的时候，要发送一个设置自己的身份，标识的信息，然后在服务器端接收到，就给每个**IoSession**设置身份，然后就好办了。


> 客户端建立连接的时候发送一个身份给服务器
```java
 @Override  
    public void sessionCreated(IoSession session) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("已建立连接" + session.getRemoteAddress());
        session.write(getJson.getMsg(id, null, null, null));  
    }  
```



> 服务器接收到消息，判断该**IoSession**有没有设置身份，没有就设置，否则就进行消息传送
```java
JSONObject jsonObject=new JSONObject((String)message);
		String from=(String) session.getAttribute("from","xx");
		if(from.equals("xx")){
			from=jsonObject.getString("from");
			if(from!=null){
				session.setAttribute("from",from);
			}
		}else{
			//System.out.println("messageReceived："+(String)message+session.getAttribute("from","xx"));
			//传达消息
			new Msg().send2One(session, jsonObject);
		}
```

> 接收到消息后解析一下，看一下是发给谁的，然后找到所有的**IoSession**，看看是发给哪一个，然后用这个**IoSession**发送出去
记得消息最后面要加换行符**"\n"**

```java
Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		for (IoSession sess : sessions) {
			if (sess.getAttribute("from", "xx").equals(jsonObject.getString("to")))
				sess.write(jsonObject.toString()+"\n");
		}

```



> 服务器端监听消息类，还有几个什么格式转化的还是什么类，也是必须的，就懒得贴了
```java
public class MySeverHandler extends IoHandlerAdapter{

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("exceptionCaught"+cause.getMessage());
	}
	//收到消息
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		JSONObject jsonObject=new JSONObject((String)message);
		String from=(String) session.getAttribute("from","xx");
		if(from.equals("xx")){
			from=jsonObject.getString("from");
			if(from!=null){
				session.setAttribute("from",from);
			}
		}else{
			//System.out.println("messageReceived："+(String)message+session.getAttribute("from","xx"));
			//传达消息
			new Msg().send2One(session, jsonObject);
		}
	}
	//发消息时候
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.close();
		System.out.println("sessionClosed");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated");
	}
	//客户端空闲时候
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		//System.out.println("sessionIdle");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened");
	}

}
```

## 客户端关键代码

> 同样的那几个包导进来，然后就开始连接，连接需要，ip和端口，端口是刚才服务器那里自己设置的，我的是9897，然后连接完了得到了**session**，就可以用来发送消息了。同样有个消息监听类，就不贴了，


```java
//Create TCP/IP connection     
		connector=new NioSocketConnector();     
             
        //创建接受数据的过滤器     
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();     
             
        //设定这个过滤器将一行一行(/r/n)的读取数据     
        chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));     
             
        MinaClientHandler minaClientHandler=new MinaClientHandler();
        minaClientHandler.setId(id);
        		
        //客户端的消息处理器：一个SamplMinaServerHander对象     
        connector.setHandler(minaClientHandler);     
             
        //set connect timeout     
        connector.setConnectTimeout(30);     
             
        //连接到服务器：     
        ConnectFuture cf = connector.connect(new InetSocketAddress(ip,port));     
            
        
        //Wait for the connection attempt to be finished.     
        cf.awaitUninterruptibly();     
        
        session=cf.getSession();
        session.write("msg");
```

这样子搞，自己设计一下，就可以发送消息给指定的人，网上根本找不到，全是我连蒙带猜搞出来的，所以我不知道这方法是不是很好，能用就好。











## 数据库设计

User/用户信息
| 字段 | 数据类型 | 备注 |
| ---- | ---- | ---- |
| u_id | string | 用户id |
| u_name | string | 昵称 |
| u_pwd | string | 登录密码 |
| u_email | string | 邮箱 |
| u_friend | text/json | 好友id组 |
| head_image | string | 头像 |
| signature | string | 签名 |
| u_tags | string | 标签 |

Msg/消息记录
| 字段 | 数据类型 | 备注 |
| ---- | ---- | ---- |
| m_id | string | id |
| m_msg | string/json | 信息内容 |
| m_from | string | 发送者 |
| m_to | string | 接收者 |
| m_date | DATETIME | 发送时间 |
| m_isRead| boolean | 是否接收 |

Group_user/群
| 字段 | 数据类型 | 备注 |
| ---- | ---- | ---- |
| g_id | string | 群id |
| u_id | string | 用户id |


|  |  |  |
