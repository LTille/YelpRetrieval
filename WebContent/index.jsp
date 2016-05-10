<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="Classes.Review"%>
<%@page import="Indexing.BuildIndex"%>
<%@page import="Indexing.MyIndexReader"%>
<%@page import="Indexing.OriginReviewReader" %>
<%@page import="Search.RetrievalModel" %>
<%@page import="DataPreprocess.BusinessScore"%>
<% 
    Map<String,List<String>> biz=(HashMap<String,List<String>>)session.getAttribute("business");
    String q = (String)session.getAttribute("q");
    OriginReviewReader oir = new OriginReviewReader();
    BusinessScore bs = new BusinessScore();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
  <meta charset="UTF-8">
  <title>SEARCH ENGINE UI</title>
  <link rel="stylesheet" href="css/style.css">
  <!--Google Fonts-->
  <link href='http://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>
  <link href='http://fonts.googleapis.com/css?family=Raleway:400,300,700,900' rel='stylesheet' type='text/css'>
  <!--Font Awesome-->
  <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  
  <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body data-spy="scroll" >
  <div class="overlay" style="opacity:50%"></div>  
  <!-- <div class="container-fluid"> -->
    
        
  <div class="wrapper">
    <div class="row">
      <div class="col-sm-2"></div>
      <div class="col-sm-8">
        <div class="form">
          <h1>Yelp Review Search</h1>
          <form class="search_bar" method="post" action="QueryServlet">
            <input type="text" class="search" name="query" placeholder="I am looking for......" value="<%=q==null?"":q%>"/>
            <button type="submit" value="Search">Search</button>
          </form>
          <p class="result">Generating Results...</p>
          <ul class="res clearfix">
            
            <% if(biz==null||biz.isEmpty()) { %>
             <div align="center"> Today is weekend</div>
            <% } else {
            %>
            <div class="panel-group" >
            <% 
              int i=0;
              for(String name:biz.keySet()){ %> 
              <div class="panel panel-default">
                <div class="panel-heading"  align="center">
                  <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse<%=i+1%>">
                    <% out.println(name);%> <strong><% out.println(" Business Star: "+bs.getScore(name));%></strong></a>
                  </h4>
                </div>
                <div id="collapse<%=i+1%>" class="panel-collapse collapse">
                  <% 
                    for(String r: biz.get(name)){%>
                  <div class="panel-body">
                    <% out.println(oir.getReviewContent(Integer.parseInt(r)));%>
                 </div>
                 <% }%>
                </div>
              </div>
             <%i++;}}%>   
            </div>  
          </ul>
        </div>
      </div>
      <div class="col-sm-2"></div>
    </div>

     
    <!-- </div> -->
  </div>
        
  </body>
</html>
