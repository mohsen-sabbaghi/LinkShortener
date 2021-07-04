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
    <title>Agriculture Bank Link Shortener</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width"/>
    <link rel="shortcut icon" href="favicon.ico?" type="image/x-icon"/>

    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script type='text/javascript' href='webjars/jquery/3.6.0/jquery.min.js'></script>
    <script type='text/javascript' href='webjars/popper.js/2.5.4/cjs/popper.js'></script>
    <script type='text/javascript' href='webjars/bootstrap/4.6.0/js/bootstrap.min.js'></script>

    <link rel='stylesheet' href='webjars/bootstrap/4.6.0/css/bootstrap.min.css'>

    <link rel="stylesheet" href="statics/css/mdb.min.css">

    <link rel="stylesheet" href="statics/css/fontello.css">
    <script src="statics/js/ls.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href=statics/css/ls.css>
</head>

<body class="hm-gradient">
<br>
<h2 class="text-center">Agriculture Bank Link Shortener Service</h2>
<hr>

<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <form method="post" action="/create/link" class="ls-form">

                <div class="row">

                    <div class="col-sm-10">
                        <label class="hiddend-label" for="longUrl">Url:</label>
                        <input class="form-control" type="text" placeholder="Enter Url..." name="longUrl"
                               id="longUrl" required oninvalid="this.setCustomValidity('Enter URL Here')"
                               oninput="this.setCustomValidity('')"
                               minlength="10"
                        >
                    </div>

                    <div class="col-sm-2">
                        <label class="hiddend-label" for="desire_link">Your Desired Url:</label>
                        <input class="form-control" type="text" placeholder="Enter your choice Url..."
                               name="desire_link" id="desire_link" maxlength="15">
                    </div>

                    <div class="col-sm-3 flex">
                        <label for="expired_date">Expired date:</label>
                        <input class="form-control" type="date" name="expired-date" id="expired_date" required
                               placeholder="Expired date (mm/dd/yyy)">
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="text-center">
                        <button type="submit" class="btn btn-dark-blue btn-block btn-large">
                            <i class="icon-hand-scissors-o"></i>
                            Make it Short!
                        </button>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>

</body>
</html>
