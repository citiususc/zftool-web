<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page language="java" import="java.util.*" %>
<%@page language="java" import="java.io.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>
<html lang="es">
<head>
  <title>ZebraFish</title>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
  <!-- CSS -->
  <link rel="stylesheet" href="./css/styles.css">
  <link rel="stylesheet" href="./css/lightbox.css">
</head>
<body background="./img/fondo3.jpg">
<c:remove var="rango"/>
<c:remove var="nGFP0"/>
<c:remove var="nGFP1"/>
<c:remove var="meanGFP0"/>
<c:remove var="meanGFP1"/>
<c:remove var="factorProliferacion"/>
<c:remove var="umbral"/>
<c:remove var="fin"/>
<c:remove var="paso"/>
<c:remove var="pez"/>
<c:remove var="hInicial"/>
<c:remove var="hFinal"/>
<c:remove var="posicionUmbral"/>
<div class="container-fluid" >
  <h1 class="centrado text-white">ZEBRAFISH</h1>
  <div class="form-group">
    <form action="./carga2D.jsp">
      <input class="btn btn-lg centradoabajo"  type="submit" value="View More" />
    </form>
  </div>
</div>

<script src="./web/WEB-INF/lib/lightbox-plus-jquery.js"></script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
</body>
</html>
