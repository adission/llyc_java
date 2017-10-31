package cn.com.lanlyc.base.util;

import java.io.Serializable;
import java.util.List;

/**
 * 分页方法类<br>
 * 拥有索引组，可自定义长度 可判断是否有上一页、下一页 可判断是否有上一组索引、下一组索引
 * @author 
 */
public class Page<T extends Serializable> implements Serializable {
    
    private static final long serialVersionUID = 4110173510080406542L;
    
    /**
     * 每页显示记录数
     */
    protected int pageSize = Integer.parseInt(ConstantUtils.getConstant("page.page-size"));
    
    /**
     * 总页数
     */
    protected int totalPage;
    
    /**
     * 自动计算的当前页页码
     */
    protected int currentPage;
    
    /**
     * 参数传入的当前页页码
     */
    protected int paramCurrentPage;
    
    /**
     * 跳跃到上一组索引的页码
     */
    protected int previousPageIndex;
    
    /**
     * 当前页的索引组
     */
    protected int[] pageIndex;
    
    /**
     * 跳跃到下一组索引的页码
     */
    protected int nextPageIndex;
    
    /**
     * 索引组长度
     */
    protected int pageIndexLength = 10;
    
    /**
     * 总记录数
     */
    protected int totalRow;
    
    /**
     * 当前页在数据库中的起始行
     */
    protected int startRow;
    
    /**
     * 上一页页码
     */
    protected int previousPage;
    
    /**
     * 下一页页码
     */
    protected int nextPage;
    
    /**
     * 当前页需要显示的记录集
     */
    protected List<T> resultRows;
    
    /**
     * 分页类需要附带的对象
     */
    protected Object attach;
    
    /**
     * 使用默认pageSize[根据'page.page-size'属性配置获得]的构造方法
     * @param currentPage
     * @author 
     */
    public Page(int currentPage) {
        this.paramCurrentPage = currentPage;
    }
    
    /**
     * 使用默认pageSize[根据'page.page-size'属性配置获得]的构造方法
     * @param currentPage 当前页码
     * @param totalRow 总记录数
     */
    public Page(int currentPage, int totalRow) {
        this.paramCurrentPage = currentPage;
        this.totalRow = totalRow;
        init();
    }
    
    /**
     * 指定pageSize的构造方法
     * @param pageSize 每页长度
     * @param currentPage 当前页码
     * @param totalRow 总记录数
     */
    public Page(int pageSize, int currentPage, int totalRow) {
        this.pageSize = pageSize;
        this.paramCurrentPage = currentPage;
        this.totalRow = totalRow;
        init();
    }
    
    /**
     * 初始化所有受pageSize、currentPage以及totalRow影响的属性
     */
    public void init() {
        // 验证显示记录数
        pageSize = (pageSize <= 1 ? 1 : pageSize);
        // 初始化总页数
        totalPage = totalRow / pageSize + (totalRow % pageSize == 0 ? 0 : 1);
        totalPage = totalPage == 0 ? 1 : totalPage;
        // 验证当前页码是否超出总页数范围并纠正
        if (paramCurrentPage < 1) {
            currentPage = 1;
        } else if (paramCurrentPage > totalPage) {
            currentPage = totalPage;
        } else {
            currentPage = paramCurrentPage;
        }
        // 初始化当前页在数据库中的起始行和结束行
        startRow = (currentPage - 1) * pageSize;
        // 初始化上页页码
        previousPage = (currentPage == 1 ? 1 : currentPage - 1);
        // 初始化下页页码
        nextPage = (currentPage == totalPage ? totalPage : currentPage + 1);
    }
    
    /**
     * 判断是否存在上一页
     * @return boolean
     */
    public boolean isHasPreviousPage() {
        return currentPage != 1;
    }
    
    /**
     * 判断是否存在下一页
     * @return boolean
     */
    public boolean isHasNextPage() {
        return currentPage != totalPage;
    }
    
    /**
     * 判断当前索引组前是否含有索引 即 若当前索引组为第一组索引，则返回false
     * @return boolean
     */
    public boolean isHasPreviousIndex() {
        return currentPage > pageIndexLength / 2 + 1;
    }
    
