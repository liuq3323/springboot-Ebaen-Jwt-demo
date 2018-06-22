package com.example.controller;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * baseController 公用方法 参数校验 转化 json转化 等
 */
public class BaseController {

    /**
     * Json字符串转JsonNode
     * @param JsonStr
     * @return
     * @throws Exception
     */
    public JsonNode toJsonNode(String JsonStr) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(JsonStr);
        return node;
    }
    /**
     * 校验接口参数
     * @param node
     * @param key
     * @return
     */
    public boolean validArrayParam(JsonNode node, String key) {
        if (node == null
                || !node.has(key)
                || node.get(key) == null
                || !node.get(key).isArray())
            return false;
        return true;
    }

    public boolean validParam(JsonNode node, String[] keys) {
        for (String key : keys) {
            if (!validParam(node, key))
                return false;
        }
        return true;
    }

    public boolean validParam(JsonNode node, String key) {
        if (node == null
                || !node.has(key)
                || node.get(key) == null
                || node.get(key).asText() == null
                || node.get(key).asText().isEmpty())
            return false;
        return true;
    }

    public Date tryAsDate(JsonNode node, String key, SimpleDateFormat sdf) throws ParseException {
        if (node.has(key) && node.get(key) != null)
            return sdf.parse(node.get(key).asText());
        return null;
    }

    public Date tryAsDate(JsonNode node, String key, SimpleDateFormat sdf, Date defaultDate) throws ParseException {
        if (node.has(key) && node.get(key) != null) {
            if (node.get(key).textValue().isEmpty())
                return defaultDate;
            return sdf.parse(node.get(key).asText());
        }
        return null;
    }


    public String tryAsText(JsonNode node, String key) {
        if (node.has(key) && node.get(key) != null)
            return node.get(key).asText();
        return "";
    }

    public Double tryAsDouble(JsonNode node, String key) {
        if (node.has(key) && node.get(key) != null)
            return node.get(key).asDouble();
        return 0d;
    }

    public Long tryAsLong(JsonNode node, String key) {
        if (node.has(key) && key.length() > 0 && node.get(key) != null)
            return node.get(key).asLong();
        return 0L;
    }

    public Integer tryAsInt(JsonNode node, String key) {
        if (node.has(key) && node.get(key) != null)
            return node.get(key).asInt();
        return 0;
    }

    public Boolean tryAsBoolean(JsonNode node, String key) {
        if (node.has(key) && node.get(key) != null)
            return node.get(key).asBoolean();
        return false;
    }

    protected String checkFieldExists(JsonNode node, String[] fields) {
        if (node == null) {
            return "没有参数!";
        }

        StringBuffer missField = new StringBuffer("");
        for (String field : fields) {
            if (!node.has(field)) {
                missField.append(field).append(" ");
            }
        }
        return missField.toString();
    }
}
