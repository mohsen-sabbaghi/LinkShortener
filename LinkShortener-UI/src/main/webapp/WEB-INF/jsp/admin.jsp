<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: Mohsen
  Date: 2/6/2021
  Time: 6:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Agriculture Bank Link Shortener</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico?" type="image/x-icon"/>

    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script type='text/javascript' src='webjars/jquery/3.6.0/jquery.min.js'></script>
    <%--    <script type='text/javascript' src='webjars/popper.js/2.5.4/cjs/popper.js'></script>--%>
    <script type='text/javascript' src='webjars/bootstrap/4.6.0/js/bootstrap.min.js'></script>
    <!-- #region datatables files -->
    <link rel="stylesheet" type="text/css" href="webjars/datatables/1.10.25/css/jquery.dataTables.min.css"/>
    <script src="webjars/sweetalert/2.1.0/sweetalert.min.js"></script>
    <script src="webjars/datatables/1.10.25/js/jquery.dataTables.min.js"></script>
    <!-- #endregion -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/statics/css/fontello.css">
    <link rel='stylesheet' href='webjars/bootstrap/4.6.0/css/bootstrap.min.css'>


    <script src="${pageContext.request.contextPath}/statics/js/admin.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href=${pageContext.request.contextPath}/statics/css/ls.css>


</head>

<body class="hm-gradient">
<%
    application.setAttribute("serverInfo", application.getServerInfo());
    application.setAttribute("systemTime", new Date());
%>

<div class="container-fluid">
    <br>
    <h2 class="text-center">Link Shortener Administration</h2>
    <a href="/shorten">Home</a>
    <hr>

    <p>
        <b>System Time:</b> ${systemTime} <br/>
        <b>Build Date:</b> ${appBuildDate} <br/>
        <b>Application Version:</b> ${appVersion}
    </p>

    <div class="col">
        <div class="row align-items-center">
            <table class="table table-bordered table-striped table-hover" id="tbl_links">
                <thead class="thead-dark">
                <tr>
                    <th scope="col"> Id</th>
                    <th scope="col"> Short Link</th>
                    <th scope="col"> Long Link</th>
                    <th scope="col"> Enabled</th>
                    <th scope="col"> Created Date</th>
                    <th scope="col"> Expires Date</th>
                    <th scope="col"> Options</th>
                </tr>
                </thead>

                <tbody>
                <%--    <jsp:useBean id="allLinks" scope="request" type="ir.bki.entities.Links"/>--%>
                <c:forEach items="${allLinks}" var="al">

                    <tr>
                        <td>${al.id}</td>
                        <td><a href="../${al.shortLink}" target="_blank">${al.shortLink}</a></td>
                        <td><a href="${al.redirectLink}" target="_blank">${al.redirectLink}</a></td>
                        <td>${al.enabled}</td>
                        <td>${al.createdDate}</td>
                        <td>${al.expiresDate}</td>
                        <td>
                            <a title="Remove Link" href="#" class="remove" id="${al.id}">
                                <i class="icon-trash"></i>
                            </a>
                            <c:choose>
                                <c:when test="${al.enabled == 'true'}">
                                    <a title="Click to Disable Link" href="#" class="disable" id="${al.id}">
                                        <i class="icon-eye icon-enabled"></i>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a title="Click to Enable Link" href="#" class="disable" id="${al.id}">
                                        <i class="icon-eye-off icon-disabled"></i>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                </c:forEach>
                </tbody>

            </table>
        </div>
        <div class="row">
            <b>Total Count: &nbsp</b> ${linksCount}
        </div>
    </div>
</div>


</body>
</html>
