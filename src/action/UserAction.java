package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

import dao.UserDao;
import entity.Bid;
import entity.Book;

public class UserAction extends ActionSupport {
	private JSONObject resultObj;// 鐟曚浇绻戦崶鐐插煂妞ょ敻娼伴惃鍑ON閺佺増宓侀敍灞肩鐎规俺顩﹂張濉痚tter,setter閺傝纭堕妴锟� 
	  
    public JSONObject getResultObj() {  
        return resultObj;  
    }  

    public void setResultObj(JSONObject resultObj) {  
        this.resultObj = resultObj;  
    }  

	public String getBook() {
		System.out.println("getBook...");

        HttpServletResponse resp = ServletActionContext.getResponse();  
        resp.setContentType("application/json");  
        ArrayList al = new ArrayList(); 
        
        UserDao dao = new UserDao();
        ArrayList<Book> books = dao.getBook();  
        for (Book b : books) {  
            Map<String, Object> m = new HashMap<String, Object>();   
            m.put("id", b.getId());
            m.put("name", b.getName());  
            m.put("desc", b.getDesc());
            m.put("startingPrice", b.getStartingPrice());
            m.put("startTime", (b.getStartTime()).toString());
            m.put("endTime", (b.getEndTime()).toString());
            m.put("minIncre", b.getMinIncre());
            m.put("highestBid", b.getHighestBid());
            al.add(m); 
        }  
        Map<String, Object> json = new HashMap<String, Object>();  
        json.put("total", books.size());// total闁匡拷鐎涙ɑ鏂侀幀鏄忣唶瑜版洘鏆�  
        json.put("rows", al);// rows闁匡拷鐎涙ɑ鏂佸В蹇涖�夌拋鏉跨秿 list閿涘苯绻�妞ょ粯妲搁垾娓瀘ws閳ユ繂鍙ч柨顔跨槤  
        resultObj = JSONObject.fromObject(json);// 閺嶇厧绱￠崠鏉沞sult娑擄拷鐣剧憰浣规ЦJSONObject  
          
        return SUCCESS;  
    }

    public String placeBid() {  
    	HttpServletRequest req = ServletActionContext.getRequest();
    	String name = req.getParameter("name");
    	String bid = req.getParameter("bid");
    	String id = req.getParameter("id");

    	//SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Timestamp d = new Timestamp(System.currentTimeMillis());
    	//String time = f.format(d);

    	UserDao dao = new UserDao();
    	int idx = Integer.parseInt(id);
    	double bidVal = Double.parseDouble(bid);
    	Book b = dao.getBookById(idx);
    	Timestamp st = b.getStartTime();
    	Timestamp et = b.getEndTime();
    	double mi = b.getMinIncre();
    	double hb = b.getHighestBid();

    	/*Date sd = null;
    	Date ed = null;

    	try {
            sd = f.parse(st);
            ed = f.parse(et);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

    	Map<String, Object> json = new HashMap<String, Object>();  
        
    	if (bidVal < hb+mi)
    		json.put("msg", "Your bid must be at least "+mi+"higher than the highest bid.");
    	else if (d.before(st)||d.after(et))
    		json.put("msg", "Watch the start and end time!");
    	else{
    		int c = dao.addBid(idx, name, bidVal, d);
	    	if (c > 0)
	    		json.put("success", true);
	    	else
	    		json.put("msg", "oops");
    	}
    	resultObj = JSONObject.fromObject(json);// 閺嶇厧绱￠崠鏉沞sult娑擄拷鐣剧憰浣规ЦJSONObject  
    	
        return SUCCESS;
    }
}
