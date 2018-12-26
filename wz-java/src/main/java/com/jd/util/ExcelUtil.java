package com.jd.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel 导入导出工具类
 * wangzhen23
 * 2018/11/22.
 */
public class ExcelUtil {

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = PoiTransformer.createInitialContext();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);

        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        //设置静默模式，不报警告
        //evaluator.getJexlEngine().setSilent(true);
        //函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("utils", new JxlsUtils());    //添加自定义功能
        evaluator.getJexlEngine().setFunctions(funcs);
        //必须要这个，否者表格函数统计会错乱
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
        File template = getTemplate(templatePath);
        if (template != null) {
            exportExcel(new FileInputStream(template), os, model);
        } else {
            throw new Exception("Excel 模板未找到。");
        }
    }

    //获取jxls模版文件
    public static File getTemplate(String path) {
        File template = new File(path);
        if (template.exists()) {
            return template;
        }
        return null;
    }

    private static void executeGridObjectListDemo(List<String> headers, List<List<Object>> data) throws IOException {
        try (InputStream is = ExcelUtil.class.getResourceAsStream("/review_template.xlsx")) {
            try (OutputStream os = new FileOutputStream("mutil_output.xlsx")) {
                Context context = new Context();
                context.putVar("headers", headers);
                context.putVar("data", data);
                JxlsHelper.getInstance().processTemplateAtCell(is, os, context, "Sheet1!A1");
            }
        }
    }

    public static void main(String[] args) {
        /*List<String> headers = Lists.newArrayList();
        List<List<Object>> dataList = Lists.newArrayList();

        for (int i = 0; i < 18; i++) {
            headers.add("header" + i);
        }

        for (int j = 0; j < 30; j++) {
            List<Object> data = Lists.newArrayList();
            for (int i = 0; i < 18; i++) {
                if(i%5==0){
                    data.add(j);
                }else {
                    data.add("data-" + j + "-" + i);
                }
            }
            dataList.add(data);
        }

        try {
            executeGridObjectListDemo(headers, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            readExcel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }

    public static void readExcel() throws IOException, SAXException, InvalidFormatException {
        InputStream inputXML = new BufferedInputStream(ExcelUtil.class.getResourceAsStream("/read_out.xml"));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
        InputStream inputXLS = new BufferedInputStream(ExcelUtil.class.getResourceAsStream("/mutil_output.xlsx"));
        List<Order> orders = new ArrayList();
        Map beans = new HashMap();
        beans.put("orders", orders);
        XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
        if (readStatus.isStatusOK()) {
            System.out.println(JSON.toJSONString(orders));
        } else {
            System.out.println(readStatus.getReadMessages());
        }
        inputXLS.close();
        inputXML.close();
    }

    public static List<Order> readExcel(InputStream inputXLS) throws IOException, InvalidFormatException {
        InputStream inputXML = new BufferedInputStream(ExcelUtil.class.getResourceAsStream("/read_out.xml"));
        XLSReader mainReader = null;
        List<Order> orders = new ArrayList();
        Map beans = new HashMap();
        beans.put("orders", orders);
        try {
            mainReader = ReaderBuilder.buildFromXML(inputXML);
            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
            if (readStatus.isStatusOK()) {
                System.out.println(JSON.toJSONString(orders));
                return orders;
            } else {
                System.out.println(readStatus.getReadMessages());
            }
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }


}
