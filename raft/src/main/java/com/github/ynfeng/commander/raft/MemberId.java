package com.github.ynfeng.commander.raft;

public class MemberId {
    private final String id;

    private MemberId(String id) {
        this.id = id;
    }

    public static MemberId create(String id) {
        return new MemberId(id);
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberId)) {
            return false;
        }

        MemberId memberId = (MemberId) o;

        return id.equals(memberId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MemberId{" +
            "id='" + id + '\'' +
            '}';
    }
}
