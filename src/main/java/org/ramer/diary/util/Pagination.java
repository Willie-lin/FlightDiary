package org.ramer.diary.util;

import java.util.List;

/**
 * 分页工具类:
 *  使用数组解决取出的用户id不连续问题
 *  将取出的用户数据放入数组中,就可以通过数组的下标直接获取.
 * @author ramer
 *
 */
@SuppressWarnings("unused")
public class Pagination<T> {
  /**
   * 总页数
   */
  private int totalPages;

  /**
   *  总记录数
   */
  private int totalNumber;

  /**
   * 下一页
   */
  private int nextPage;
  /**
   * 上一页
   */
  private int lastPage;
  /**
   * 是否还有下一页
   */
  private boolean hasNext;
  /**
   * 是否有上一页
   */
  private boolean hasLast;
  /**
   * 当前页号: 从1开始
   */
  private int number;
  /**
   * 每页大小
   */
  private int pageSize;

  //  用于存放满足条件的用户信息
  private List<T> content;

  public Pagination(List<T> ts, int page, int size, int count) {
    setTotalNumber(count);
    setPageSize(size);
    setTotalPages();
    setNumber(page);
    setContent(ts);
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public void setNextPage(int nextPage) {
    this.nextPage = nextPage;
  }

  /**
   * 设置当前页号
   * @param number
   */
  public void setNumber(int number) {
    this.number = (number <= 0 || totalPages <= 0) ? 0 : number >= totalPages ? totalPages : number;
  }

  public int getNumber() {
    return number;
  }

  /**
   * 设置每页大小
   * @param pageSize
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * 设置总记录数
   * @param totalNumber
   */
  public void setTotalNumber(int totalNumber) {
    this.totalNumber = totalNumber;
  }

  /**
   * 设置总页数
   * @param size 每页大小
   */
  public void setTotalPages() {
    double d = Math.ceil(totalNumber * 1.0 / pageSize);
    this.totalPages = (int) d;
  }

  public int getNextPage() {

    if (number >= totalPages) {
      return totalPages;
    }
    return totalPages + 1;
  }

  public int getLastPage() {
    if (number <= 0) {
      return 0;
    }
    return totalPages - 1;
  }

  public boolean getHasNext() {
    return number < totalPages ? true : false;
  }

  public boolean getHasLast() {
    return number > 1 ? true : false;
  }

  public int getPageSize() {
    return pageSize > totalPages ? totalPages : pageSize < 1 ? 1 : pageSize;
  }

  public int getTotalPages() {
    return totalPages;
  }

}
