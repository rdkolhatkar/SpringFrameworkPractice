<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Student Added Successfully</title>

    <!-- Linking CSS -->
    <link rel="stylesheet" type="text/css" href="/css/StudentSuccessStyle.css">
</head>

<body>

<div class="success-container">
    <h1>${message}</h1>

    <div class="details-card">
        <p><strong>Student Name:</strong> ${studentName}</p>
        <p><strong>Generated Student ID:</strong> ${studentId}</p>
    </div>

    <a href="/student-form" class="btn">Register Another Student</a>
</div>

</body>
</html>
