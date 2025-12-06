<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Job Post</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-warning">
    <div class="container">
        <a class="navbar-brand fs-1 fw-medium" href="homePage">Job Portal Web App</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav"><span class="navbar-toggler-icon"></span></button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="homePage">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="viewAllJobs">All Jobs</a></li>
                <li class="nav-item"><a class="nav-link" href="https://telusko.com/">Contact</a></li>
            </ul>
        </div>
    </div>
</nav>


<div class="container mt-5">
    <h2 class="text-center mb-4">Add Job Post</h2>

    <form action="handleForm" method="post" class="border p-4 bg-light rounded">

        <div class="mb-3">
            <label class="form-label">Post ID</label>
            <input type="number" name="postId" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Job Profile</label>
            <input type="text" name="postProfile" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Description</label>
            <textarea name="postDescription" class="form-control" rows="3" required></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Required Experience (Years)</label>
            <input type="number" name="requiredExperience" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Tech Stack (comma separated)</label>
            <input type="text" name="postTechStack" class="form-control"
                   placeholder="Java, Spring Boot, Hibernate" required>
        </div>

        <button type="submit" class="btn btn-success w-100">Submit Job</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
