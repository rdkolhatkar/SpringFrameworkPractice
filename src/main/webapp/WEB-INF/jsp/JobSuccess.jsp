<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Job Details</title>
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
        rel="stylesheet"
        crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4 text-center">Job Details</h2>

    <c:choose>
        <c:when test="${not empty job}">
            <div class="card border-dark">
                <div class="card-body">
                    <h4 class="card-title">${job.postProfile}</h4>
                    <p class="card-text"><strong>Description:</strong> ${job.postDescription}</p>
                    <p class="card-text"><strong>Experience Required:</strong> ${job.requiredExperiance} years</p>
                    <p class="card-text"><strong>Tech Stack:</strong></p>

                    <c:choose>
                        <c:when test="${not empty job.postTechStack}">
                            <ul>
                                <c:forEach var="tech" items="${job.postTechStack}">
                                    <li>${tech}</li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <p>No tech stack specified.</p>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="alert alert-warning text-center" role="alert">
                No job details available.
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>
</html>
