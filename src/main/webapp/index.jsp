<%--
  Created by IntelliJ IDEA.
  User: Mohsen
  Date: 2/6/2021
  Time: 6:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Link Shortener</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<h2>Link Shortener...</h2>
<hr>
<%--<form method="post" action="/create/link">--%>
<%--    <label>Url: <input type="text" name="link" id="link"> </label>--%>
<%--    <br>--%>
<%--    <label>Expired date: <input type="date" name="expired-date" id="expired-date"> </label>--%>
<%--    <br>--%>
<%--    <input type="submit" value="Make it Short!">--%>
<%--</form>--%>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <form method="post" action="/create/link">
                <div class="form-group">
                    <label>Url: <input class="form-control" type="text" placeholder="Enter Url..." name="link"
                                       id="link">
                    </label>
                </div>
                <div class="form-group">
                    <label>Expired date: <input class="form-control" type="date" name="expired-date" id="expired-date">
                    </label>
                </div>

                <div class="form-group">
                    <label>Your Desired Url: <input class="form-control" type="text" placeholder="Enter Url..."
                                                    name="desire_link" id="desire_link"> </label>
                </div>
                <button type="submit" class="btn btn-default">Make it Short!</button>
            </form>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>
</body>
</html>
