package com.example.haidtracker.data.model.auth;

public class LoginResponse {

        private String token;
        private User user;

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }

        public static class User {
            private String id;
            private String email;
            private String name;
            private String role;

            public String getId() {
                return id;
            }

            public String getEmail() {
                return email;
            }

            public String getName() {
                return name;
            }

            public String getRole() {
                return role;
            }
        }
    }


