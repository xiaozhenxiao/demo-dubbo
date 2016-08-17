package java;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class JavaTest {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		  list.add("JavaWeb编程词典");        //向列表中添加数据
		  list.add("Java编程词典");        //向列表中添加数据
		  list.add("C#编程词典");         //向列表中添加数据
		  list.add("ASP.NET编程词典");        //向列表中添加数据
		  list.add("VC编程词典");         //向列表中添加数据
		  list.add("SQL编程词典");        //向列表中添加数据
		  Iterator<String> its = list.iterator();     //获取集合迭代器
		  System.out.println("集合中所有元素对象：");
		  while (its.hasNext()) {        //循环遍历集合
		   System.out.print(its.next() + "  ");     //输出集合内容
		  }
		  List<String> subList = list.subList(3, 5);    //获取子列表
		  System.out.println("\n截取集合中部分元素：");
		  Iterator it = subList.iterator();
		  while (it.hasNext()) {
		   System.out.print(it.next() + "  ");
		  }
		  System.out.println();
		  Integer fenzi = 6;
		  Integer fenmu = 10;
		  System.out.println(Math.ceil(fenzi.doubleValue()/fenmu.doubleValue()));
		  
		  System.out.println("Double转换：" + Double.valueOf("10"));
		  
		  Integer testNull = null;
		try {
			if(testNull == null){
                System.out.println("null");
                if(testNull == 0){
                    System.out.println("0");
                }
            }
		} catch (Exception e) {
			System.out.println("测试空指针");
		}
		Double d1 = 28d;
		Double d2 = 20d;
		Double dresult = (d1-d2)/d2 * 100;
		DecimalFormat df  = new DecimalFormat("###.00");
		System.out.println("double 除法："+dresult);
		System.out.println("double 除法df："+df.format(dresult));

		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss'Z'");
		System.out.println(format.format(new Date()));

		BigDecimal dec = new BigDecimal("23.23422");
		Integer ii = 0;
		double dd = Math.round(dec.doubleValue()*100);
		int rr = ii + (int)dd;
		System.out.println(rr);
		System.out.println(dd);
	}

}
