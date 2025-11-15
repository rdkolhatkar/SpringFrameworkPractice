<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Calculator Page</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>

<div class="container">
    <h1>Web Calculator</h1>
    <p>Select an operation:</p>

    <form action="" method="post" id="calcForm">
        <input type="number" name="num1" placeholder="Enter First Number" required>
        <input type="number" name="num2" placeholder="Enter Second Number" required>

        <select name="operation" id="operation" required>
            <option value="">-- Choose Operation --</option>
            <option value="add">Addition (+)</option>
            <option value="subtract">Subtraction (-)</option>
            <option value="multiply">Multiplication (×)</option>
            <option value="divide">Division (÷)</option>
        </select>

        <button type="submit">Calculate</button>
    </form>
</div>

<script>
document.getElementById("operation").addEventListener("change", function() {
    let op = this.value;

    if(op === "add") {
        document.getElementById("calcForm").action = "add";
        document.getElementById("calcForm").method = "post";
    }
    else if(op === "multiply") {
        document.getElementById("calcForm").action = "multiply";
        document.getElementById("calcForm").method = "post";
    }
    else if(op === "subtract") {
        document.getElementById("calcForm").action = "subtract";
        document.getElementById("calcForm").method = "get";
    }
    else if(op === "divide") {
        document.getElementById("calcForm").action = "divide";
        document.getElementById("calcForm").method = "get";
    }
});
</script>

</body>
</html>
