<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/default.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/visitOther.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
<script src="${pageContext.request.contextPath}/js/js.cookie.js"></script>
<script type="text/javascript">
  $(function(){
      path = "${pageContext.request.contextPath}";
      userid = "${other.id}";
  })
</script>
<script src="${pageContext.request.contextPath}/js/ramer/visitOther.js"></script>
  <title>
    Personal Center
  </title>
</head>
<body>
<!--  返回-->
<div class="return_link" >
    <img alt="error" src="${pageContext.request.contextPath}/pictures/back.png" id="back">
</div>
<!--  返回主页-->
<div class="return_home">
    <img alt="error" src="${pageContext.request.contextPath}/pictures/home.png" id="home">
</div>
    <div class="user_info_panel">
      <div class="head">
        <a>
          <img alt="error" src="${pageContext.request.contextPath}/${other.head }"
            onerror="javascript:this.src='${pageContext.request.contextPath}/pictures/userHead.jpg'">
        </a>
      </div>
      <div class="selfinfo">
        <div class="name">
          ${other.name}
          <br>
        </div>
        <div class="contact">
          <span id="followSpan">
            <c:if test="${isFollowed eq true}">
             <a href="${pageContext.request.contextPath}/user/topic/notFollow" class="notFollow">
                <i class="icon-minus" id="icon"></i><i id="text" style="font-style: normal;">取消关注</i>
            </a>
          </c:if>
          <c:if test="${isFollowed ne true}">
            <a href="${pageContext.request.contextPath}/user/topic/follow" class="follow">
              <i class="icon-plus" id="icon"></i><i id="text" style="font-style: normal;">关注</i>
            </a>
          </c:if>
            </span>
            <span>
              QQ: ${!empty other.qqNum ? other.qqNum : "无"}
            </span>
            <span>
              微博: ${!empty other.weiboNum ? other.weiboNum : "无"}
            </span>
            <span>
              <a id="showFollowPanel" href="#">
                他/她关注的<i class="icon-arrow-down"></i>
              </a>
            </span>
            <span>
              <a href="javascript:void(0)" id="notify">
                <i class="icon-envelope">
                </i>
              </a>
            </span>
          </div>
        </div>
      </div>

      <!-- 私信面板 -->
      <div class="notify_panel" id="notify_panel">
        <form 
            action="${pageContext.request.contextPath}/user/personal/sendPrivMess"
            method="post"
            enctype="multipart/form-data"
            id="sendPrivMessForm">
          <textarea id="privMessContent" rows="7" cols="30" class="notify_content" name="content"></textarea>
          <input type="submit" value="发送私信" id="sendPrivMess">
          <input type="reset" value="收起" class="hiddenNotify">
        </form>
      </div>

      <!-- 关注面板 -->
      <div class="follow_panel clearfix" id="followPanel">
        <c:if test="${empty topic.user.follows }">
          <span>
            他/她还没有关注任何人 !
          </span>
        </c:if>
        <c:if test="${!empty topic.user.follows }">
          <c:forEach items="${topic.user.follows}" var="follow">
            <div class="followed_user_panel">
              <div>
                <a href="${pageContext.request.contextPath}/user/personal/${follow.followedUser.id}">
                  <img 
                      alt="error"
                      src="${pageContext.request.contextPath}/${follow.followedUser.head }"
                      class="followed_user_head">

                </a>
              </div>
              <div class="followed_user_name_panel">
                <a href="${pageContext.request.contextPath}/user/personal/${follow.followedUser.id}">
                  ${follow.followedUser.name}
                </a>
              </div>
            </div>
          </c:forEach>
        </c:if>
      </div>

      <!-- 显示他的分享 -->
      <c:if test="${empty topicsPage.content}">
        <div class="no_topic">
          <strong>
            他还没有发布任何分享
          </strong>
        </div>
      </c:if>
      <c:if test="${!empty topicsPage.content}">
        <c:forEach items="${topicsPage.content}" var="topic">
          <div class="show_topic">
            <div class="side">
              <div class="day">
                <a href="#">
                  ${topic.date.date}/${topic.date.month+1}
                </a>
              </div>
            </div>
            <div class="main">
              <div class="content">
                <c:if test="${!empty topic.picture }">
                    <div class="img">
                        <img alt="error" src="${pageContext.request.contextPath}/${topic.picture}" class="topic_pic">
                    </div>
                    <p class="desc"></p>
                </c:if>
                <c:if test="${empty topic.picture }">
                    <p class="desc" style="height: 35px;"></p>
                </c:if>
                <div class="text">${topic.content }</div>
                <!-- 标签分享 -->
                <div class="tags_panel">
                 <c:forEach items="${topic.tags}" var="tag">
                   <i class="icon-tags"></i>
                   <span class="tags">${tag}</span>
                 </c:forEach>
                </div>
                <!-- 通过标签获取分享表单 -->
                <form action="${pageContext.request.contextPath}/home/tag" id="tagForm">
                  <input type="text" name = "tag" id="tagName">
                </form>
              </div>
            </div>

            <hr class="h_line">
            <c:set var="isPraise" value="false">
            </c:set>
            <c:forEach items="${praises }" var="p">
              <c:if test="${topic.id eq p }">
                <c:set var="isPraise" value="true">
                </c:set>
              </c:if>
            </c:forEach>
            <c:if test="${ isPraise eq true}">
              <a href="${pageContext.request.contextPath}/user/topic/notPraise/${topic.id}" class="thumbsup">
                <i class="icon-thumbs-up-ramer">
                </i>
              </a>
            </c:if>
            <c:if test="${ isPraise ne true}">
              <a href="${pageContext.request.contextPath}/user/topic/praise/${topic.id}" class="thumbsup">
                <i class="icon-thumbs-up">
                </i>
              </a>
            </c:if>
            <small class="upCounts">
              (${empty topic.upCounts ? 0 : topic.upCounts})
            </small>
            <a href="${pageContext.request.contextPath}/user/topic/comment/${topic.id}" class="comment">
              <i class="icon-edit">
              </i>
            </a>
            <c:set var="isFavourite" value="false">
            </c:set>
            <c:forEach items="${favourites }" var="f">
              <c:if test="${topic.id eq f }">
                <c:set var="isFavourite" value="true">
                </c:set>
              </c:if>
            </c:forEach>
            <c:if test="${ isFavourite eq true}">
              <a href="${pageContext.request.contextPath}/user/topic/notFavourite/${topic.id}" class="favourite">
                <i class="icon-star">
                </i>
              </a>
            </c:if>
            <c:if test="${ isFavourite ne true}">
              <a href="${pageContext.request.contextPath}/user/topic/favourite/${topic.id}" class="favourite">
                <i class="icon-star-empty">
                </i>
              </a>
            </c:if>
            <!-- 评论表单 -->
            <div class="comment_form_panel">
              <form action="" method="post" class="comment_form">
                <textarea rows="7" cols="30" class="comment_content" name="content"></textarea>
                <input type="hidden" name="user" value="${user.id }" />
                <input type="submit" value="发表评论">
                <input type="reset" value="收起" class="hiddenCommentForm">
              </form>
            </div>
            <!-- 显示评论 -->
            <c:forEach items="${topic.comments}" var="comment">
              <div class="comment_panel">
                <p>
                  ${comment.user.name } : ${comment.content }
                  <sub>
                    &nbsp;&nbsp;<fmt:formatDate value="${comment.date}" pattern="HH:mm:ss yyyy-MM-dd"/>
                  </sub>
                  <a href="${pageContext.request.contextPath}/user/topic/comment/reply/${comment.id}" class="reply">
                    <i class="icon-edit">
                    </i>
                  </a>
                </p>
                <!-- 回复评论表单 -->
                <div class="reply_form_panel">
                  <form action="" method="post" class="reply_form">
                    <textarea rows="7" cols="30" class="reply_content" name="content"></textarea>
                    <input type="submit" value="发表评论">
                    <input type="reset" value="收起" class="hiddenReplyForm">
                  </form>
                </div>
                <c:forEach items="${comment.replies}" var="reply">
                  <p>
                    ${reply.user.name}&nbsp;回复&nbsp;${comment.user.name} : ${reply.content }
                    <sub>
                      &nbsp;&nbsp;<fmt:formatDate value="${reply.date}" pattern="HH:mm:ss yyyy-MM-dd"/>
                    </sub>
                    <a 
                        href="${pageContext.request.contextPath}/user/topic/comment/reply/${reply.comment.id}"
                        class="reply2">

                      <i class="icon-edit">
                      </i>
                    </a>
                  </p>
                  <!-- 回复回复表单 -->
                  <div class="reply_double_form_panel">
                    <form action="" method="post" class="reply_double_form">
                      <textarea rows="7" cols="30" class="reply_double_content" name="content"></textarea>
                      <input type="submit" value="回复">
                      <input type="reset" value="收起" class="hiddenReplyDoubleForm">
                    </form>
                  </div>
                </c:forEach>


              </div>
            </c:forEach>
          </div>
        </c:forEach>
      </c:if>
      
<!-- 存储记录的总页数 -->
<input id="totalPages" type="hidden" value="${topicsPage.totalPages }">
<!-- 存储当前页号 -->
<input id="number" type="hidden" value="${topicsPage.number}">
<!-- 分页 -->
<ul class="page_panel">
  <li class="lastPage">
    <a href="?pageNum=${topicsPage.number + 1 - 1}" id="lastPage">
      <img alt="error" src="${pageContext.request.contextPath}/pictures/previous.png" >
    </a>
  </li>
  <li class="nextPage">
    <a href="?pageNum=${topicsPage.number + 1 + 1}" id="nextPage">
      <img alt="error" src="${pageContext.request.contextPath}/pictures/next.png" >
    </a>
  </li>
</ul>
  </body>
</html>
