package cn.howardliu.gear.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>created at 16-4-27
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Page<T> {
    private long total;
    private long pageCount;
    private long pageSize;
    private long pageNum;
    private List<T> content = new ArrayList<>();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
