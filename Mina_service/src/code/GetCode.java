package code;

public class GetCode {
	public static String getCode(int code){
		switch (code) {
		case 201:
			return "名字不能为空";
		case 202:
			return "密码不能为空";
		case 203:
			return "邮箱不能为空";
		case 204:
			return "邮箱已被注册";
		case 205:
			
		case 206:
			
		case 207:
			
		default:
			return null;
		}
	}
}
