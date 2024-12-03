package io.github.seujorgenochurras.p2pApi.domain.service.github;

public class EditFileDto {
    private String message;
    private String content;
    private Commiter commiter;
    private String author;
    private String sha;

    public String getSha() {
        return sha;
    }

    public EditFileDto setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public EditFileDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EditFileDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Commiter getCommiter() {
        return commiter;
    }

    public EditFileDto setCommiter(Commiter commiter) {
        this.commiter = commiter;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public EditFileDto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public static final class Commiter {
        private String name;
        private String email;

        public Commiter(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Commiter() {
        }

        public String getName() {
            return name;
        }

        public Commiter setName(String name) {
            this.name = name;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public Commiter setEmail(String email) {
            this.email = email;
            return this;
        }
    }
}