    /**
     * 判断当前索引组后是否含有索引 即 若当前索引组为最后一组索引，则返回false
     * @return boolean
     */
    public boolean isHasNextIndex() {
        /*
         * 考虑到pageIndexCount可能为奇数 pageIndexCount/2 不等于 pageIndexCount - pageIndexCount/2 所以必须使用 pageIndexCount - pageIndexCount/2 - 1 而不能使用 pageIndexCount/2 - 1
         */
        return currentPage < totalPage - (pageIndexLength - pageIndexLength / 2 - 1);
    }
    
    public int getNextPage() {
        return nextPage;
    }
    
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    
    public int getPreviousPage() {
        return previousPage;
    }
    
    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * 设置currentPage的同时，调用init()方法重新初始化
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        init();
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    /**
     * 设置pageSize的同时，调用init()方法重新初始化
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        init();
    }
    
    public int getStartRow() {
        return startRow;
    }
    
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
    
    public int getTotalPage() {
        return totalPage;
    }
    
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    
    public int getTotalRow() {
        return totalRow;
    }
    
    /**
     * 设置totalRow的同时，调用init()方法重新初始化
     * @param totalRow
     */
    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
        init();
    }
    
    public List<T> getResultRows() {
        return resultRows;
    }
    
    public void setResultRows(List<T> resultRows) {
        this.resultRows = resultRows;
    }
    
    /**
     * @return 得到当前页需要显示的索引组
     */
    public int[] getPageIndex() {
        if (totalPage <= pageIndexLength) {
            /*
             * 当最大页小于等于索引组长度的时候 则重声索引组长度为总页码长度
             */
            pageIndex = new int[totalPage];
            for (int i = 0; i < totalPage; i++) {
                pageIndex[i] = i + 1;
            }
        } else {
            pageIndex = new int[pageIndexLength];
            int half = pageIndexLength / 2;
            if (currentPage <= half) {
                /*
                 * 当前页小于等于索引组一半长度时 则索引组从第一页开始索引
                 */
                for (int i = 0; i < pageIndex.length; i++) {
                    pageIndex[i] = i + 1;
                }
            } else if (currentPage >= totalPage - half + 1) {
                /*
                 * 当前页接近最大页并且间隔小于索引组一半长度时 则索引组从最大页开始反向往前索引
                 */
                for (int i = pageIndexLength - 1; i >= 0; i--) {
                    pageIndex[i] = totalPage + i + 1 - pageIndexLength;
                }
            } else {
                for (int i = 0; i < pageIndex.length; i++) {
                    pageIndex[i] = currentPage - half + i;
                }
            }
        }
        return pageIndex;
    }
    
    public void setPageIndex(int[] pageIndex) {
        this.pageIndex = pageIndex;
    }
    
    public int getPageIndexLength() {
        return pageIndexLength;
    }
    
    public void setPageIndexLength(int pageIndexLength) {
        this.pageIndexLength = pageIndexLength;
    }
    
    /**
     * @return 获得可以切换到下一组索引的跳转页码
     */
    public int getNextPageIndex() {
        nextPageIndex = getPageIndex()[getPageIndex().length / 2] + pageIndexLength;
        if (nextPageIndex > totalPage) {
            nextPageIndex = totalPage;
        }
        return nextPageIndex;
    }
    
    public void setNextPageIndex(int nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }
    
    /**
     * @return 获得可以切换到上一组索引数组的跳转页码
     */
    public int getPreviousPageIndex() {
        previousPageIndex = getPageIndex()[getPageIndex().length / 2] - pageIndexLength;
        if (previousPageIndex < 1) {
            previousPageIndex = 1;
        }
        return previousPageIndex;
    }
    
    public void setPreviousPageIndex(int previousPageIndex) {
        this.previousPageIndex = previousPageIndex;
    }
    
    public Object getAttach() {
        return attach;
    }
    
    public void setAttach(Object attach) {
        this.attach = attach;
    }
}
