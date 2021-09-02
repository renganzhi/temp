package com.uxsino.leaderview.model.asset;

import com.google.common.collect.Lists;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.db.repository.ICustomRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务请求类条件查询，主要用于分页请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Criteria<T> extends PageModel{

    private static final Log LOG = LogFactory.getLog(Criteria.class);
    /**
     * 排序
     */
    @ApiModelProperty(value="字段排序",required=false)
    public Map<String, Map<String, Object>> sort;
    /**
     * 固定条件
     */
    @ApiModelProperty(value="固定查询条件",required=false)
    public ArrayList<QueryCond> fixQuery;

    private Class<T> cls;


    public static String escapeLike(String likeKeyWord) {
        if (likeKeyWord == null) {
            return "%%";
        } else {
            return "%" + escape(likeKeyWord)+"%" ;
        }
    }

    public static String escape(String likeKeyWord) {
        if (likeKeyWord == null) {
            return "";
        } else {
            return (likeKeyWord.replaceAll("_", "\\\\_").replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]")
                    .replaceAll("\\^", "\\\\^").replaceAll("%", "\\\\%"));
        }
    }

    /**
     * 创建额外的附加属性,如删除，启用标识
     * @return
     */
    public String createExtQuery() {
        return "";
    }

    public Map<String, Object> param(Boolean isHumpToUnderline) {
        Map<String, Object> params=new HashMap<String, Object>();
        if(this.fixQuery!=null && this.fixQuery.size()>0) {
            for(QueryCond obj:this.fixQuery) {
                //没有查询值的条件忽略
                Object v = obj.getFieldValue();
                if (null ==v || v.toString().equals(""))
                    continue;
                if(isHumpToUnderline) {
                    params.put(WhereSqlUtils.humpToUnderline(obj.getFieldKey()), obj.getCond().equals("contains")?Criteria.escapeLike(obj.getFieldValue().toString()):obj.getFieldValue());
                }else { //jpql
                         //处理枚举
                    try {
                        Field field = cls.getDeclaredField(obj.getFieldKey());
                        //是否枚举类型
                        if(field.getType().isEnum()){
                            params.put(obj.getFieldKey(), EnumUtils.getEnum((Class<Enum>)field.getType(),obj.getFieldValue().toString()));
                        }else{
                            params.put(obj.getFieldKey(), obj.getCond().equals("contains")?Criteria.escapeLike(obj.getFieldValue().toString()):obj.getFieldValue());
                        }

                    } catch (NoSuchFieldException e) {
                          //字段不存在，忽略条件
                         continue;
                    }
                }
            }
        }
        return params;
    }


    public Criteria(Class<T> cls) {
        this.cls = cls;
    }

    public String createQuery(String className,boolean isHumpToUnderline) {
        String jpql="";
        jpql="from "+className +" where 1=1 ";
        jpql=jpql+createExtQuery()+WhereSqlUtils.getCondtionSql(this.fixQuery, isHumpToUnderline, new HashMap<>(),null);
        return jpql;
    }


    public Long count(ICustomRepository<?, ?> dao,String className,boolean nativeQuery,boolean isHumpToUnderline) {
        Long count = 0L;
        if (nativeQuery) {
            count = dao.countBySql("select count(*) " + createQuery(className,isHumpToUnderline), this.param(nativeQuery));
        } else {
            count = dao.countByJPQL("select count(*) " + createQuery(className,isHumpToUnderline), this.param(nativeQuery));
        }
        this.setCount(count);
        return this.getCount();
    }

    /**
     * 判断是否有有有效查询条件
     * @param criteria
     * @return 没有有效的查询条件返回False 否则返回True
     */
    public static Boolean predealCond(Criteria criteria){
        ArrayList<QueryCond> queryCond = criteria.getFixQuery();
        if(queryCond!=null && queryCond.size()>0) {
            for(QueryCond obj:queryCond) {
                //没有值的条件忽略掉
                Object v = obj.getFieldValue();
                if (null !=v && !v.toString().equals(""))
                    return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    @SuppressWarnings("unchecked")
    public Criteria<T> doSearch(ICustomRepository<?, ?> dao,String className,boolean nativeQuery,boolean isHumpToUnderline) {
        try {
            Lists.<Runnable>newArrayList(()->{
                this.count(dao,className,nativeQuery,isHumpToUnderline);
            },()->{
                List<T> datas;
                if (nativeQuery) {
                    datas = (List<T>) dao.nativeQuery(this.createQuery(className,isHumpToUnderline), cls, this.param(isHumpToUnderline), this.getCurrentNo(),
                            this.getPageSize());
                } else {
                    datas = (List<T>) dao.findByJPQL(this.createQuery(className,isHumpToUnderline), this, this.param(isHumpToUnderline), cls);
                }

                this.setObject(datas);
            }).parallelStream().forEach(run->run.run());
        } catch (Exception e) {
            LOG.error("Criteria doSearch (...)执行失败,JPQL语句:" + this.createQuery(className,isHumpToUnderline) + "，错误：" + "", e);
        }
        return this;
    }



    /**
     * @param jpql 查询语句
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> getObjectListByJpaSql(ICustomRepository<?, ?> dao,String jpql) {
        return (List<T>) dao.findByJPQL(jpql, this.getCurrentNo(), this.getPageSize());
    }


    public List<T> getObjectListByJpaSql(ICustomRepository<?, ?> dao,String jpql,Map<String,Object> params,Class<T> resultClass) {
        return (List<T>) dao.findByJPQL(jpql,params,resultClass);

    }


    @SuppressWarnings("unchecked")
    public List<T> getObjectListBySql(ICustomRepository<?, ?> dao,Class<T> resultClass,String sql,Map<String,Object> params) {
        return (List<T>) dao.nativeQuery("select * " + sql, resultClass, params, this.getCurrentNo(), this.getPageSize());
    }

//    /**
//     * 统计总数
//     *
//     * @param jpql
//     * @return
//     */
//    public Long countByJPQL(ICustomRepository<?, ?> dao,String jpql) {
//        return dao.countByJPQL(jpql,this.param(false));
//    }

    public Long countByJPQL(ICustomRepository<?, ?> dao,String jpql,Map<String,Object> params) {
        return dao.countByJPQL(jpql,params);
    }


    public PageModel parsePageModel() {
        PageModel pageModel = new PageModel();
        pageModel.setCount(this.getCount());
        pageModel.setCurrentNo(this.getCurrentNo());
        pageModel.setPageSize(this.getPageSize());
        pageModel.setTotalPages(this.getTotalPages());
        pageModel.setObject(this.getObject());
        return pageModel;
    }
}
