<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Greetings Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            padding: 40px;
            text-align: center;
        }
        .container {
            background: white;
            padding: 25px;
            width: 400px;
            margin: 0 auto;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
        }
        p {
            font-size: 18px;
        }
        .btn {
            padding: 10px 15px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
        }
        .btn:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>👋 Welcome to the Greetings Page</h1>

    <p>Enter your name and I'll greet you!</p>

    <form action="greet" method="post">
        <input type="text" name="username" placeholder="Enter your name" required
               style="padding: 8px; width: 80%; margin-bottom: 10px;">
        <br>
        <button type="submit" class="btn">Say Hello</button>
    </form>
</div>

</body>
</html>
