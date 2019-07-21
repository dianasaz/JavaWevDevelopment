<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans:600'>
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
</head>
<body>

<div class="limiter">
    <div class="container-login100">
        <div class="row main-form">
            <c:choose>
                <c:when test="${param.register != null}">

                    <!--Registration form-->
                    <form class="login100-form" method="POST"
                          action="controller?command=register">
					<span class="login100-form-title">
						Sign Up
					</span>

                        <div class="form-group">
                            <label for="login" class="cols-sm-2 control-label">Log in</label>
                            <div class="cols-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-user fa" aria-hidden="true"></i></span>
                                    <input type="text" class="form-control" name="login" id="login" placeholder="Enter your login"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="name" class="cols-sm-2 control-label">Your Name</label>
                            <div class="cols-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-user fa" aria-hidden="true"></i></span>
                                    <input type="text" class="form-control" name="name" id="name" placeholder="Enter your Name"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="email" class="cols-sm-2 control-label">Your Email</label>
                            <div class="cols-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-envelope fa" aria-hidden="true"></i></span>
                                    <input type="text" class="form-control" name="email" id="email" placeholder="Enter your Email"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="phoneNumber" class="cols-sm-2 control-label">phoneNumber</label>
                            <div class="cols-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-users fa" aria-hidden="true"></i></span>
                                    <input type="text" class="form-control" name="phoneNumber" id="phoneNumber" placeholder="Enter your phoneNumber"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="password" class="cols-sm-2 control-label">Password</label>
                            <div class="cols-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-lock fa-lg" aria-hidden="true"></i></span>
                                    <input type="password" class="form-control" name="password" id="password" placeholder="Enter your Password"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="address" class="cols-sm-2 control-label">Address</label>
                            <div class="cols-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-lock fa-lg" aria-hidden="true"></i></span>
                                    <input type="text" class="form-control" name="address" id="Address" placeholder="Address"/>
                                </div>
                            </div>
                        </div>


                        <c:if test="${error_registration eq true}">
                            <div class="container alert alert-warning alert-dismissible fade show m-t-16" role="alert">
                                User with this login already exist. Sign in or choose another login.
                            </div>
                        </c:if>


                        <div class="container-login100-form-btn">
                            <button type="submit" class="login100-form-btn">
                                Sign up
                            </button>
                        </div>


                        <div class="flex-col-c p-t-170 p-b-40">
						<span class="txt1 p-b-9">
							Have an account?
						</span>

                            <a href="controller?command=login" class="txt3">
                                Sign in now
                            </a>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <form class="login100-form validate-form p-l-55 p-r-55 p-t-178" method="POST"
                          action="controller?command=login">
					<span class="login100-form-title">
						Sign In
					</span>

                        <div class="wrap-input100 validate-input m-b-16" data-validate="Please enter login">
                            <input class="input100" type="text" name="login" placeholder="Login">
                            <span class="focus-input100"></span>
                        </div>

                        <div class="wrap-input100 validate-input m-b-16" data-validate="Please enter password">
                            <input class="input100" type="password" name="password" placeholder="Password">
                            <span class="focus-input100"></span>
                        </div>


                        <c:if test="${error_authentification eq true}">
                            <div class="container alert alert-warning alert-dismissible fade show m-t-16" role="alert">
                                Enter <strong>invalid</strong> login or password. Please try again

                            </div>
                        </c:if>

                        <div class="container-login100-form-btn">
                            <button type="submit" class="login100-form-btn">
                                Sign in
                            </button>
                        </div>

                        <a href="controller?command=home_page" class="txt2 flex-col-c p-t-16">
                            Continue as guest
                        </a>

                        <div class="flex-col-c p-t-120 p-b-40">
						<span class="txt1 p-b-9">
							Don"t have an account?
						</span>
                            <c:url value="login.jsp" var="toRegistr">
                                <c:param name="register" value="true"/>
                            </c:url>
                            <div class="form-group ">
                                <a href="controller?command=register" target="_blank" type="button" id="button" class="login-button">Register</a>
                            </div>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>


<!--===============================================================================================-->
<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/animsition/js/animsition.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/bootstrap/js/popper.js"></script>
<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/daterangepicker/moment.min.js"></script>
<script src="vendor/daterangepicker/daterangepicker.js"></script>
<!--===============================================================================================-->
<script src="vendor/countdowntime/countdowntime.js"></script>
<!--===============================================================================================-->
<script src="js/main.js"></script>

<style>
    .form-group{
        margin-bottom: 15px;
    }

    .login100-form{
        margin-left: 43%;

    }
    label{
        margin-bottom: 15px;
    }
    input,
    input::-webkit-input-placeholder {
        font-size: 11px;
        padding-top: 3px;
    }
    .form-control {
        height: auto!important;
        padding: 8px 12px !important;
    }
    .input-group {
        box-shadow: 0px 2px 5px 0px rgba(0,0,0,0.21)!important;
    }

    .login100-form-title{
        margin-left: 10%;
        font-size: 15px;
    }
    #button {
        border: 1px solid #ccc;
        margin-top: 28px;
        padding: 6px 12px;
        color: #666;
        text-shadow: 0 1px #fff;
        cursor: pointer;
        border-radius: 3px 3px;
        box-shadow: 0 1px #fff inset, 0 1px #ddd;
        background: #f5f5f5;
        background: -moz-linear-gradient(top, #f5f5f5 0%, #eeeeee 100%);
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #f5f5f5), color-stop(100%, #eeeeee));
        background: -webkit-linear-gradient(top, #f5f5f5 0%, #eeeeee 100%);
        background: -o-linear-gradient(top, #f5f5f5 0%, #eeeeee 100%);
        background: -ms-linear-gradient(top, #f5f5f5 0%, #eeeeee 100%);
        background: linear-gradient(top, #f5f5f5 0%, #eeeeee 100%);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#f5f5f5', endColorstr='#eeeeee', GradientType=0);
    }
    .container-login100{
        margin-top: 30px;
        margin: 0 auto;
        padding: 10px 40px;
        background:#009edf;
        color: #FFF;
        text-shadow: none;
        box-shadow: 0px 3px 5px 0px rgba(0,0,0,0.31);
        height: auto;
    }
    span.input-group-addon i {
        color: #009edf;
        font-size: 17px;
    }
    .login-button{
        margin-top: 5px;
    }

    .main-form {
        width: auto;
    }
</style>
</body>

</html>


