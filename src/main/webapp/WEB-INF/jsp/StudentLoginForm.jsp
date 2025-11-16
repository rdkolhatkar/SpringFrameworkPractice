<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Student Registration Form</title>
    <link rel="stylesheet" type="text/css" href="/css/StudentLoginFormStyle.css">
</head>
<body>

<div class="form-container">
    <h2>Student Registration</h2>

    <!-- Form action hits controller "/submit-student" -->
    <!-- modelAttribute="student" binds fields to StudentData.java -->
    <form action="/submit-student" method="post">

        <label>First Name:</label>
        <input type="text" name="firstName" required>

        <label>Last Name:</label>
        <input type="text" name="lastName" required>

        <label>Date of Birth:</label>
        <input type="date" name="dob" required>

        <label>Address:</label>
        <textarea name="address" required></textarea>

        <label>Email:</label>
        <input type="email" name="email" required>

        <label>Country:</label>
        <input type="text" name="country" required>

        <label>State:</label>
        <input type="text" name="state" required>

        <label>Postal Code:</label>
        <input type="text" name="postalCode" required>

        <button type="submit">Register Student</button>
    </form>
</div>

</body>
</html>
