package com.example.haidtracker.data.model.cycle;

public class CycleStats {
    private int userId;
    private Count _count;

    public CycleStats() {}

    public CycleStats(int userId, Count count) {
        this.userId = userId;
        this._count = count;
    }

    // Getters
    public int getUserId() { return userId; }
    public Count getCount() { return _count; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setCount(Count count) { this._count = count; }

    // Inner class for count structure
    public static class Count {
        private int id;

        public Count() {}

        public Count(int id) {
            this.id = id;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
}