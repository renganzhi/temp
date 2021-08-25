package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.criteria.Criteria;
import com.uxsino.commons.utils.StringUtils;
import com.uxsino.commons.utils.TimeUtils;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 前端查询条件处理工具类
 *
 */
public class WhereSqlUtils {

	/**
	 * 获取查询条件 返回 and 。。。。。。 这个接口尽量不要使用，针对某些条件检索数据不正确
	 * @param queryCond  查询条件，建值对
	 * @param isHumpToUnderline  是否 驼峰命名转下划线命名
	 * @param params 参数集合
	 * @param attrPrefix  表字段前缀，可为空。无需带点.
	 * @return
	 */
	public static String getCondtionSql(List<QueryCond> queryCond,boolean isHumpToUnderline,Map<String, Object> params,String attrPrefix) {
		if(queryCond==null || queryCond.size()==0) {
			return "";
		}
		StringBuffer sb =new StringBuffer();
		for(QueryCond obj:queryCond) {
			//没有值的条件忽略掉
			Object v = obj.getFieldValue();
			if (null ==v || v.toString().trim().equals(""))
				continue;
			String keyName=obj.getFieldKey();
			if(isHumpToUnderline) {
				keyName=humpToUnderline(keyName);
			}
			if (StringUtils.isStrictNotEmpty(attrPrefix)) {
				keyName=attrPrefix+"."+keyName;
			}
			String cond=getQueryJudgeConditon(obj.getCond(),keyName,obj.getFieldKey(),true);
			if(cond==null || cond.equals("")) {
				continue;
			}
			sb.append(" and " + cond);

			params.put(obj.getFieldKey(), (obj.getFieldValue() instanceof  String  && obj.getCond().equals("contains"))? Criteria.escapeLike(obj.getFieldValue().toString()):obj.getFieldValue());
		}

		return sb.toString();
	}

