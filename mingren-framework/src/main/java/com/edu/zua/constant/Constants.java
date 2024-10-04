package com.edu.zua.constant;

public class Constants {
    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 分页的时候，每页的数据条数
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 分类状态   0 正常， 1 禁用
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 审核通过的友链
     */
    public static final String LINK_STATUS_NORMAL = "0";

    public static final Object ROOT_COMMENT = -1;
    /**
     * 评论的类型：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论的类型：友链评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * redis中的文章浏览量
     */
    public static final String REDIS_VIEW_COUNT_KEY = "article:viewCount";

    public static final String MENU = "C";
    public static final String BUTTON = "F";

    public static final String REDIS_BACKEND_LOGIN_KEY = "login:";
    public static final String  ADMIN = "1";
}
