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

    <!--
        Main calculator form
        action="" → will be updated dynamically by JavaScript
    -->
    <form action="" method="post" id="calcForm">

        <!--
            step="0.01" → allows decimal numbers (floating numbers)
            required → user must enter these fields
        -->
        <input type="number" step="0.01" name="num1" placeholder="Enter First Number" required>
        <input type="number" step="0.01" name="num2" placeholder="Enter Second Number" required>

        <!--
            Dropdown menu to choose which mathematical operation to perform.
            JavaScript will change the form action & method when user selects an option.
        -->
        <select name="operation" id="operation" required>
            <option value="">-- Choose Operation --</option>

            <!-- Existing operations -->
            <option value="add">Addition (Integer)</option>
            <option value="addFloat">Addition (Decimal Numbers)</option> <!-- NEW OPTION -->
            <option value="subtract">Subtraction (-)</option>
            <option value="multiply">Multiplication (×)</option>
            <option value="divide">Division (÷)</option>
        </select>

        <button type="submit">Calculate</button>
    </form>
</div>

<script>
/*
    This script listens for changes in the dropdown menu.
    Based on selected operation, it changes:
    ✔ form action → URL to call in Controller
    ✔ form method → GET or POST request
*/
document.getElementById("operation").addEventListener("change", function() {
    let op = this.value;

    if(op === "add") {
        // Calls your existing integer addition method
        document.getElementById("calcForm").action = "add";
        document.getElementById("calcForm").method = "post";
    }
    else if(op === "addFloat") {
        // NEW: Calls your floating number addition method using ModelAndView
        document.getElementById("calcForm").action = "addFloat";
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
