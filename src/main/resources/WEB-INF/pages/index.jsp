<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
     <div align="center">

         <form action="/add" enctype="multipart/form-data" method="POST">
             Add Files: <input type="file" name="fileList" required multiple>
             <input type="submit" />
         </form>

         <form action="/toZip" enctype="multipart/form-data" method="POST">
             <input type="submit" value = "To ZIP"/>
         </form>

         <table class="table table-striped">
             <c:forEach items="${files}" var="entry">
                 <tr>
                     <td>${entry.value}</td>
                 </tr>
             </c:forEach>
         </table>

         <form action="/clean" enctype="multipart/form-data" method="POST">
             <input type="submit" value = "Clean"/>
         </form>

     </div>
  </body>
</html>