	/**
	 * 根据给的条件和判断拼装sql ,如果传入的字段不在类定义定义的返回内将忽略条件
	 * @param clazz
	 * @param queryCond
	 * @param isHumpToUnderline
	 * @param attrPrefix
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getCondtionSql(Class clazz,List<QueryCond> queryCond,boolean isHumpToUnderline,String attrPrefix,
										Map<String,Object> params) {
		if(queryCond==null || queryCond.size()==0) {
			return "";
		}
		StringBuffer sb =new StringBuffer();
		for(int i=0;i<queryCond.size();i++) {
			QueryCond obj=queryCond.get(i);
			//没有值的条件忽略掉
			Object v = obj.getFieldValue();
			if (null ==v || v.toString().trim().equals(""))
				continue;
			String keyName=obj.getFieldKey();
			if(isHumpToUnderline) {
				keyName=humpToUnderline(keyName);
			}
			if (StringUtils.isStrictNotEmpty(attrPrefix)) {
				keyName=attrPrefix+"."+keyName;
			}
			String paramKey=obj.getFieldKey()+i;
			String cond=getQueryJudgeConditon(obj.getCond(),keyName,paramKey,true);
			if(cond==null || cond.equals("")) {
				continue;
			}
			//根据字段属性处理条件
			try {
				Field field = clazz.getDeclaredField(obj.getFieldKey());				
				if(cond.contains("in :")) {
					List<Object> d=(List<Object>) v;
					d.remove(null);
					if(d.size()==0) {
						continue;
					}
					if(field.getType().isEnum()){
						List<Object> enumList=new LinkedList<Object>();
						if(isHumpToUnderline) {
							for(Object o:d) {
								enumList.add(EnumUtils.getEnum((Class<Enum>)field.getType(),o.toString()).toString());
							}
							
						}else {
							for(Object o:d) {
								enumList.add(EnumUtils.getEnum((Class<Enum>)field.getType(),o.toString()));
							}
						}
						params.put(paramKey,enumList);
					}else {
						params.put(paramKey,d);
					}
				}else {
					//是否枚举类型
					if(field.getType().isEnum()){
						if(isHumpToUnderline) {
							params.put(paramKey, EnumUtils.getEnum((Class<Enum>)field.getType(),obj.getFieldValue().toString()).toString());
						}else {
							params.put(paramKey, EnumUtils.getEnum((Class<Enum>)field.getType(),obj.getFieldValue().toString()));
						}
						
					}else if(field.getType().isInstance(new Long(2L))) {
						params.put(paramKey,  Long.parseLong(v.toString()));
					} else if(field.getType().isInstance(new Date())){
//						List<Object> d=(List<Object>) v;
//						params.put(obj.getFieldKey(), TimeUtils.stringToDate(d.get(0).toString(),"yyyy-MM-dd"));
						params.put(paramKey, TimeUtils.stringToDate(v.toString(),"yyyy-MM-dd HH:mm:ss"));
					}else if(obj.getFieldValue() instanceof  String  && obj.getCond().equals("contains")){
						params.put(paramKey, Criteria.escapeLike(obj.getFieldValue().toString()));
					}else{
						params.put(paramKey, v);
					}
				}

				sb.append(" and "+cond);
			} catch (NoSuchFieldException e) {
				//字段不存在，忽略条件
				continue;
			}
		}
		
		return sb.toString();
	}


	/**
	 * 驼峰命名转下划线命名
	 * @param fieldName
	 * @return
	 */
	public static String humpToUnderline(String fieldName) {
		Pattern humpPattern = Pattern.compile("[A-Z]");
		Matcher matcher = humpPattern.matcher(fieldName);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	/**
	 * 创建排序字符串
	 *
	 * @param sort           排序结构
	 * @param isHumpToUnderline 是否 驼峰命名转下划线命名
	 * @param attrPrefix     属性名前缀,无需带点.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String createSortStr(Map<String, Map<String, Object>> sort, Boolean isHumpToUnderline, String attrPrefix) {
		if (sort==null || sort.isEmpty()) {
			return "";
		}
		StringBuilder sortStr = new StringBuilder();
		Map<String, Object>[] attrList = new Map[sort.size()];
		int num=0;
		for (String attrName : sort.keySet()) {
			Map<String, Object> attr = sort.get(attrName);
			Map<String, Object> res = new HashMap<String, Object>();
			if (isHumpToUnderline) {
				attrName = Criteria.humpToUnderline(attrName);
			}
			if (StringUtils.isStrictNotEmpty(attrPrefix)) {
				attrName = attrPrefix +"."+ attrName;
			}

			res.put("attrName", attrName);
			res.put("flag", attr.get("flag"));
			if(attr.get("order")!=null) {
				attrList[(int) attr.get("order") - 1] = res;
			}else {
				attrList[num]=res;
			}	
			num++;
		}
		for (Map<String, Object> r : attrList) {
			int sortFlag = Integer.valueOf(r.get("flag").toString());
			if (sortFlag == 1) {
				sortStr.append(r.get("attrName") + " asc, ");
			} else if (sortFlag == -1) {
				sortStr.append(r.get("attrName") + " desc, ");
			}
		}
		
        if (sortStr.length() > 0)
            return " order by "+sortStr.substring(0, sortStr.lastIndexOf(","));
        else
            return "";
	}
	
	
	/**
	 * 创建排序字符串,用于jsonb字段类型
	 *
	 * @param sort           排序结构
	 * @param isHumpToUnderline 是否 驼峰命名转下划线命名
	 * @param attrPrefix     属性名前缀,无需带点.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String createSortByJsonb(Map<String, Map<String, Object>> sort, Boolean isHumpToUnderline, String attrPrefix,String fieldName) {
		if (sort==null || sort.isEmpty()) {
			return "";
		}
		StringBuilder sortStr = new StringBuilder();
		Map<String, Object>[] attrList = new Map[sort.size()];
		int num=0;
		for (String attrName : sort.keySet()) {
			Map<String, Object> attr = sort.get(attrName);
			Map<String, Object> res = new HashMap<String, Object>();
			if (isHumpToUnderline) {
				attrName = Criteria.humpToUnderline(attrName);
			}
			if (StringUtils.isStrictNotEmpty(attrPrefix)) {
				attrName = attrPrefix +"."+ fieldName+"->>'"+attrName+"'";
			}
			res.put("attrName", attrName);
			res.put("flag", attr.get("flag"));
			if(attr.get("order")!=null) {
				attrList[(int) attr.get("order") - 1] = res;
			}else {
				attrList[num]=res;
			}	
			num++;
		}
		for (Map<String, Object> r : attrList) {
			int sortFlag = Integer.valueOf(r.get("flag").toString());
			if (sortFlag == 1) {
				sortStr.append(r.get("attrName") + " asc, ");
			} else if (sortFlag == -1) {
				sortStr.append(r.get("attrName") + " desc, ");
			}
		}
		
        if (sortStr.length() > 0)
            return " order by "+sortStr.substring(0, sortStr.lastIndexOf(","));
        else
            return "";
	}
	
	/**
	 * 获取字段条件，主要针对jsonObject字段的处理
	 * @param fieldAttrObj
	 * @param cond
	 * @param value
	 * @return
	 */
	public static String getFieldCondtion(AssetAttr fieldAttrObj,String cond,String value,String attrPrefix) {
		if(fieldAttrObj==null) {
			return "";
		}
		String keyName="properties";  //暂时写死
		if (StringUtils.isStrictNotEmpty(attrPrefix)) {
			keyName=attrPrefix+"."+keyName;
		}
		String fieldId=fieldAttrObj.getId();
		JSONObject fieldDefinition=fieldAttrObj.getFieldDefinition();
		String fromat=null;
		if(fieldDefinition!=null && fieldDefinition.get("format")!=null) {
			fromat=fieldDefinition.getString("format");
		}
		StringBuffer sql=new StringBuffer();
		sql.append(" and ");
		FieldType fieldType = FieldType.getFieldType(fieldAttrObj.getFieldType());
		String judge="";
		switch (fieldType) {
			case Options:				
				if (!StringUtils.isEmpty(fromat)) {
					if ("cascade".equals(fromat)
							|| "multiple".equals(fromat)
							|| "checkbox".equals(fromat)){
						//选项
						value=value.replace("\"", "'");
						sql.append(keyName+" -> '"+fieldId+"'"+getQueryJudgeConditon(cond)+"array"+value);
						break;
					}
				}
				judge=getQueryJudgeConditon(cond);
				if(judge.trim().equals("like") || judge.trim().equals("not like")) {
					sql.append(keyName+"->>'"+fieldId+"'" + judge+ " '%"+value+ "%' ");
				}else {
					sql.append(keyName+"->>'"+fieldId+"'" + judge+ "'"+value+ "' ");
				}
				break;

			case Text:
				if (!StringUtils.isEmpty(fromat)) {
					if ("float".equals(fromat)) {
						sql.append("to_number(0 || ( "+keyName+"->>'" +fieldId+"'),'999999999999999.9999')"+ getQueryJudgeConditon(cond)+ ""+Double.valueOf(value));
						break;
					}else if("integer".equals(fromat)) {
						sql.append("cast(0 || ( "+keyName+"->>'" +fieldId+"') as integer)"+ getQueryJudgeConditon(cond)+ ""+Integer.parseInt(value));
						break;
					}
				}
				judge=getQueryJudgeConditon(cond);
				if(judge.trim().equals("like") || judge.trim().equals("not like")) {
					sql.append(keyName+"->>'"+fieldId+"'" + judge+ " '%"+value+ "%' ");
				}else {
					sql.append(keyName+"->>'"+fieldId+"'" + judge+ "'"+value+ "' ");
				}
				break;
//			case Integer:
//				//单独处理
//				//暂时用integer，实际因该不会超过20yi
//				sql.append("cast(0 || ( info->>'" +fieldKey+"') as integer)"+ getQueryJudgeConditon(cond)+ ""+Integer.parseInt(value)+ " ) ");
//				break;
//			case Float:
//				//单独处理
//				//增加0 || 防止字段值为空
//				sql.append("to_number(0 || ( info->>'" +fieldKey+"'),'999999999999999.9999')"+ getQueryJudgeConditon(cond)+ ""+Double.valueOf(value)+ " ) ");
//				break;
			default:
				judge=getQueryJudgeConditon(cond);
				if(judge.trim().equals("like") || judge.trim().equals("not like")) {
					sql.append(keyName+"->>'"+fieldId+"'" + judge+ " '%"+value+ "%' ");
				}else {
					sql.append(keyName+"->>'"+fieldId+"'" + judge+ "'"+value+ "' ");
				}
		}
		return sql.toString();
	}
	
	
//	/**
//	 * 获取执行sql查询参数的判断符号
//	 * @param conditon
//	 * @return
//	 */
//	public static String getQueryJudgeConditon(QueryCond queryCondObj,String paramKey,boolean isUpper,Map<String,Object> params) {
//		String cond=null;
//		cond=getQueryJudgeConditon(queryCondObj.getCond(),queryCondObj.getFieldKey(),paramKey,isUpper);
//		if(cond==null || cond.equals("")) {
//			return "";
//		}
//		params.put(paramKey, queryCondObj.getFieldValue());
//		return " and "+cond;
//	}
//	
	
	/**
	 * 获取执行sql查询参数的判断符号
	 * @return
	 */
	public static String getQueryJudgeConditon(String cond,String keyName,String paramKey,boolean isUpper) { //
		String defaultJudgeCondition=null;
		CompareCondEnum compareCondEnum = CompareCondEnum.getCompareCondEnum(cond);
		if(compareCondEnum==null) {
			return "";
		}
		switch (compareCondEnum) {
			case EQ:
				defaultJudgeCondition=keyName + " = :" + paramKey;
				break;
			case GT:
				defaultJudgeCondition=keyName + " > :" + paramKey;
				break;
			case LT:
				defaultJudgeCondition=keyName + " < :" + paramKey;
				break;
			case Contains:
				if(isUpper) {
					defaultJudgeCondition="upper("+keyName + ") like upper( :" +paramKey+")";
				}else {
					defaultJudgeCondition=keyName + " like :" +paramKey;
				}
				break;
			case GE:
				defaultJudgeCondition=keyName + " >= :" + paramKey;
				break;
			case LE:
				defaultJudgeCondition=keyName + " <= :" + paramKey;
			case Between:
				defaultJudgeCondition= keyName + " BETWEEN :" + paramKey+" and :"+paramKey+"1";
				break;
			case IN:
				defaultJudgeCondition=keyName + " in :" + paramKey;
				break;
			default:
				defaultJudgeCondition=keyName + " = :" + paramKey;
				break;
		}
		return defaultJudgeCondition;
	}
	
	
	/**
	 * 根据条件转换成sql的判断
	 * @param middle 中间条件
	 * @return
	 */
	private static String getQueryJudgeConditon(String middle) {
		String defaultJudgeCondition="=";
		CompareCondEnum compareCondEnum = CompareCondEnum.getCompareCondEnum(middle);
		switch (compareCondEnum) {
			case EQ:
				defaultJudgeCondition=" = ";
				break;
			case NotEQ:
				defaultJudgeCondition=" <> ";
				break;
			case GT:
				defaultJudgeCondition=" > ";
				break;
			case LT:
				defaultJudgeCondition=" < ";
				break;
			case GE:
				defaultJudgeCondition=" >= ";
				break;
			case LE:
				defaultJudgeCondition=" <= ";
				break;
			case Contains:
				defaultJudgeCondition=" like ";
				break;
			case NotContains:
				defaultJudgeCondition=" not like ";
				break;
			case AllContains:
				defaultJudgeCondition=" ?& ";
				break;
			case AnyOne:
				defaultJudgeCondition=" ?| ";
				break;
			default:
				break;
		}

		return defaultJudgeCondition;
	}

	
}
