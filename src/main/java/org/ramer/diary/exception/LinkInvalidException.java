/*
 *
 */

package org.ramer.diary.exception;

/**
 * 非法访问异常处理
 * @author ramer
 *
 */
public class LinkInvalidException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public LinkInvalidException(String message) {
    super(message);
  }

  public LinkInvalidException() {
  }
}
