INSERT INTO users (full_name, email, username, password, role, is_verified, created_at, updated_at, deleted_at)
VALUES
  ('Admin User', 'admin@user.com', 'admin', '$2a$10$7GOLx7uLBwNuNq1oPxtPbuCb/5Q2g/P5ylWGM8tQyz0aDUp51M0am', 'SUPER_ADMIN', true, NOW(), NOW(), NULL),
  ('Editor User', 'editor@user.com', 'editor', '$2a$10$7GOLx7uLBwNuNq1oPxtPbuCb/5Q2g/P5ylWGM8tQyz0aDUp51M0am', 'EDITOR', true, NOW(), NOW(), NULL),
  ('Viewer User', 'viewer@user.com', 'viewer', '$2a$10$7GOLx7uLBwNuNq1oPxtPbuCb/5Q2g/P5ylWGM8tQyz0aDUp51M0am', 'VIEWER', true, NOW(), NOW(), NULL),
  ('Contributor User', 'contributor@user.com', 'contributor', '$2a$10$7GOLx7uLBwNuNq1oPxtPbuCb/5Q2g/P5ylWGM8tQyz0aDUp51M0am', 'CONTRIBUTOR', true, NOW(), NOW(), NULL);

INSERT INTO article (title, content, is_published, author_id, created_at, updated_at, deleted_at)
VALUES
  ('Spring Boot Introduction', 'Learn the basics of Spring Boot and how to build REST APIs.', true, 1, NOW(), NOW(), NULL),
  ('Advanced JPA Techniques', 'Explore advanced mapping and querying with JPA and Hibernate.', true, 2, NOW(), NOW(), NULL),
  ('Redis Integration in Java', 'How to use Redis for caching and session management in Java apps.', false, 4, NOW(), NOW(), NULL),
  ('Security Best Practices', 'Essential security tips for modern web applications.', true, 1, NOW(), NOW(), NULL),
  ('Testing with JUnit', 'A guide to writing effective unit and integration tests in Java.', false, 3, NOW(), NOW(), NULL);

